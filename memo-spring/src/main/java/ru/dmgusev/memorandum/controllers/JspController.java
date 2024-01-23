package ru.dmgusev.memorandum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JspController {

    @GetMapping("/jsp")
    public String jsp(Model model) {
        model.addAttribute("name", "John");
        return "hello";
    }
}
