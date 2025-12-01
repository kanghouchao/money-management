package com.technos.moneyManagement.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("EXPENSE")
public class Expense extends FinancialRecord {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 64)
  private ExpenseCategory category;

  @Override
  protected FinancialRecordType defaultRecordType() {
    return FinancialRecordType.EXPENSE;
  }
}
