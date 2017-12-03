package com.b07.database.helper.android;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.DatabaseInserter;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.enumerators.Roles;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidInsertHelper extends DatabaseDriverAndroid{
    public DatabaseAndroidInsertHelper(Context context) {
        super(context);
    }
    public long insertRole(String role){
        long roleId = -1;
        // check if role is valid
        if(role.equals(Roles.ADMIN.toString()) || role.equals(Roles.CUSTOMER.toString())){
            // insert role
            roleId = super.insertRole(role);
        }
        return roleId;
    }
    public long insertNewUser(String name, int age, String address, String password){
        long userId  = -1;
        // check if input parameters are valid
        if(name != null && age >= 0 && address != null && address.length() <= 100 && password != null && password.length() <= 64){
            // insert user into database
            userId =  super.insertNewUser(name, age, address, password);
        }
        return userId;
    }

    public long insertUserRole(int userId, int roleId){
        int userRoleId = -1;
        // insert user with the given role
        super.insertUserRole(userId, roleId);
        return userRoleId;
    }

    public long insertItem(String name, BigDecimal price, Context context){
        // insert item into database if price is >= 0, else return 0
        if(price.compareTo(new BigDecimal("0.00"))>= 0){
            // check if item is already in database
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
            List<Item> itemList = sel.getAllItemsHelper();

            // if item is already in database dont insert item and return 0 for item id
            for(Item item : itemList){
                if(item.getName().equals(name)){
                    return 0;
                }
            }

            // if item is not in database then insert it
            return super.insertItem(name, price);
        } else{
            // return 0 for the item id if the price is invalid.
            return 0;
        }

    }

    public long insertInventoryHelper(int itemId, int quantity, Context context) throws InvalidIdException, InvalidInputException {
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Item> items = sel.getAllItemsHelper();
        // if the price user input is a negative number, throw an invalid input
        // exception
        if (quantity >= 0) {
            // check if the itemId is already exist in our database
            for (Item item : items) {
                if (item.getId() == itemId) {
                    // insert item into inventory with quantity and return the id
                    return super.insertInventory(itemId, quantity);
                }
            }
            throw new InvalidIdException("The item is invalid.");
            // if the input is correct, use database function
        } else {
            throw new InvalidInputException("The quantity is invalid");
        }
    }

    public long insertSale(int userId, BigDecimal totalPrice) {
        BigDecimal leastPrice = new BigDecimal("0");
        long saleId = -1;
        // if totalPrice is >= 0 then proceed check if user id is valid
        if (totalPrice.compareTo(leastPrice) >= 0) {
            saleId = super.insertSale(userId, totalPrice);
        }
        return saleId;
    }
    public long insertItemizedSale(int saleId, int itemId, int quantity, Context context) throws DatabaseInsertException, SQLException, InvalidIdException, InvalidRoleException {
        long itemizedId = -1;
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        List<Item> itemsList = sel.getAllItemsHelper();
        SalesLog salesLog = sel.getSalesHelper();
        for (Item item : itemsList) {
            if (item.getId() == itemId) {
                for (Sale sale : salesLog.getSales()) {
                    if (sale.getId() == saleId) {
                        if (quantity >= 0 & quantity <= DatabaseSelectHelper.getInventoryQuantity(itemId)) {
                            itemizedId =
                                    super.insertItemizedSale(saleId, itemId, quantity);
                            return itemizedId;
                        }
                    }
                }
            }
        }
        throw new DatabaseInsertException("Check the value of sale/item/quantity");
    }

    public long insertAccount(int accountId, boolean active){
        // not made yet
        return 1;
    }


}