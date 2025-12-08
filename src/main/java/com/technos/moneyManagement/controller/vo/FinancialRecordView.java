package com.technos.moneyManagement.controller.vo;

import com.technos.moneyManagement.repository.entity.FinancialRecordType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecordView(
    Integer id,
    LocalDate recordedAt,
    FinancialRecordType recordType,
    BigDecimal amount,
    String note) {}
