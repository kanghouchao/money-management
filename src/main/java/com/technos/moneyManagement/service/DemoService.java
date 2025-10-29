package com.technos.moneyManagement.service;

import com.technos.moneyManagement.repository.DemoRepository;
import com.technos.moneyManagement.repository.entity.Demo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {
  private final DemoRepository demoRepository;

  public Demo getDemoById(int id) {
    return demoRepository.findById(id).orElse(null);
  }
}
