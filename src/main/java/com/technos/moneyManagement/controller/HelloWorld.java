package com.technos.moneyManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** HelloWorld controller */
@Controller
@RequestMapping
public class HelloWorld {

  /** to hello-world thymeleaf page */
  @GetMapping("/hello")
  public String sayHello(@RequestParam String name, Model model) {
    // Add the name parameter to the model
    model.addAttribute("name", name);
    // This method will return the hello-world.html template
    return "hello-world";
  }
}
