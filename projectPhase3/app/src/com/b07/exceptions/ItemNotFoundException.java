package com.b07.exceptions;

public class ItemNotFoundException extends Exception {

  public ItemNotFoundException(String string) {
    super(string);
  }

  /**
   * SerialID for item not found exception.
   */
  private static final long serialVersionUID = -6000056757675603123L;

}
