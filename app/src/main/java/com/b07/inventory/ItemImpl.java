package com.b07.inventory;

import java.math.BigDecimal;

public class ItemImpl implements Item {
 
  private static final long serialVersionUID = -5829418623470240907L;
  private int id;
  private String name;
  private BigDecimal price;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;

  }

  @Override
  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public void setPrice(BigDecimal price) {
    this.price = price;

  }

}
