package com.spring.ivannikov.project2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StartController {
    @GetMapping()
    public String getStartPage(){
        return "start-page";
    }
}
