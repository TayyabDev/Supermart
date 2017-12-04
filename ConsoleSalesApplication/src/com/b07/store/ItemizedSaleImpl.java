package com.b07.store;

import com.b07.inventory.Item;
import java.util.HashMap;

public class ItemizedSaleImpl extends SaleImpl {
  private int saleId;


  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // only set it to item map if length is less than one
    if (itemMap.size() == 1) {
      super.setItemMap(itemMap);
    }
  }

  public void setSaleId(int saleId) {
    this.saleId = saleId;
  }

  public int getSaleId() {
    return this.saleId;
  }
}


