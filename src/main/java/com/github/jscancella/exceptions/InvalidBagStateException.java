package com.github.jscancella.exceptions;

public class InvalidBagStateException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidBagStateException(final String message) {
    super(message);
  }
}
