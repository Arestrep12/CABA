package com.example.caba.web.controller;

import com.example.caba.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("resumen", dashboardService.obtenerResumen());
        return "dashboard/index";
    }
}

