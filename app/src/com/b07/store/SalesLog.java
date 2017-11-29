package com.b07.store;

import java.util.List;

public interface SalesLog {

  public List<Sale> getSales();


  public void setSales(List<Sale> sales);


  public void addSale(Sale sale);
}
