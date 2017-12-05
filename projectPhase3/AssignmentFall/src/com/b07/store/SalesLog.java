package com.b07.store;

import java.io.Serializable;
import java.util.List;

public interface SalesLog extends Serializable {

  public List<Sale> getSales();

  public void setSales(List<Sale> sales);

  public void addSale(Sale sale);

}
