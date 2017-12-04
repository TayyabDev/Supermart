package com.b07.database.helper.android;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.DatabaseSelector;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.enumerators.Roles;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.InventoryFullException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.ItemizedSaleImpl;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Account;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidSelectHelper extends DatabaseDriverAndroid {
    public DatabaseAndroidSelectHelper(Context context) {
        super(context);
    }

    /**
     * get a User given the Id.
     * @param userId the Id of the user
     * @return the user that had the Id
     * @throws InvalidRoleException if the role is invalid
     * @throws InvalidIdException if the Id is invalid
     */
    public User getUser(int userId) throws InvalidRoleException, InvalidIdException {
        User user = null;
        Cursor c = super.getUserDetails(userId);
        int id = -1;
        String name = "";
        int age = -1;
        String address = "";

        // get user info
        while(c.moveToNext()){
            id = c.getInt(c.getColumnIndex("ID"));
            name = c.getString(c.getColumnIndex("NAME"));
            age =  c.getInt(c.getColumnIndex("AGE"));
            address = c.getString(c.getColumnIndex("ADDRESS"));
        }
        c.close();
        // get users role id
        if(id > 0){
            int roleId = getUserRole(userId);
            // construct user and return
            if(roleId > 0){
                user = createUser(id, name, age, address, roleId);
            } else {
                throw new InvalidRoleException("User is not registered in database as any role.");
            }
        } else {
            throw new InvalidIdException("User is not in database");
        }


        return user;

    }

    /**
     * Create a user.
     * @param id id of the user
     * @param name name of the User
     * @param age age of the User
     * @param address address of the User
     * @param roleId Id of the role
     * @return return the newly created user
     */
    private User createUser(int id, String name, int age, String address, int roleId) {
        // initialize the user
        User user = null;
        // get the role name using the id
        String roleName = getRoleName(roleId);
        // if role is activity_admin create an activity_admin
        if (roleName.equals(Roles.ADMIN.toString())) {
            user = new Admin(id, name, age, address);
        } else if (roleName.equals(Roles.CUSTOMER.toString())) {
            user = new Customer(id, name, age, address);
        }
        return user;
    }

    /**
     * Get the name of the role.
     * @param roleId the Id of the role
     * @return the name of the role
     */
    public String getRoleName(int roleId) {
        return super.getRole(roleId);
    }

    /**
     * Get the role of a user.
     * @param userId the Id of the user
     * @return the role of the user
     */
    public int getUserRoleId(int userId) {
        return super.getUserRole(userId);
    }

    /**
     * Get a list of all Role Ids.
     * @return A list that holds all role Ids
     */
    public List<Integer> getRoleIdsHelper() {
        Cursor c = super.getRoles();
        List<Integer> roleIds = new ArrayList<>();

        while(c.moveToNext()){
            roleIds.add(c.getInt(c.getColumnIndex("ID")));
        }
        c.close();
        return roleIds;
    }

    /**
     * Get the password for a user.
     * @param userId the Id of the user
     * @return the user's password
     */
    public String getPasswordHelper(int userId) {return super.getPassword(userId);}

    /**
     * Get an from the database.
     * @param itemId the Id of the item
     * @return return the item, based on the Id
     * @throws InvalidIdException if the Id is invalid
     */
    public Item getItemHelper(int itemId) throws InvalidIdException {
        // intitialize item info
        int id = 0;
        String name = null;
        BigDecimal price = null;

        // get cursor and search for the item's info
        Cursor c = super.getItem(itemId);
        while(c.moveToNext()){
            id = c.getInt(c.getColumnIndex("ID"));
            name = c.getString(c.getColumnIndex("NAME"));
            price = new BigDecimal(c.getString(c.getColumnIndex("PRICE")));
        }
        c.close();

        // create item and return if item was found
        if(id > 0){
            Item item = new ItemImpl();
            item.setId(id);
            item.setPrice(price);
            item.setName(name);
            return item;
        } else {
            // item not in database so throw exception
            throw new InvalidIdException("Item not in database.");
        }

    }

    /**
     * Get a list of all the items in the store.
     * @return a list holding item objects that exist in the store
     */
    public List<Item> getAllItemsHelper() {
        Cursor c = super.getAllItems();
        // initialize list of items to return
        List<Item> items = new ArrayList<>();
        while (c.moveToNext()) {
            // get the information about the current item
            int id = c.getInt(c.getColumnIndex("ID"));
            String name = c.getString(c.getColumnIndex("NAME"));
            BigDecimal price = new BigDecimal(c.getString(c.getColumnIndex("PRICE")));

            // create the item
            Item item = new ItemImpl();
            item.setId(id);
            item.setName(name);
            item.setPrice(price);

            // add the current item to the list of items
            items.add(item);
        }
        // close cursor and return list of items
        c.close();
        return items;
    }

    /**
     * Get the quantity of the item in the store, given its id.
     * @param itemId the Id of the item
     * @return the quantity of the item that is in the store
     */
    public int getInventoryQuantity(int itemId) {
        // initialize quantity and search inventory for it
        int quantity = -1;
        List<Item> itemList = getAllItemsHelper();
        for (Item item : itemList) {
            if (item.getId() == itemId) {
                // connect to database and get the inventory quantity once we have found the item
                try{
                    quantity = super.getInventoryQuantity(itemId);
                    return quantity;
                } catch (Exception e){
                    //
                }


            }
        }
        // quantity of -1  will be returned if invalid item Id
        return quantity;
    }

    /**
     * Get the inventory of the database.
     * @return the inventory of the database
     */
    public Inventory getInventoryHelper() {
        // initialize an inventory
        Inventory inventory = new InventoryImpl();

        // get cursor
        Cursor c = super.getInventory();

        while (c.moveToNext()) {
            // update the item map with the current table data
            try {
                inventory.updateItemMap(getItemHelper(c.getInt(c.getColumnIndex("ITEMID"))),c.getInt(c.getColumnIndex("QUANTITY")));
            } catch (InventoryFullException e) {
                e.printStackTrace();
            } catch (InvalidIdException e) {
                e.printStackTrace();
            }

        }
        c.close();
        return inventory;
    }


    /**
     * Check if the userId exists in the database.
     * @param userId the Id of the User
     * @return True if the user exists in the database, false otherwise
     */
    private boolean checkUserId(int userId) {
        boolean check = false;
        Cursor c = super.getUserDetails(userId);
        while (c.moveToNext()) {
            check = ((c.getColumnIndex("ID")) == userId);
        }
        c.close();
        return check;
    }


    /**
     * Get a list of all users for a certain role.
     * @param roleId the Id of the role
     * @return a list of the all the users by a certain role
     */
    public List<Integer> getUsersByRoleHelper(int roleId){
        // get cursor and initialize the user id list
        Cursor c = super.getUsersByRole(roleId);
        List<Integer> userIdList = new ArrayList<>();

        // loop through cursor results
        while(c.moveToNext()){
            //  get the user id and it to list
            userIdList.add(c.getInt(c.getColumnIndex("ID")));
        }
        // close cursor and return list of user id's
        c.close();
        return userIdList;

    }


    /**
     * Get a list of all Users.
     * @return a List of all users in the application
     */
    public List<User> getUsersDetailsHelper() {
        Cursor c = super.getUsersDetails();
        List<User> users = new ArrayList<>();
        while (c.moveToNext()){
            int id = c.getInt(c.getColumnIndex("ID"));
            String name = c.getString(c.getColumnIndex("NAME"));
            int age =  c.getInt(c.getColumnIndex("AGE"));
            String address = c.getString(c.getColumnIndex("ADDRESS"));
            User newUser = createUser(id, name, age, address, getUserRoleId(id));
            users.add(newUser);
        }
        c.close();
        return users;
    }


    /**
     * Get all the sales that had occured.
     * @return a SalesLog with all sales
     * @throws InvalidIdException if there is an invalid Id
     * @throws InvalidRoleException if there is an invalid role
     */
    public SalesLog getSalesHelper() throws InvalidIdException, InvalidRoleException {
        Cursor c = super.getSales();
        SalesLog salesLog = new SalesLogImpl();
        List<Sale> saleList = new ArrayList<>();
        while (c.moveToNext()){
            int id = c.getInt(c.getColumnIndex("ID"));
            Sale sale = getSaleByIdHelper(id);
            salesLog.addSale(sale);
        }
        c.close();
        return salesLog;

    }


    /**
     * Get details of a sale.
     * @param saleId the Id of the sale
     * @return Sale based on the Id
     * @throws InvalidIdException if the Id is invalid
     * @throws InvalidRoleException if the Role is invalid
     */
    public Sale getSaleByIdHelper(int saleId) throws InvalidIdException, InvalidRoleException {
        Cursor c = super.getSaleById(saleId);
        Sale sale = new SaleImpl();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("ID"));
            int userId = c.getInt(c.getColumnIndex("USERID"));
            BigDecimal price = new BigDecimal((c.getString(c.getColumnIndex("TOTALPRICE"))));
            sale.setId(id);
            sale.setUser(getUser(userId));
            sale.setTotalPrice(price);
        }
        c.close();
        return sale;
    }


    /**
     * Return a List of sales based off a user's id.
     * @param userId the Id of the user
     * @return a list of sales from the user.
     * @throws InvalidIdException if there is an invalid Id
     * @throws InvalidRoleException if the role is invalid
     */
    public List<Sale> getSalesToUserHelper(int userId) throws InvalidIdException, InvalidRoleException {
        Cursor c = super.getSalesToUser(userId);
        List<Sale> sales = new ArrayList<>();
        Sale sale = new SaleImpl();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("ID"));
            int usersId = c.getInt(c.getColumnIndex("USERID"));
            BigDecimal price = new BigDecimal((c.getString(c.getColumnIndex("TOTALPRICE"))));
            sale.setId(id);
            sale.setUser(getUser(userId));
            sale.setTotalPrice(price);
            sales.add(sale);
        }
        c.close();
        return sales;
    }


    /**
     * Populate the sale object's item map with the items stored in the database corresponding to it's sale ID.
     * @param saleId the Id of the sale
     * @param sale the sale corresponding to the sale id
     * @throws InvalidIdException the Id is invalid
     */
    public void getItemizedSaleByIdHelper(int saleId, Sale sale) throws InvalidIdException {
        Cursor c = super.getItemizedSaleById(saleId);
        HashMap<Item, Integer> itemMap = new HashMap<>();
        while(c.moveToNext()){
            // get current item
            itemMap.put(getItemHelper(c.getInt(c.getColumnIndex("ITEMID"))), c.getInt(c.getColumnIndex("QUANTITY")));
            System.out.println(c.getInt(c.getColumnIndex("ITEMID")) + " " + c.getInt(c.getColumnIndex("QUANTITY")));
        }
        c.close();

        // set the item map to the sales item map
        sale.setItemMap(itemMap);

    }


    /**
     * Given a list of sales (saleslog), populate each sale in the saleslog's itemmap given their itemized sales.
     * @param salesLog a history of sales that occured
     * @throws InvalidIdException if the Id is invalid
     */
    public void getItemizedSalesHelper(SalesLog salesLog) throws InvalidIdException {
        for(Sale sale : salesLog.getSales()){
            getItemizedSaleByIdHelper(sale.getId(),sale);
        }
    }


    /**
     * get a list of all the User's accounts given an Id.
     * @param userId the Id of the user
     * @return a list of all the accounts that the user has
     */
    public List<Integer> getUserAccountsHelper(int userId)  {
        Cursor c = super.getUserAccounts(userId);
        List<Integer> accountIds = new ArrayList<>();
        while (c.moveToNext()) {
            accountIds.add(c.getInt(c.getColumnIndex("ID")));
        }
        c.close();
        return accountIds;
    }


    /**
     * get the details of the given account.
     * @param accountId the Id of the aaccount.
     * @return the Account holding information
     */
    public Account getAccountDetailsHelper(int accountId) {
        Cursor c = super.getAccountDetails(accountId);
        List<Integer> itemIdList = new ArrayList<Integer>();
        List<Integer> quantityList = new ArrayList<Integer>();
        while (c.moveToNext()) {
            itemIdList.add(c.getInt(c.getColumnIndex("ITEMID")));
            quantityList.add(c.getInt(c.getColumnIndex("QUANTITY")));
        }
        c.close();
        return new Account(accountId, itemIdList, quantityList);
    }

    /**
     * get a list of inactive accounts given an Id.
     * @param userId the Id of the user
     * @return A list holding the Ids of all active accounts based off the user's Id
     * @throws InvalidIdException if the Id is invalid
     */
    public List<Integer> getUserActiveAccountsHelper(int userId) throws InvalidIdException {
        // check if user id is valid
        List<User> userList  = getUsersDetailsHelper();
        for(User user : userList){
            if(user.getId() == userId){
                // get cursor with user's active accounts
                Cursor c = super.getUserActiveAccounts(userId);

                // construct new list and populate it with the user's accounts
                List<Integer> userActiveAccounts = new ArrayList<>();

                while(c.moveToNext()){
                    userActiveAccounts.add(c.getInt(c.getColumnIndex("ID")));
                }

                // close cursor and return
                c.close();
                return userActiveAccounts;

            }
        }
        throw new InvalidIdException("User ID is not valid!");
    }

    /**
     * Get a list of inaactive account given an Id
     * @param userId the Id of the user
     * @return A list holding all the Ids of inactive accounts based off the user's Id
     * @throws InvalidIdException If the Id is invalid
     */
    public List<Integer> getUserInactiveAccountsHelper(int userId) throws InvalidIdException {
        // check if user id is valid
        List<User> userList  = getUsersDetailsHelper();
        for(User user : userList){
            if(user.getId() == userId){
                // get cursor with user's active accounts
                Cursor c = super.getUserInactiveAccounts(userId);

                // construct new list and populate it with the user's accounts
                List<Integer> userInactiveAccounts = new ArrayList<>();

                while(c.moveToNext()){
                    userInactiveAccounts.add(c.getInt(c.getColumnIndex("ID")));
                }

                // close cursor and return
                c.close();
                return userInactiveAccounts;

            }
        }
        throw new InvalidIdException("User ID is not valid!");
    }
}

