package com.technos.moneyManagement.controller;

import com.technos.moneyManagement.repository.entity.FinancialRecord;
import com.technos.moneyManagement.service.FinancialRecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financial-records")
@RequiredArgsConstructor
public class FinancialRecordController {

  private final FinancialRecordService financialRecordService;

  @GetMapping
  public List<FinancialRecord> listAllOrderedByRecordedAtDesc() {
    return financialRecordService.getAllOrderedByRecordedAtDesc();
  }
}
