package com.b07.store;

import com.b07.inventory.Item;
import java.math.BigDecimal;
import java.util.HashMap;


public class DailyDeals {
  private HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount;

  public DailyDeals(HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount) {
    this.dayToDiscount = new HashMap<>();
  }

  public DailyDeals() {
    this.dayToDiscount = new HashMap<>();
  }

  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
    return dayToDiscount;
  }

}
