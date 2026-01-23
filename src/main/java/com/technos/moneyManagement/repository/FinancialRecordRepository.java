package com.technos.moneyManagement.repository;

import com.technos.moneyManagement.repository.entity.FinancialRecord;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Integer> {

  Page<FinancialRecord> findAllByOrderByRecordedAtDescIdDesc(Pageable pageable);

  List<FinancialRecord> findAllByRecordedAtBetweenOrderByRecordedAtAsc(
      LocalDate startInclusive, LocalDate endInclusive);
}
