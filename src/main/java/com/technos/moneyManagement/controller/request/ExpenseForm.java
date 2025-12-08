package com.technos.moneyManagement.controller.request;

import com.technos.moneyManagement.repository.entity.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ExpenseForm {

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate recordedAt = LocalDate.now();

  @NotNull
  @DecimalMin(value = "0.01")
  private BigDecimal amount;

  @Size(max = 512)
  private String note;

  @NotNull private ExpenseCategory category;
}
