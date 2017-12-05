package com.b07.store;

import android.content.Context;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.enumerators.Days;
import com.b07.inventory.Item;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DailyDeals {

  private HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount;

  /**
   * Initialize a daily deals with your own deals.
   * @param dayToDiscount the day to item map discount hashmap.
   * @param context the current state of the application.
   */
  public DailyDeals(HashMap<String, HashMap<Item, BigDecimal>> dayToDiscount, Context context) {
    this.dayToDiscount = new HashMap<>();
  }

  /**
   * Initialize a daily deal with default items.
   * @param context the current state of the application.
   */
  public DailyDeals(Context context) {
    this.dayToDiscount = new HashMap<>();
    // put in the days of the week
    HashMap<Item, BigDecimal> itemMap;

    // get list of items
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<Item> items = sel.getAllItemsHelper();

    for(Item item : items){
      switch (item.getName()){
        case "FISHING_ROD":
          itemMap = new HashMap<>();
          itemMap.put(item, new BigDecimal("0.10"));
          dayToDiscount.put("MONDAY", itemMap);
          dayToDiscount.put("SUNDAY", itemMap);
          break;
        case "SKATES":
          itemMap = new HashMap<>();
          itemMap.put(item, new BigDecimal("0.20"));
          dayToDiscount.put("TUESDAY", itemMap);
          dayToDiscount.put("SATURDAY", itemMap);
          break;
        case "PROTEIN_BAR":
          itemMap = new HashMap<>();
          itemMap.put(item, new BigDecimal("0.30"));
          dayToDiscount.put("WEDNESDAY", itemMap);
          break;
        case "HOCKEY_STICK":
          itemMap = new HashMap<>();
          itemMap.put(item, new BigDecimal("0.40"));
          dayToDiscount.put("THURSDAY", itemMap);
          break;
        case "RUNNING_SHOES":
          itemMap = new HashMap<>();
          itemMap.put(item, new BigDecimal("0.45"));
          dayToDiscount.put("FRIDAY", itemMap);
          break;
      }
    }

  }

  /**
   * Get the daily deal item map.
   * @return the daily deal item map.
   */
  public HashMap<String, HashMap<Item, BigDecimal>> getItemMap() {
    return dayToDiscount;
  }

  /**
   * Set the discount for the given day.
   * @param day a day of the week.
   * @param itemMap the discount item map.
   */
  public void setDiscount(String day, HashMap<Item, BigDecimal> itemMap) {
    dayToDiscount.replace(day, itemMap);
  }

  /**
   * Get today's deal.
   * @return today's deal.
   */
  public HashMap<Item, BigDecimal> getTodaysDiscount(){
    // determine the day it is using the Days enum
    Calendar calendar = Calendar.getInstance();
    String currentDay = Days.getDay(calendar.get(Calendar.DAY_OF_WEEK)).toString();
    System.out.println(currentDay);

    // return the item map at the current day
    return dayToDiscount.get(currentDay);
  }


}



