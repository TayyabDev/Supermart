package com.b07.inventory;

import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InventoryFullException;
import java.io.Serializable;
import java.util.HashMap;

public interface Inventory extends Serializable {

  public HashMap<Item, Integer> getItemMap();

  public void setItemMap(HashMap<Item, Integer> itemMap);

  public void updateItemMap(Item item, Integer value) throws InventoryFullException;

  public int getTotalItems();

  public void setTotalItems(int total) throws InvalidQuantityException;

}
