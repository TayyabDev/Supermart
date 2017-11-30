package com.b07.exceptions;

public class InvalidRoleException extends Exception {

  /**
   * SerialID for invalid role exception.
   */
  private static final long serialVersionUID = 5371857656165510690L;

  public InvalidRoleException(String string) {
    super(string);
  }

  public InvalidRoleException() {
    super();
  }
}
