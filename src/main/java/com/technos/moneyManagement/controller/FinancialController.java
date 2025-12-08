package com.technos.moneyManagement.controller;

import com.technos.moneyManagement.controller.request.ExpenseForm;
import com.technos.moneyManagement.controller.request.IncomeForm;
import com.technos.moneyManagement.controller.vo.CalendarDayView;
import com.technos.moneyManagement.controller.vo.FinancialRecordView;
import com.technos.moneyManagement.repository.entity.ExpenseCategory;
import com.technos.moneyManagement.repository.entity.FinancialRecord;
import com.technos.moneyManagement.repository.entity.FinancialRecordType;
import com.technos.moneyManagement.repository.entity.IncomeCategory;
import com.technos.moneyManagement.service.FinancialRecordService;
import com.technos.moneyManagement.service.dto.MonthlyCategoryBreakdown;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/financial")
@RequiredArgsConstructor
public class FinancialController {

  private final FinancialRecordService financialRecordService;

  @GetMapping
  public String viewFinancialRecords(Model model) {
    List<FinancialRecordView> records =
        financialRecordService.getAllOrderedByRecordedAtDesc().stream().map(this::toView).toList();
    model.addAttribute("records", records);
    return "financial/records";
  }

  @GetMapping("/income/new")
  public String showIncomeForm(Model model) {
    ensureIncomeForm(model);
    model.addAttribute("incomeCategories", IncomeCategory.values());
    return "financial/income-form";
  }

  @PostMapping("/income")
  public String submitIncome(
      @Valid @ModelAttribute("incomeForm") IncomeForm incomeForm,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("incomeCategories", IncomeCategory.values());
      return "financial/income-form";
    }

    financialRecordService.registerIncome(
        incomeForm.getRecordedAt(),
        incomeForm.getAmount(),
        incomeForm.getNote(),
        incomeForm.getCategory());

