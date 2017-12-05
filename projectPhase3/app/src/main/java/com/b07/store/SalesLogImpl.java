package com.b07.store;

import java.util.ArrayList;
import java.util.List;

public class SalesLogImpl implements SalesLog {

  private static final long serialVersionUID = -1495641776665974524L;
  private List<Sale> sales = new ArrayList<>();

  @Override
  public List<Sale> getSales() {
    return sales;
  }

  @Override
  public void setSales(List<Sale> sales) {
    this.sales = sales;
  }

  @Override
  public void addSale(Sale sale) {
    // go through the list and check if the itemized sale is already there
    boolean found = false;
    for (Sale x : sales) {
      if (x.getId() == sale.getId()) {
        found = true;
      }
    }
    // if the itemized sale was not already found then add it
    if (!found) {
      sales.add(sale);
    }
  }
}
