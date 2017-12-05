
package com.b07.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

  private static final long serialVersionUID = 2279704311689629160L;
  private int id;
  private List<Integer> itemIdList = new ArrayList<>();
  private List<Integer> quantityList = new ArrayList<>();

  /**
   * Constructor of account.
   * 
   * @param id of the account
   * @param itemIdList of items stored in this account
   * @param quantityList stored in this account
   */
  public Account(int id, List<Integer> itemIdList, List<Integer> quantityList) {
    this.id = id;
    this.itemIdList = itemIdList;
    this.quantityList = quantityList;
  }

  public int getId() {
    return id;
  }

  public List<Integer> getItemIds() {
    return itemIdList;
  }

  public List<Integer> getItemQuantities() {
    return quantityList;
  }

}
