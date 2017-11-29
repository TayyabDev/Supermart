package com.b07.exceptions;

public class InvalidInputException extends Exception {

  /**
   * SerialID for invalid input exception.
   */
  private static final long serialVersionUID = -8725932331428458952L;

  public InvalidInputException(String string) {
    super(string);
  }

}
