document.addEventListener('DOMContentLoaded', () => {
  const categoryContainer = document.querySelector('.category-picker');
  const categoryInput = document.getElementById('category');

  // --- Category Selection ---
  if (categoryContainer && categoryInput) {
    // Initial Highlight
    const currentVal = categoryInput.value;
    if (currentVal) {
      const btn = categoryContainer.querySelector(`.cat-btn[data-value="${currentVal}"]`);
      if (btn) btn.classList.add('active');
    }

    categoryContainer.addEventListener('click', (e) => {
      const btn = e.target.closest('.cat-btn');
      if (!btn) return;

      // Remove active from all
      categoryContainer.querySelectorAll('.cat-btn').forEach(b => b.classList.remove('active'));
      
      // Add active to clicked
      btn.classList.add('active');
      
      // Update hidden input
      categoryInput.value = btn.dataset.value;
    });
  }
});