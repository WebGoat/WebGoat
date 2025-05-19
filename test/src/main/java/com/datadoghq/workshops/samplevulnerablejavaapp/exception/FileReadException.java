package com.datadoghq.workshops.samplevulnerablejavaapp.exception;

public class FileReadException extends Exception {
    public FileReadException(String message) {
        super(message);
    }
}

