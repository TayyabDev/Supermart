package com.b07.exceptions;

public class InvalidStringException extends Exception {

  /**
   * serialID for invalid input exceptions.
   */

  private static final long serialVersionUID = 2L;

  public InvalidStringException(String string) {
    super(string);
  }

  public InvalidStringException() {
    super();
  }

}
