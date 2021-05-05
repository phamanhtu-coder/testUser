package com.example.football.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {

    @RequestMapping({ "/home" })
    public String home() {
        return "Welcome in Manchester United";
    }
}
