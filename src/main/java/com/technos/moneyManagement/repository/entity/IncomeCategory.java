package com.technos.moneyManagement.repository.entity;

public enum IncomeCategory {
  SALARY("給与", "\uD83D\uDCBC"),
  ALLOWANCE("手当", "\uD83C\uDF81"),
  BONUS("ボーナス", "\uD83C\uDF89"),
  SIDE_HUSTLE("副業", "\uD83D\uDEE0\uFE0F"),
  INVESTMENT("投資", "\uD83D\uDCC8"),
  TEMPORARY("臨時", "\u26A1\uFE0F");

  private final String label;
  private final String icon;

  IncomeCategory(String label, String icon) {
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
