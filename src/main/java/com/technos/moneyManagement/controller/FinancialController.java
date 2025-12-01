package com.technos.moneyManagement.controller;

import com.technos.moneyManagement.repository.entity.FinancialRecord;
import com.technos.moneyManagement.service.FinancialRecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/financial")
@RequiredArgsConstructor
public class FinancialController {

  private final FinancialRecordService financialRecordService;

  @GetMapping
  public String viewFinancialRecords(Model model) {
    List<FinancialRecord> records = financialRecordService.getAllOrderedByRecordedAtDesc();
    model.addAttribute("records", records);
    return "financial/records";
  }
}
