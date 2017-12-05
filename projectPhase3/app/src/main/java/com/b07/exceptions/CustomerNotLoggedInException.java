package com.b07.exceptions;

public class CustomerNotLoggedInException extends Exception {

  /**
   * SerialID for customer not logged in exception.
   */
  private static final long serialVersionUID = -6165443955639677977L;

  public CustomerNotLoggedInException(String string) {
    super(string);
  }

}
