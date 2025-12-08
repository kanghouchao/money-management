package com.technos.moneyManagement.repository.entity;

public enum ExpenseCategory {
  FOOD("食費", "\uD83C\uDF7D\uFE0F"),
  DAILY_NECESSITIES("日用品", "\uD83D\uDCBA"),
  CLOTHING("衣服", "\uD83D\uDC55"),
  BEAUTY("美容", "\uD83D\uDC84"),
  ENTERTAINMENT("娯楽", "\uD83C\uDFAE"),
  MEDICAL("医療", "\uD83D\uDC8A"),
  EDUCATION("教育", "\uD83D\uDCDA"),
  UTILITIES("光熱費", "\uD83D\uDCA1"),
  TRANSPORTATION("交通", "\uD83D\uDE8C"),
  COMMUNICATION("通信", "\uD83D\uDCF1"),
  HOUSING("住居", "\uD83C\uDFE0");

  private final String label;
  private final String icon;

  ExpenseCategory(String label, String icon) {
    this.label = label;
    this.icon = icon;
  }

  public String getLabel() {
    return label;
  }

  public String getIcon() {
    return icon;
  }
}
