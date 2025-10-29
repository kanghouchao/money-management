package com.technos.moneyManagement.controller;

import com.technos.moneyManagement.repository.entity.Demo;
import com.technos.moneyManagement.service.DemoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class Index {

  private final DemoService demoService;

  @GetMapping("/")
  public String home(Model model) {
    List<Demo> demos = demoService.getAllDemos();
    model.addAttribute("demos", demos);
    return "index";
  }

  @PostMapping("/")
  public String addDemo(@ModelAttribute Demo demo) {
    if (ObjectUtils.isEmpty(demo) || !StringUtils.hasText(demo.getName())) {
      return "error/400";
    }
    this.demoService.save(demo);
    return "redirect:/";
  }
}
