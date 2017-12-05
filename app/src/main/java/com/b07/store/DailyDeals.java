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
    // put in the days of the week
    HashMap<Item, BigDecimal> itemMap = new HashMap<>();
    dayToDiscount.put("Monday", itemMap);
    dayToDiscount.put("Tuesday", itemMap);
    dayToDiscount.put("Wednesday", itemMap);
    dayToDiscount.put("Thursday", itemMap);
    dayToDiscount.put("Friday", itemMap);
    dayToDiscount.put("Saturday", itemMap);
    dayToDiscount.put("Sunday", itemMap);
  }


  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
    return dayToDiscount;
  }


  public void setDiscount(String day, HashMap<Item, BigDecimal> itemMap) {
    dayToDiscount.replace(day, itemMap);
  }
}



