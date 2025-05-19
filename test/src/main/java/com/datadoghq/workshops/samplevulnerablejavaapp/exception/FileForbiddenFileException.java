package com.datadoghq.workshops.samplevulnerablejavaapp.exception;

public class FileForbiddenFileException extends Exception {
    public FileForbiddenFileException(String message) {
        super(message);
    }
}

