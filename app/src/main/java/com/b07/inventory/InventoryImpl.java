package com.b07.inventory;

import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InventoryFullException;
import java.util.HashMap;


public class InventoryImpl implements Inventory {
  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
  private int total;
  private boolean totalSet = false;

  @Override
  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;

  }

  @Override
  public void updateItemMap(Item item, Integer value) throws InventoryFullException {
    // if we set the total items already then check if inventory is full
    if (totalSet) {
      if (itemMap.size() < total) {
        // put item into inventory
        itemMap.put(item, value);
      } else {
        // if inventory full throw exception
        throw new InventoryFullException();
      }
    } else {
      // if we are not setting a limit on inventory
      itemMap.put(item, value);
    }

  }

  @Override
  public int getTotalItems() {
    // return the number of items in the inventory
    return itemMap.size();
  }

  @Override
  public void setTotalItems(int total) throws InvalidQuantityException {
    // check if total is valid
    if (total >= itemMap.size()) {
      this.total = total;
      totalSet = true;
    } else {
      throw new InvalidQuantityException("The total must be greater than current inventory size");
    }


  }

}
