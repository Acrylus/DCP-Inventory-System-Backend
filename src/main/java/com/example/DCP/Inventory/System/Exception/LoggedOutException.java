package com.example.DCP.Inventory.System.Exception;

public class LoggedOutException extends RuntimeException {
    public LoggedOutException(String message) {
        super(message);
    }
}
