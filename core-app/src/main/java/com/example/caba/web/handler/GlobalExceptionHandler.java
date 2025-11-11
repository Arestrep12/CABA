package com.example.caba.web.handler;

import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "com.example.caba.web")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, HttpServletRequest request, Model model) {
        log.warn("Ruta {} produjo NotFoundException {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(BusinessRuleException.class)
    public String handleBusiness(BusinessRuleException ex, HttpServletRequest request, Model model) {
        log.warn("Regla de negocio violada en {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/business";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, HttpServletRequest request, Model model) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        model.addAttribute("error", "Ha ocurrido un error inesperado");
        return "error/500";
    }
}

