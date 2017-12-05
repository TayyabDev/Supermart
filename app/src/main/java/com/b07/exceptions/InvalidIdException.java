package com.b07.exceptions;

public class InvalidIdException extends Exception {

  /**
   * serial ID for invalid id's.
   */
  private static final long serialVersionUID = -3820260170158079638L;

  public InvalidIdException(String string) {
    super(string);
  }

}
