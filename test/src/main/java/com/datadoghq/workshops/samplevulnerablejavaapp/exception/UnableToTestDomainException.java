package com.datadoghq.workshops.samplevulnerablejavaapp.exception;

public class UnableToTestDomainException extends DomainTestException {
  public UnableToTestDomainException(String message) {
    super(message);
  }
}
