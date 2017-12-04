package com.b07.store;

import android.content.Context;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Account;
import com.b07.users.Admin;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminInterface {
  private Admin currentAdmin;
  @SuppressWarnings("unused")
  private Inventory inventory;

  /**
   * A public constructor for AdminInterface.
   * 
   * @param inventory inventory object.
   */
  public AdminInterface(Admin admin, Inventory inventory) {
    this.inventory = inventory;
    this.currentAdmin = admin;
  }

  /**
   * A public constructor for AdminInterface.
   * 
   * @param inventory inventory object.
   */
  public AdminInterface(Inventory inventory) {
    this.inventory = inventory;
  }

  /**
   * set the current employee.
   * 
   * @param admin admin object.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if the id is invalid.
   */
  public void setCurrentAdmin(Admin admin) throws SQLException, InvalidIdException {
    // we need to check if employee created a password
    int id = admin.getId();
    // look up the password of the employee in the database
    String password = DatabaseSelectHelper.getPassword(id);
    // check the password in database matches
    boolean authenticated = admin.authenticate(password);
    // if the employee set a password, we set current employee to it since it is authenticated
    if (authenticated) {
      this.currentAdmin = admin;
    }
  }

  /**
   * Check if we have an employee right now.
   * 
   * @return True if the interface has an employee.
   */
  public boolean hasCurrentAdmin() {
    return this.currentAdmin != null;
  }

  /**
   * Update the Inventory with the quantity of item given
   * 
   * @param item item wants to restock.
   * @param quantity quantity of the item to restock.
   * @return Return true if the operation is successful, false otherwise.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidQuantityException thrown if something goes wrong with the quantity.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   * @throws InvalidIdException thrown if the id is invalid.
   */
  public boolean restockInventory(Item item, int quantity)
      throws SQLException, InvalidQuantityException, InvalidInputException, InvalidIdException {
    // get the id of the item
    int itemId = item.getId();
    // try restocking the inventory
    return DatabaseUpdateHelper.updateInventoryQuantity(quantity, itemId);
  }

  /**
   * Restick the inventory of a given item and quantity.
   * @param item item in the store.
   * @param quantity the quantity that needs to be restocked.
   * @param context the context of the state of the application.
   * @return True if operation was successful, falase otherwise.
   */
  public boolean restockInventory(Item item, int quantity, Context context) {
    // get current quantity of item
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    int currentQuantity = sel.getInventoryQuantity(item.getId());

    // restock the inventory with the new quantity
    DatabaseAndroidUpdateHelper upd =  new DatabaseAndroidUpdateHelper(context);
    return upd.updateInventoryQuantity(currentQuantity + quantity, item.getId(), context);
  }


  /**
   * Create a new activity_customer with the information provided.
   * 
   * @param name name of the activity_customer.
   * @param age age of the activity_customer.
   * @param address address of the activity_customer.
   * @param password password of the activity_customer.
   * @return Return true if the operation succeed, false otherwise.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the quantity.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   */
  public int createCustomer(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException, InvalidRoleException, InvalidIdException,
      InvalidInputException {
    // try inserting the activity_customer into the database. an exception will be raised if not possible
    int customerId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    // get the roleId's in the databases
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    // search for the role id of activity_customer
    for (Integer roleId : roleIds) {
      // once role id of activity_customer is found, establish the activity_customer as a activity_customer in the database
      if (DatabaseSelectHelper.getRoleName(roleId).equals("CUSTOMER")) {
        DatabaseInsertHelper.insertUserRole(customerId, roleId);
        break;
      }
    }
    // return the customers id
    return customerId;
  }

  /**
   * Create a new customer for the store.
   * @param name name of customer.
   * @param age age of customer.
   * @param address address of customer.
   * @param password password of customer.
   * @param context the context of the state of the application.
   * @return the id of the customer.
   */
  public int createCustomer(String name, int age, String address, String password, Context context) {
    // try inserting the activity_customer into the database. an exception will be raised if not possible
    DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);

    int customerId = (int) ins.insertNewUser(name, age, address, password);
    // initialize select helper
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);

    // search for activity_customer role id
    List<Integer> roleIds = sel.getRoleIdsHelper();

    for(Integer roleId : roleIds){
      // once we have found customer role id, establish user as customer
      if(sel.getRoleName(roleId).equals("CUSTOMER")){
        ins.insertUserRole(customerId, roleId);
        // give user a message with the user id
        return customerId;
      }
    }
      return customerId;
  }



  /**
   * Create a new employee with the information provided.
   * 
   * @param name name of the employee.
   * @param age age of the employee.
   * @param address address of the employee.
   * @param password password of the employee.
   * @return Return true if the operation succeed, false otherwise.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the quantity.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   */
  public int createAdmin(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException, InvalidRoleException, InvalidIdException,
      InvalidInputException {
    // try inserting the employee into the database. an exception will be raised if not possible
    int employeeId = DatabaseInsertHelper.insertNewUser(name, age, address, password);

    // get the roleId's in the databases
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    // search for the role id of employee
    for (Integer roleId : roleIds) {
      // once role id of employee is found, establish the user an employee in the database
      if (DatabaseSelectHelper.getRoleName(roleId).equals("EMPLOYEE")) {
        DatabaseInsertHelper.insertUserRole(employeeId, roleId);
        break;
      }
    }
    // return the customers id
    return employeeId;
  }

  public int createAdmin(String name, int age, String address, String password, Context context) {
    // try inserting the user into the database. an exception will be raised if not possible
    DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);

    int adminId = (int) ins.insertNewUser(name, age, address, password);
    // initialize select helper
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);

    // search for admin role id
    List<Integer> roleIds = sel.getRoleIdsHelper();

    for(Integer roleId : roleIds){
      // once we have found customer role id, establish user as customer
      if(sel.getRoleName(roleId).equals("ADMIN")){
        ins.insertUserRole(adminId, roleId);
        // give user a message with the user id
        return adminId;
      }
    }
    return adminId;
  }



  /**
   * Creater a new account for a user.
   * @param userId the Id of the user.
   * @return the Account that was just created.
   * @throws SQLException if an SQL error occurs.
   * @throws InvalidInputException if an input error occurs.
   * @throws DatabaseInsertException if an insert error occurs.
   * @throws InvalidRoleException if an invalid role error occurs.
   * @throws InvalidIdException if an invalid id error occurs.
   */
  public Account createAccount(int userId) throws SQLException, InvalidInputException,
      DatabaseInsertException, InvalidRoleException, InvalidIdException {
    return DatabaseSelectHelper.getAccountDetails(DatabaseInsertHelper.insertAccount(userId));
  }

  /**
   * .
   * 
   * @return return a historical sales records
   * @throws SQLException on failure
   * @throws InvalidRoleException on invalid role input
   * @throws InvalidInputException on invalid input
   * @throws InvalidIdException on invalid id input
   */
  public String viewBooks()
      throws SQLException, InvalidRoleException, InvalidInputException, InvalidIdException {
    String statement = "";

    // create a hashmap to store the quantity of each item sold
    HashMap<Item, Integer> totalItemsSold = new HashMap<Item, Integer>();

    // create int to store total sales
    BigDecimal totalSales = new BigDecimal("0.00");

    // get the sales log
    SalesLog salesLog = DatabaseSelectHelper.getSales();
    // get the itemized sales
    SalesLog itemizedSalesLog = new SalesLogImpl();
    DatabaseSelectHelper.getItemizedSales(itemizedSalesLog);

    // loop through all the sales in saleslog
    for (Sale sale : salesLog.getSales()) {
      // get the itemized sale
      // add the activity_customer name
      statement += "Customer: " + sale.getUser().getName() + "\n";

      // add the purchase number
      statement += "Purchase Number: " + sale.getId() + "\n";

      // add the total purchase price to string and totalSales counter
      statement += "Total Purchase Price: " + sale.getTotalPrice() + "\n";
      totalSales = totalSales.add(sale.getTotalPrice());

      // add the itemized breakdown
      statement += "Itemized Breakdown: ";

      for (Sale itemizedSale : itemizedSalesLog.getSales()) {
        if (itemizedSale.getId() == sale.getId()) {
          List<Item> itemList = new ArrayList<>(itemizedSale.getItemMap().keySet());
          statement += itemList.get(0).getName() + ": "
              + itemizedSale.getItemMap().get(itemList.get(0)) + "\n";

          // check if the item is already defined the total items hashmap
          if (totalItemsSold.get(itemList.get(0)) != null) {
            totalItemsSold.put(itemList.get(0), totalItemsSold.get(itemList.get(0))
                + itemizedSale.getItemMap().get(itemList.get(0)));
          } else {
            totalItemsSold.put(itemList.get(0), itemizedSale.getItemMap().get(itemList.get(0)));
          }
        }
      }


      // add the divider line to seperate customers
      statement += "----------------------------------------------------------------\n";
    }
    // now add the total quantites of each item to the string
    for (Item item : totalItemsSold.keySet()) {
      statement += "Number " + item.getName() + " Sold: " + totalItemsSold.get(item) + "\n";
    }

    // now add the total sum to the string
    statement += "TOTAL SALES: " + totalSales;
    return statement;
  }


  /**
   * Get a string of all the sales that took place
   * @param context the context of the state of the application.
   * @return A string that shows all the sales
   * @throws InvalidIdException if an invalid Id error occurs
   * @throws InvalidRoleException if an invalid role error occrus
   */
  public String viewBooks(Context context) throws InvalidIdException, InvalidRoleException {
    String statement = "";

    // create a hashmap to store the quantity of each item sold
    HashMap<String, Integer> totalItemsSold = new HashMap<>();

    // create int to store total sales
    BigDecimal totalSales = new BigDecimal("0.00");
    HashMap<Item, Integer> itemToQuantity;
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    // get the sales log
    SalesLog salesLog = sel.getSalesHelper();
    // get the itemized sales
    sel.getItemizedSalesHelper(salesLog);


    // loop through all the sales in saleslog
    for (Sale sale : salesLog.getSales()) {
      // get the itemized sale
      // add the activity_customer name
      statement += "Customer: " + sale.getUser().getName() + "\n";

      // add the purchase number
      statement += "Purchase Number: " + sale.getId() + "\n";

      // add the total purchase price to string and totalSales counter
      statement += "Total Purchase Price: " + sale.getTotalPrice() + "\n";
      totalSales = totalSales.add(sale.getTotalPrice());

      // add the itemized breakdown
      statement += "Itemized Breakdown: " ;

      for(Item item : sale.getItemMap().keySet()){
          String itemName = item.getName();
          int quantity = sale.getItemMap().get(item);
          statement += itemName + ": " + quantity + "\n" + "                                        ";
          if(totalItemsSold.get(item.getName()) == null){
              totalItemsSold.put(itemName, quantity);
          } else {
              totalItemsSold.put(itemName, totalItemsSold.get(itemName) + quantity);
          }

      }
      // add the divider line to seperate customers
        statement = statement.substring(0, statement.length()- 40);
      statement += "----------------------------------------------------------------\n";
    }
    // now add the total quantites of each item to the string
    for (String itemName : totalItemsSold.keySet()) {
      statement += "Number " + itemName + " Sold: " + totalItemsSold.get(itemName) + "\n";
    }
    // now add the total sum to the string
    statement += "TOTAL SALES: " + totalSales;
    return statement;
  }




  /**
   * Inserts any item into the database.
   * 
   * @param itemName the name of the item to be inserted
   * @return the id of the inserted item, -1 if item cold not be inserted
   * @throws InvalidInputException
   * @throws SQLException
   * @throws DatabaseInsertException
   */
  public int addItem(String itemName, BigDecimal price)
      throws DatabaseInsertException, SQLException, InvalidInputException {
    int itemId = -1;
    // insert item into database
    itemId = DatabaseInsertHelper.insertItem(itemName, price);
    return itemId;
  }

  /**
   * Add an item to the store.
   * @param itemName the name of the item.
   * @param price the price of the item.
   * @param context the context of the state of the application.
   * @return the item id if the newly created item
   * @throws InvalidInputException if an invalid input occurs
   * @throws InvalidIdException if an invalid Id error occurs
   */
  public int addItem(String itemName, BigDecimal price, Context context) throws InvalidInputException, InvalidIdException {
      int itemId = -1;

      // insert item into database
      DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);
      itemId = (int) ins.insertItem(itemName, price, context);

      // insert item into inventory if itemid > 0 (meaning it has been inserted into database)
      if(itemId > 0){
        ins.insertInventoryHelper(itemId, 0, context);
      }


      // return the item's id
      return itemId;
  }

  /**
   * Edit a user's information.
   * @param userId the Id of the user.
   * @param name name of the user.
   * @param age age of the user.
   * @param address address of the user.
   * @param context the context of the state of the application.
   * @return True if operation was successful, false otherwise.
   * @throws InvalidIdException if an invalid Id error occurs.
   * @throws InvalidRoleException if an invalid Role error occurs.
   */
  public boolean editUser(int userId, String name, int age, String address, Context context) throws InvalidIdException, InvalidRoleException {
    DatabaseAndroidUpdateHelper upd = new DatabaseAndroidUpdateHelper(context);
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    // update users name
    if (sel.getUser(userId) != null) {
      boolean editName = upd.updateUserNameHelper(name, userId, context);
      boolean editAge = upd.updateUserAgeHelper(userId, age, context);
      boolean editAddress = upd.updateUserAddressHelper(address,userId, context);
      return editName && editAge && editAddress;
    }
    return false;
  }

  /**
   * Get a list of all inacctive accounts for a given user.
   * @param userId the id of the user.
   * @param context the context of the state of the application.
   * @return A list that has all the inactive accounts
   * @throws InvalidIdException If an invalid Id error occurs.
   */
  public List<Account> getInactiveAccounts(int userId, Context context) throws InvalidIdException {
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        // get inactive accounts
        List<Integer> userInactiveAccountIds = sel.getUserInactiveAccountsHelper(userId);
        List<Account> userInactiveAccounts = new ArrayList<>();
        for(Integer current : userInactiveAccountIds){
            userInactiveAccounts.add(sel.getAccountDetailsHelper(current));
        }

        return userInactiveAccounts;
    }

  /**
   * Get a list of all active accounts for a given user.
   * @param userId the is of the user.
   * @param context the context of the state of the application.
   * @return A list that has all the active accounts.
   * @throws InvalidIdException If an invalid Id error occurs.
   */
    public List<Account> getActiveAccounts(int userId, Context context) throws InvalidIdException {
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        // get inactive accounts
        List<Integer> userActiveAccountIds = sel.getUserActiveAccountsHelper(userId);
        List<Account> userActiveAccounts = new ArrayList<>();
        for(Integer current : userActiveAccountIds){
            userActiveAccounts.add(sel.getAccountDetailsHelper(current));
        }

        return userActiveAccounts;
    }




}
