package com.b07.store;

import java.math.BigDecimal;
import java.util.HashMap;
import com.b07.inventory.Item;

public class DailyDeals {
    private HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount;

    public DailyDeals(HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount){
        this.dayToDiscount = new HashMap<>();
    }
    public DailyDeals(){
        this.dayToDiscount = new HashMap<>();
    }


  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
        return dayToDiscount;
      }
      
  }


