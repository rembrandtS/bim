package com.devo.bim.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController extends AbstractController {

    @GetMapping("/index")
    public String index(Model model) {
        return "main/index";
    }

    @GetMapping("/layout")
    public String layout() {
        return "layout/default";
    }
}
