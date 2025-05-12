package com.estsoft.findmember_team01.exception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorViewController {

    @GetMapping("/error")
    public String errorView(Model model, @RequestParam(required = false) String msg) {
        model.addAttribute("errorMessage", msg != null ? msg : "알 수 없는 오류 발생");

        return "error";
    }
}
