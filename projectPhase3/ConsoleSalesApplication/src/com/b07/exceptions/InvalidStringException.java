package com.b07.exceptions;

public class InvalidStringException extends Exception {

  public InvalidStringException(String string) {
    super(string);
  }

  public InvalidStringException() {
    super();
  }

  /**
   * serialID for invalid input exceptions.
   */

  private static final long serialVersionUID = 2L;

}
