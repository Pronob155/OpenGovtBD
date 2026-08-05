package com.opengovtbd.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleIllegalState(IllegalStateException ex) {
        ModelAndView mav = new ModelAndView("redirect:/login");
        return mav;
    }

    @ExceptionHandler(java.util.NoSuchElementException.class)
    public String handleNotFound(Model model) {
        return "error/404";
    }
}
