package com.b07.exceptions;

public class InvalidQuantityException extends Exception {

  /**
   * SerialID for invalid quantity exception.
   */
  private static final long serialVersionUID = -4343542123357802001L;

  public InvalidQuantityException(String string) {
    super(string);
  }

}
