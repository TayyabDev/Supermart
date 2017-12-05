package com.b07.store;

import android.content.Context;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.enumerators.Days;
import com.b07.inventory.Item;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DailyDeals {

  private HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount;

  public DailyDeals(HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount, Context context) {
    this.dayToDiscount = new HashMap<>();
  }

  public DailyDeals(Context context) {
    this.dayToDiscount = new HashMap<>();
    // put in the days of the week
    HashMap<Item, BigDecimal> itemMap = new HashMap<>();

    // get list of items
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);


    dayToDiscount.put("MONDAY", itemMap);
    dayToDiscount.put("TUESDAY", itemMap);
    dayToDiscount.put("WEDNESDAY", itemMap);
    dayToDiscount.put("THURSDAY", itemMap);
    dayToDiscount.put("FRIDAY", itemMap);
    dayToDiscount.put("SATURDAY", itemMap);
    dayToDiscount.put("SUNDAY", itemMap);
  }
  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
    return dayToDiscount;
  }
  public void setDiscount(String day, HashMap<Item, BigDecimal> itemMap) {
    dayToDiscount.replace(day, itemMap);
  }

  public HashMap<Item, BigDecimal> getTodaysDiscount(){
    // determine the day it is using the Days enum
    Calendar calendar = Calendar.getInstance();
    String currentDay = Days.getDay(calendar.get(Calendar.DAY_OF_WEEK)).toString();

    // return the item map at the current day
    return dayToDiscount.get(currentDay);
  }


}



