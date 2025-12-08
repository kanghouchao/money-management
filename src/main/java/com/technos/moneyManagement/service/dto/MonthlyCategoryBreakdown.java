package com.technos.moneyManagement.service.dto;

import com.technos.moneyManagement.repository.entity.ExpenseCategory;
import com.technos.moneyManagement.repository.entity.IncomeCategory;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.Map;

public record MonthlyCategoryBreakdown(
    YearMonth month,
    Map<IncomeCategory, BigDecimal> incomeTotals,
    Map<ExpenseCategory, BigDecimal> expenseTotals) {

  public MonthlyCategoryBreakdown(YearMonth month) {
    this(month, new EnumMap<>(IncomeCategory.class), new EnumMap<>(ExpenseCategory.class));
  }

  public BigDecimal totalIncome() {
    return incomeTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal totalExpense() {
    return expenseTotals.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
