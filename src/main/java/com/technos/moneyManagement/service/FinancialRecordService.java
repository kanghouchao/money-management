package com.technos.moneyManagement.service;

import com.technos.moneyManagement.repository.FinancialRecordRepository;
import com.technos.moneyManagement.repository.entity.Expense;
import com.technos.moneyManagement.repository.entity.ExpenseCategory;
import com.technos.moneyManagement.repository.entity.FinancialRecord;
import com.technos.moneyManagement.repository.entity.Income;
import com.technos.moneyManagement.repository.entity.IncomeCategory;
import com.technos.moneyManagement.service.dto.MonthlyCategoryBreakdown;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

  private final FinancialRecordRepository financialRecordRepository;

  public List<FinancialRecord> getAllOrderedByRecordedAtDesc() {
    return financialRecordRepository.findAllByOrderByRecordedAtDesc();
  }

  public Income registerIncome(
      LocalDate recordedAt, BigDecimal amount, String note, IncomeCategory category) {
    LocalDate resolvedRecordedAt = recordedAt != null ? recordedAt : LocalDate.now();
    Income income =
        Income.builder()
            .recordedAt(resolvedRecordedAt)
            .amount(amount)
            .note(note)
            .category(category)
            .build();
    return financialRecordRepository.save(income);
  }

  public Expense registerExpense(
      LocalDate recordedAt, BigDecimal amount, String note, ExpenseCategory category) {
    LocalDate resolvedRecordedAt = recordedAt != null ? recordedAt : LocalDate.now();
    Expense expense =
        Expense.builder()
            .recordedAt(resolvedRecordedAt)
            .amount(amount)
            .note(note)
            .category(category)
            .build();
    return financialRecordRepository.save(expense);
  }

  public MonthlyCategoryBreakdown getCurrentMonthCategoryBreakdown() {
    List<FinancialRecord> records = financialRecordRepository.findAll();
    YearMonth currentMonth = YearMonth.now();
    Map<IncomeCategory, BigDecimal> incomeTotals = new EnumMap<>(IncomeCategory.class);
    Map<ExpenseCategory, BigDecimal> expenseTotals = new EnumMap<>(ExpenseCategory.class);

    for (FinancialRecord record : records) {
      LocalDate recordedAt = record.getRecordedAt();
      if (recordedAt == null || !YearMonth.from(recordedAt).equals(currentMonth)) {
        continue;
      }

      if (record instanceof Income income) {
        incomeTotals.merge(income.getCategory(), income.getAmount(), BigDecimal::add);
      } else if (record instanceof Expense expense) {
        expenseTotals.merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
      }
    }

    return new MonthlyCategoryBreakdown(currentMonth, incomeTotals, expenseTotals);
  }

  public List<FinancialRecord> getRecordsForMonth(YearMonth month) {
    YearMonth targetMonth = month != null ? month : YearMonth.now();
    LocalDate start = targetMonth.atDay(1);
    LocalDate end = targetMonth.atEndOfMonth();
    return financialRecordRepository.findAllByRecordedAtBetweenOrderByRecordedAtAsc(start, end);
  }
}
