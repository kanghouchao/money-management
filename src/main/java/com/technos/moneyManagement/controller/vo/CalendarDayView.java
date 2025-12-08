package com.technos.moneyManagement.controller.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CalendarDayView(
    LocalDate date,
    boolean inCurrentMonth,
    String weekdayLabel,
    List<FinancialRecordView> records,
    BigDecimal incomeTotal,
    BigDecimal expenseTotal) {

  public BigDecimal netTotal() {
    return incomeTotal.subtract(expenseTotal);
  }

  public int dayOfMonth() {
    return date.getDayOfMonth();
  }

  public boolean hasIncome() {
    return incomeTotal.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean hasExpense() {
    return expenseTotal.compareTo(BigDecimal.ZERO) > 0;
  }
}
