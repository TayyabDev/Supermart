package com.b07.store;

import java.math.BigDecimal;
import java.util.HashMap;
import com.b07.inventory.Item;

public class DailyDeals {

  private HashMap<String, HashMap<Item, BigDecimal>> discount = new HashMap<String, HashMap<Item, BigDecimal>>();
      
  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
        return discount;
      }
      
  }

