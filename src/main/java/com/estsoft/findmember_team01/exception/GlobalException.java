package com.estsoft.findmember_team01.exception;

import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final GlobalExceptionType type;

    public GlobalException(GlobalExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public HttpStatus getStatus() {
        return type.getStatus();
    }
}
