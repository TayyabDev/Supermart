package com.b07.store;

import com.b07.inventory.Item;
import java.util.HashMap;


public class ItemizedSaleImpl extends SaleImpl {

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // only set it to item map if length is less than one
    if (itemMap.size() == 1) {
      super.setItemMap(itemMap);
    }
  }
}


