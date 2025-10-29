package com.technos.moneyManagement.service;

import com.technos.moneyManagement.repository.DemoRepository;
import com.technos.moneyManagement.repository.entity.Demo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {
  private final DemoRepository demoRepository;

  public List<Demo> getAllDemos() {
    return demoRepository.findAll();
  }

  public void save(Demo demo) {
    demoRepository.save(demo);
  }
}
