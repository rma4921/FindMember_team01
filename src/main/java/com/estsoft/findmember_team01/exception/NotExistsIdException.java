package com.estsoft.findmember_team01.exception;

public class NotExistsIdException extends RuntimeException {

    public NotExistsIdException(Long id) {
        super("not exists id: " + id);
    }
}