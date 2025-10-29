package com.technos.moneyManagement.controller;

import com.technos.moneyManagement.repository.entity.Demo;
import com.technos.moneyManagement.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class Index {

  private final DemoService demoService;

  @GetMapping("/")
  public String home(@RequestParam(required = false, defaultValue = "0") int id, Model model) {
    Demo demo = demoService.getDemoById(id);
    model.addAttribute("demo", demo);
    return "index";
  }
}
