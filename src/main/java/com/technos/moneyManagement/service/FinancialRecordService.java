package com.technos.moneyManagement.service;

import com.technos.moneyManagement.repository.FinancialRecordRepository;
import com.technos.moneyManagement.repository.entity.FinancialRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

  private final FinancialRecordRepository financialRecordRepository;

  public List<FinancialRecord> getAllOrderedByRecordedAtDesc() {
    return financialRecordRepository.findAllByOrderByRecordedAtDesc();
  }
}
