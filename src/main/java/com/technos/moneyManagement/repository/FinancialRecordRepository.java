package com.technos.moneyManagement.repository;

import com.technos.moneyManagement.repository.entity.FinancialRecord;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Integer> {

  List<FinancialRecord> findAllByOrderByRecordedAtDesc();

  List<FinancialRecord> findAllByRecordedAtBetweenOrderByRecordedAtAsc(
      LocalDate startInclusive, LocalDate endInclusive);
}
