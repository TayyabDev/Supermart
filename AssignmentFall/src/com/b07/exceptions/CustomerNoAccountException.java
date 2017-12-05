package com.b07.exceptions;

public class CustomerNoAccountException extends Exception {

  /** Exception called when customer does not have account and tries restoring cart.
   * 
   */
  private static final long serialVersionUID = -5734261812192213096L;

  public CustomerNoAccountException(String message) {
    super(message);
  }
}