    redirectAttributes.addFlashAttribute("successMessage", "収入を登録しました。");
    return "redirect:/financial";
  }

  @GetMapping("/expense/new")
  public String showExpenseForm(Model model) {
    ensureExpenseForm(model);
    model.addAttribute("expenseCategories", ExpenseCategory.values());
    return "financial/expense-form";
  }

  @PostMapping("/expense")
  public String submitExpense(
      @Valid @ModelAttribute("expenseForm") ExpenseForm expenseForm,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("expenseCategories", ExpenseCategory.values());
      return "financial/expense-form";
    }

    financialRecordService.registerExpense(
        expenseForm.getRecordedAt(),
        expenseForm.getAmount(),
        expenseForm.getNote(),
        expenseForm.getCategory());

    redirectAttributes.addFlashAttribute("successMessage", "支出を登録しました。");
    return "redirect:/financial";
  }

  private void ensureIncomeForm(Model model) {
    if (!model.containsAttribute("incomeForm")) {
      model.addAttribute("incomeForm", new IncomeForm());
    }
  }

  private void ensureExpenseForm(Model model) {
    if (!model.containsAttribute("expenseForm")) {
      model.addAttribute("expenseForm", new ExpenseForm());
    }
  }

  private FinancialRecordView toView(FinancialRecord record) {
    return new FinancialRecordView(
        record.getId(),
        record.getRecordedAt(),
        record.getRecordType(),
        record.getAmount(),
        record.getNote());
  }

  @GetMapping("/overview")
  public String showOverview(Model model) {
    MonthlyCategoryBreakdown breakdown = financialRecordService.getCurrentMonthCategoryBreakdown();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");

    List<Map.Entry<IncomeCategory, BigDecimal>> incomeEntries =
        sortEntriesByValueDesc(breakdown.incomeTotals());
    List<Map.Entry<ExpenseCategory, BigDecimal>> expenseEntries =
        sortEntriesByValueDesc(breakdown.expenseTotals());

    List<String> incomeLabels =
        incomeEntries.stream()
            .map(entry -> formatCategoryLabel(entry.getKey().getIcon(), entry.getKey().getLabel()))
            .collect(Collectors.toList());
    List<BigDecimal> incomeValues =
        incomeEntries.stream().map(Map.Entry::getValue).collect(Collectors.toList());

    List<String> expenseLabels =
        expenseEntries.stream()
            .map(entry -> formatCategoryLabel(entry.getKey().getIcon(), entry.getKey().getLabel()))
            .collect(Collectors.toList());
    List<BigDecimal> expenseValues =
        expenseEntries.stream().map(Map.Entry::getValue).collect(Collectors.toList());

    model.addAttribute("monthLabel", breakdown.month().format(formatter));
    model.addAttribute("incomeTotal", breakdown.totalIncome());
    model.addAttribute("expenseTotal", breakdown.totalExpense());
    model.addAttribute("netBalance", breakdown.totalIncome().subtract(breakdown.totalExpense()));
    model.addAttribute("incomeChartLabels", incomeLabels);
    model.addAttribute("incomeChartValues", incomeValues);
    model.addAttribute("expenseChartLabels", expenseLabels);
    model.addAttribute("expenseChartValues", expenseValues);
    return "financial/overview";
  }

  @GetMapping("/calendar")
  public String showCalendar(Model model) {
    YearMonth currentMonth = YearMonth.now();
    List<FinancialRecordView> monthlyViews =
        financialRecordService.getRecordsForMonth(currentMonth).stream().map(this::toView).toList();
    Map<LocalDate, List<FinancialRecordView>> recordsByDay =
        monthlyViews.stream().collect(Collectors.groupingBy(FinancialRecordView::recordedAt));

    LocalDate firstDay = currentMonth.atDay(1);
    LocalDate lastDay = currentMonth.atEndOfMonth();
    LocalDate calendarStart = alignBackwardTo(firstDay, DayOfWeek.SUNDAY);
    LocalDate calendarEnd = alignForwardTo(lastDay, DayOfWeek.SATURDAY);

    List<List<CalendarDayView>> weeks = new ArrayList<>();
    List<CalendarDayView> currentWeek = new ArrayList<>();
    LocalDate cursor = calendarStart;
    while (!cursor.isAfter(calendarEnd)) {
      List<FinancialRecordView> dayRecords = recordsByDay.getOrDefault(cursor, List.of());
      String weekdayLabel = cursor.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
      CalendarDayView dayView =
          new CalendarDayView(
              cursor,
              YearMonth.from(cursor).equals(currentMonth),
              weekdayLabel,
              dayRecords,
              sumByType(dayRecords, FinancialRecordType.INCOME),
              sumByType(dayRecords, FinancialRecordType.EXPENSE));
      currentWeek.add(dayView);
      if (currentWeek.size() == 7) {
        weeks.add(currentWeek);
        currentWeek = new ArrayList<>();
      }
      cursor = cursor.plusDays(1);
    }

    BigDecimal monthIncomeTotal = sumByType(monthlyViews, FinancialRecordType.INCOME);
    BigDecimal monthExpenseTotal = sumByType(monthlyViews, FinancialRecordType.EXPENSE);

    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");
    model.addAttribute("monthLabel", currentMonth.format(monthFormatter));
    model.addAttribute("weeks", weeks);
    model.addAttribute("weekdays", List.of("日", "月", "火", "水", "木", "金", "土"));
    model.addAttribute("today", LocalDate.now());
    model.addAttribute("monthIncomeTotal", monthIncomeTotal);
    model.addAttribute("monthExpenseTotal", monthExpenseTotal);
    model.addAttribute("monthNet", monthIncomeTotal.subtract(monthExpenseTotal));
    return "financial/calendar";
  }

  private <T> List<Map.Entry<T, BigDecimal>> sortEntriesByValueDesc(Map<T, BigDecimal> source) {
    return source.entrySet().stream()
        .sorted(Map.Entry.<T, BigDecimal>comparingByValue(Comparator.reverseOrder()))
        .toList();
  }

  private String formatCategoryLabel(String icon, String label) {
    return icon + " " + label;
  }

  private LocalDate alignBackwardTo(LocalDate date, DayOfWeek dayOfWeek) {
    LocalDate cursor = date;
    while (cursor.getDayOfWeek() != dayOfWeek) {
      cursor = cursor.minusDays(1);
    }
    return cursor;
  }

  private LocalDate alignForwardTo(LocalDate date, DayOfWeek dayOfWeek) {
    LocalDate cursor = date;
    while (cursor.getDayOfWeek() != dayOfWeek) {
      cursor = cursor.plusDays(1);
    }
    return cursor;
  }

  private BigDecimal sumByType(List<FinancialRecordView> records, FinancialRecordType targetType) {
    return records.stream()
        .filter(record -> record.recordType() == targetType)
        .map(FinancialRecordView::amount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
