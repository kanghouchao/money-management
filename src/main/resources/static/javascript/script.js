document.addEventListener('DOMContentLoaded',()=>{
  const KEY='expenses_v1'
  const form=document.getElementById('expense-form')
  const categoryPicker = document.getElementById('category')
  const categoryInput = document.getElementById('category-input')

  if(categoryPicker){
    categoryPicker.querySelectorAll('.cat-btn').forEach(b=>b.setAttribute('aria-checked','false'))
    categoryPicker.addEventListener('click', e=>{
      const btn = e.target.closest('.cat-btn')
      if(!btn) return
      categoryPicker.querySelectorAll('.cat-btn').forEach(b=>{b.classList.remove('active'); b.setAttribute('aria-checked','false')})
      btn.classList.add('active')
      btn.setAttribute('aria-checked','true')
      if(categoryInput) categoryInput.value = btn.dataset.value || ''
    })
  } 

  let expenses=load()

  form.addEventListener('submit',e=>{
    e.preventDefault()
    const date=document.getElementById('date').value
    const category=(document.getElementById('category-input')||{}).value
    const amount=parseFloat(document.getElementById('amount-input').value)
    const note=document.getElementById('note').value.trim()
    if(!date||!category||Number.isNaN(amount))return
    const item={id:Date.now().toString(),date,category,amount,note}
    expenses.push(item)
    save()
    form.reset()
    // clear category selection UI
    if(categoryInput) categoryInput.value = ''
    if(categoryPicker) categoryPicker.querySelectorAll('.cat-btn').forEach(b=>{b.classList.remove('active'); b.setAttribute('aria-checked','false')})
    // clear amount display
    const amountInput = document.getElementById('amount-input')
    const amountDisplay = document.getElementById('amount-display')
    if(amountInput) amountInput.value = ''
    if(amountDisplay) amountDisplay.textContent = '0.00'
    document.getElementById('date').focus()
  })

  // clear-all button removed from UI; no list rendering necessary

  function save(){localStorage.setItem(KEY,JSON.stringify(expenses))}
  function load(){try{return JSON.parse(localStorage.getItem(KEY))||[]}catch(e){return[]}}

  // Saved expenses UI removed — no render function

  function categoryBadgeHTML(cat){
    const map = {
      'Food': {color:'#16a34a', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M8 3v7" stroke="#fff" stroke-width="2" stroke-linecap="round"/><path d="M12 3v7" stroke="#fff" stroke-width="2" stroke-linecap="round"/><path d="M8 10h4" stroke="#fff" stroke-width="2" stroke-linecap="round"/></svg>'},
      'Transport': {color:'#f97316', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><rect x="3" y="7" width="18" height="7" rx="2" fill="#fff"/></svg>'},
      'Utilities': {color:'#db2777', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M13 2L3 14h7l-1 8 10-12h-7l1-8z" fill="#fff"/></svg>'},
      'Entertainment': {color:'#1d4ed8', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M12 2l2.9 6.3L21 9.1l-5 3.8L17 21l-5-3.2L7 21l1-8.1L3 9.1l6.1-.8L12 2z" fill="#fff"/></svg>'},
      'Other': {color:'#475569', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M21 10v7a2 2 0 0 1-2 2H7l-4-4V5a2 2 0 0 1 2-2h11" fill="#fff"/></svg>'},
      'Clothes': {color:'#7c3aed', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M4 7l3-3h10l3 3v11a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V7z" fill="#fff"/></svg>'},
      'Medical': {color:'#dc2626', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M13 11h6v2h-6v6h-2v-6H5v-2h6V5h2v6z" fill="#fff"/></svg>'},
      'Housing': {color:'#0ea5a4', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M12 3l9 7v9a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1V10l9-7z" fill="#fff"/></svg>'},
      'Phone Bill': {color:'#f59e0b', svg:'<svg width="22" height="22" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M7 2h10v2H7V2zm5 4a4 4 0 1 1 0 8 4 4 0 0 1 0-8z" fill="#fff"/></svg>'}
    }
    const info = map[cat] || map['Other']
    return `<div style="width:44px;height:44px;display:flex;align-items:center;justify-content:center;background:${info.color};border-radius:8px">${info.svg}</div>`
  }

  // remove() not needed without list UI

  function formatCurrency(n){return new Intl.NumberFormat(undefined,{style:'currency',currency:'USD',maximumFractionDigits:2}).format(n)}
  function escapeHtml(s){return (s||'').replace(/[&<>"']/g,ch=>({"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"})[ch])}

  // --- Scrollable date picker ---
  const dateInput = document.getElementById('date')
  const dp = document.getElementById('date-picker')
  const dpBackdrop = document.getElementById('dp-backdrop')
  const dpDone = document.getElementById('dp-done')
  const dpDay = document.getElementById('dp-day')
  const dpMonth = document.getElementById('dp-month')
  const dpYear = document.getElementById('dp-year')

  const ITEM_HEIGHT = 40

  function buildColumn(el, items){
    el.innerHTML = ''
    for(const txt of items){
      const it = document.createElement('div')
      it.className = 'dp-item'
      it.textContent = txt
      el.appendChild(it)
    }
  }

  function daysInMonth(y,m){ return new Date(y,m,0).getDate() }

  function centerColumnTo(col, index){ col.scrollTop = index * ITEM_HEIGHT }
  function getIndexFromScroll(col){ return Math.round(col.scrollTop / ITEM_HEIGHT) }

  function populatePicker(initDate){
    const now = initDate || new Date()
    const curY = now.getFullYear()
    const curM = now.getMonth()+1
    const curD = now.getDate()

    const years = []
    const base = (new Date()).getFullYear()
    for(let y=base-50;y<=base+10;y++) years.push(String(y))
    const months = []
    for(let m=1;m<=12;m++) months.push(String(m).padStart(2,'0'))
    const days = []
    for(let d=1; d<=31; d++) days.push(String(d).padStart(2,'0'))

    buildColumn(dpYear, years)
    buildColumn(dpMonth, months)
    buildColumn(dpDay, days)

    centerColumnTo(dpYear, years.indexOf(String(curY)))
    centerColumnTo(dpMonth, curM-1)
    centerColumnTo(dpDay, curD-1)
  }

  function adjustDaysForMonth(){
    const y = parseInt(dpYear.children[getIndexFromScroll(dpYear)].textContent,10)
    const m = parseInt(dpMonth.children[getIndexFromScroll(dpMonth)].textContent,10)
    const max = daysInMonth(y,m)
    // ensure day list contains at least up to max, but keep 01-31 for consistent snapping
    const currIndex = getIndexFromScroll(dpDay)
    if(currIndex >= max) centerColumnTo(dpDay, max-1)
  }

  function snapOnScroll(col){
    let tid = null
    col.addEventListener('scroll', ()=>{
      if(tid) clearTimeout(tid)
      tid = setTimeout(()=>{
        const idx = getIndexFromScroll(col)
        centerColumnTo(col, idx)
        if(col===dpMonth || col===dpYear) adjustDaysForMonth()
      }, 120)
    })
  }

  [dpDay, dpMonth, dpYear].forEach(snapOnScroll)

  function openPicker(){
    let init = null
    if(dateInput.value){
      const parts = dateInput.value.split('-').map(Number)
      if(parts.length===3) init = new Date(parts[0], parts[1]-1, parts[2])
    }
    populatePicker(init)
    dp.classList.remove('hidden')
    dp.setAttribute('aria-hidden','false')
  }

  function closePicker(){ dp.classList.add('hidden'); dp.setAttribute('aria-hidden','true') }

  dateInput.addEventListener('click', openPicker)
  dpBackdrop.addEventListener('click', closePicker)

  dpDone.addEventListener('click', ()=>{
    const y = dpYear.children[getIndexFromScroll(dpYear)].textContent
    const m = dpMonth.children[getIndexFromScroll(dpMonth)].textContent
    const dRaw = dpDay.children[getIndexFromScroll(dpDay)].textContent
    const max = daysInMonth(parseInt(y,10), parseInt(m,10))
    const d = Math.min(parseInt(dRaw,10), max)
    dateInput.value = `${y}-${String(m).padStart(2,'0')}-${String(d).padStart(2,'0')}`
    closePicker()
  })

  // close on Escape
  document.addEventListener('keydown',(e)=>{ if(e.key==='Escape' && !dp.classList.contains('hidden')) closePicker() })

  // --- Amount currency + calculator ---
  const currencySelect = null
  const amountDisplay = document.getElementById('amount-display')
  const amountInputHidden = document.getElementById('amount-input')
  const calc = document.getElementById('calc')
  const calcBackdrop = document.getElementById('calc-backdrop')
  const calcDisplay = document.getElementById('calc-display')

  function formatForCurrency(value){
    const cur = 'JPY'
    if(value===''||value===null||Number.isNaN(Number(value))) return new Intl.NumberFormat(undefined,{style:'currency',currency:cur,maximumFractionDigits:0}).format(0)
    return new Intl.NumberFormat(undefined,{style:'currency',currency:cur,maximumFractionDigits:0}).format(Number(value))
  }

  // open calculator
  amountDisplay && amountDisplay.addEventListener('click', ()=>{
    const v = amountInputHidden && amountInputHidden.value ? String(amountInputHidden.value) : '0'
    calcDisplay.textContent = v
    calc.classList.remove('hidden')
    calc.setAttribute('aria-hidden','false')
  })

  function closeCalc(){ calc.classList.add('hidden'); calc.setAttribute('aria-hidden','true') }
  calcBackdrop && calcBackdrop.addEventListener('click', closeCalc)

  // keypad handling
  const keys = document.querySelectorAll('.calc-key')
  keys.forEach(k=>{
    k.addEventListener('click', ()=>{
      const txt = k.textContent.trim()
      let cur = calcDisplay.textContent || '0'
      if(txt === 'C'){
        cur = '0'
      } else if(txt === 'DEL'){
        if(cur.length<=1) cur = '0'
        else cur = cur.slice(0,-1)
      } else if(k.dataset.action === 'done' || txt === 'OK'){
        const num = parseFloat(cur)
        if(!Number.isNaN(num)){
          amountInputHidden.value = String(num)
          amountDisplay.textContent = formatForCurrency(num)
        }
        closeCalc()
        return
      } else if(txt === '.'){
        if(!cur.includes('.')) cur = cur + '.'
      } else {
        if(cur === '0') cur = txt
        else cur = cur + txt
      }
      calcDisplay.textContent = cur
    })
  })

  // currency selector removed; amounts use JPY by default

  document.addEventListener('keydown',(e)=>{ if(e.key==='Escape' && !calc.classList.contains('hidden')) closeCalc() })

  if(amountInputHidden && amountInputHidden.value) amountDisplay.textContent = formatForCurrency(amountInputHidden.value)

})