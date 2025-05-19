package com.datadoghq.workshops.samplevulnerablejavaapp.exception;

public class InvalidDomainException extends DomainTestException {
  public InvalidDomainException(String message) {
    super(message);
  }
}
