package com.devo.bim.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/index")
    public String index() {
        return "main/index";
    }

    @GetMapping("/layout")
    public String layout() {
        return "layout/default";
    }
}
