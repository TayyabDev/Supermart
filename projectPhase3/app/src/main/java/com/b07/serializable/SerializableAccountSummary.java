package com.b07.serializable;

import java.io.Serializable;

public class SerializableAccountSummary implements Serializable {

  private static final long serialVersionUID = 5447379500756474879L;
  private int accountId;
  private int itemId;
  private int quantity;

  private SerializableAccountSummary(int accountid, int itemid, int quantity) {
    this.setaccountId(accountid);
    this.setitemId(itemid);
    this.setquantity(quantity);
  }

  private void setaccountId(int id) {
    this.accountId = id;
  }

  @SuppressWarnings("unused")
  private int getaccountId() {
    return this.accountId;
  }

  private void setitemId(int itemid) {
    this.itemId = itemid;
  }

  @SuppressWarnings("unused")
  private int getitemId() {
    return this.itemId;
  }

  private void setquantity(int quantity) {
    this.quantity = quantity;
  }

  @SuppressWarnings("unused")
  private int getquantity() {
    return this.quantity;
  }
}
