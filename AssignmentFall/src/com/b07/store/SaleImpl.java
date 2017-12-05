package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.math.BigDecimal;
import java.util.HashMap;

public class SaleImpl implements Sale {

  private static final long serialVersionUID = -5868303827035303319L;
  private int id;
  private User user;
  private transient BigDecimal price;
  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setUser(User user) {
    this.user = user;

  }

  @Override
  public BigDecimal getTotalPrice() {
    return price;
  }

  @Override
  public void setTotalPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public HashMap<Item, Integer> getItemMap() {
    return itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }

}
