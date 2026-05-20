package com.david.nextplay.exception;

public class GameAlreadyExistsException extends RuntimeException {
    
    public GameAlreadyExistsException(String message) {
        super(message);
    }
}
