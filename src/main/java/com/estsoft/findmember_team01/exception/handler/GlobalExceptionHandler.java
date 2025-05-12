package com.estsoft.findmember_team01.exception.handler;

import com.estsoft.findmember_team01.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Object handlerGlobalException(GlobalException e,
        HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getType().name());
            error.put("message", e.getMessage());

            return ResponseEntity.status(e.getStatus()).body(error);
        } else {
            return new ModelAndView(
                "redirect:/error?msg=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    @ExceptionHandler(Exception.class)
    public Object handlerOtherException(Exception e, HttpServletRequest request) {
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "INTERNAL_ERROR");
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } else {
            return new ModelAndView(
                "redirect:/error?msg=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
