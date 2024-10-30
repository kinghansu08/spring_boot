package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 예외를 처리
    public String handleException(Exception ex, RedirectAttributes redirectAttributes, Model model) {
        // 예외 메시지를 모델에 추가하거나 리다이렉트 속성에 추가 가능
        model.addAttribute("errorMessage", ex.getMessage());
        return "error_page/article_error"; // 오류 처리 페이지로 이동
    }
}