package com.technos.moneyManagement.repository.entity;

public enum FinancialRecordType {
  INCOME("収入"),
  EXPENSE("支出");

  private final String label;

  FinancialRecordType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
