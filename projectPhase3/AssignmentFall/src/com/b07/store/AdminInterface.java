package com.b07.store;

import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.InvalidStringException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.serializable.SerializeDemo;
import com.b07.users.Account;
import com.b07.users.Admin;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminInterface {
  private Admin currentAdmin;
  private Inventory inventory;

  /**
   * A public constructor for EmployeeInterface.
   * 
   * @param inventory inventory object.
   */
  public AdminInterface(Admin admin, Inventory inventory) {
    this.inventory = inventory;
    this.currentAdmin = admin;
  }

  /**
   * A public constructor for EmployeeInterface.
   * 
   * @param inventory inventory object.
   */
  public AdminInterface(Inventory inventory) {
    this.inventory = inventory;
  }

  /**
   * set the current employee.
   * 
   * @param admin employee object.
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
    // if the employee set a password, we set current employee to it since it is
    // authenticated
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
    // get current quantity of item
    int currentQuantity = DatabaseSelectHelper.getInventoryQuantity(item.getId());

    // restock the inventory with the new quantity
    return DatabaseUpdateHelper.updateInventoryQuantity(currentQuantity + quantity, item.getId());
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
    // try inserting the activity_customer into the database. an exception will be
    // raised if not possible
    int customerId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    // get the roleId's in the databases
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    // search for the role id of activity_customer
    for (Integer roleId : roleIds) {
      // once role id of activity_customer is found, establish the activity_customer
      // as a activity_customer in the database
      if (DatabaseSelectHelper.getRoleName(roleId).equals("CUSTOMER")) {
        DatabaseInsertHelper.insertUserRole(customerId, roleId);
        break;
      }
    }
    // return the customers id
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
    // try inserting the employee into the database. an exception will be raised if
    // not possible
    int employeeId = DatabaseInsertHelper.insertNewUser(name, age, address, password);

    // get the roleId's in the databases
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    // search for the role id of employee
    for (Integer roleId : roleIds) {
      // once role id of employee is found, establish the user an employee in the
      // database
      if (DatabaseSelectHelper.getRoleName(roleId).equals("EMPLOYEE")) {
        DatabaseInsertHelper.insertUserRole(employeeId, roleId);
        break;
      }
    }
    // return the customers id
    return employeeId;
  }

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

  public String viewBooks() throws InvalidIdException, InvalidRoleException, SQLException {
    String statement = "";

    // create a hashmap to store the quantity of each item sold
    HashMap<String, Integer> totalItemsSold = new HashMap<>();

    // create int to store total sales
    BigDecimal totalSales = new BigDecimal("0.00");
    HashMap<Item, Integer> itemToQuantity;
    // get the sales log
    SalesLog salesLog = DatabaseSelectHelper.getSales();
    // get the itemized sales
    DatabaseSelectHelper.getItemizedSales(salesLog);

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

      for (Item item : sale.getItemMap().keySet()) {
        String itemName = item.getName();
        int quantity = sale.getItemMap().get(item);
        statement += itemName + ": " + quantity + "\n" + "                                        ";
        if (totalItemsSold.get(item.getName()) == null) {
          totalItemsSold.put(itemName, quantity);
        } else {
          totalItemsSold.put(itemName, totalItemsSold.get(itemName) + quantity);
        }

      }
      // add the divider line to seperate customers
      statement = statement.substring(0, statement.length() - 40);
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
   * @throws InvalidInputException if item can not be inserted
   * @throws SQLException if SQL error arises
   * @throws DatabaseInsertException if item cannot be inserted
   */
  public int addItem(String itemName, BigDecimal price)
      throws DatabaseInsertException, SQLException, InvalidInputException {
    int itemId = -1;

    // insert item into database
    itemId = DatabaseInsertHelper.insertItem(itemName, price);

    // insert item into inventory if itemid > 0 (meaning it has been inserted into
    // database)
    if (itemId > 0) {
      DatabaseInsertHelper.insertInventory(itemId, 0);
    }

    // return the item's id
    return itemId;
  }

  /**
   * Edit a given users information.
   * 
   * @param userId id of user
   * @param name new name of user
   * @param age new age of user
   * @param address new address of user
   * @return if the user was edited succesfuly.
   * @throws InvalidIdException if user id invalid
   * @throws InvalidRoleException if user not given role yet
   * @throws SQLException if sql eror
   * @throws InvalidInputException if input error
   * @throws InvalidStringException if any of the input strings are invalid
   */
  public boolean editUser(int userId, String name, int age, String address)
      throws InvalidIdException, InvalidRoleException, SQLException, InvalidInputException,
      InvalidStringException {
    // update users name
    if (DatabaseSelectHelper.getUserDetails(userId) != null) {
      boolean editName = DatabaseUpdateHelper.updateUserName(name, userId);
      boolean editAge = DatabaseUpdateHelper.updateUserAge(userId, age);
      boolean editAddress = DatabaseUpdateHelper.updateUserAddress(address, userId);
      return editName && editAge && editAddress;
    }
    return false;
  }

  /**
   * Get inactive accounts of user.
   * 
   * @param userId id of user
   * @return users inactive acounts
   * @throws InvalidIdException if id invalid
   * @throws SQLException if sql error
   * @throws InvalidInputException if input goes wrong
   */
  public List<Account> getInactiveAccounts(int userId)
      throws InvalidIdException, SQLException, InvalidInputException {
    // get inactive accounts
    List<Integer> userInactiveAccountIds = DatabaseSelectHelper.getUserInactiveAccounts(userId);
    List<Account> userInactiveAccounts = new ArrayList<>();
    for (Integer current : userInactiveAccountIds) {
      userInactiveAccounts.add(DatabaseSelectHelper.getAccountDetails(current));
    }

    return userInactiveAccounts;
  }

  /**
   * Get active accounts of user.
   * 
   * @param userId id of user
   * @return users active accounts
   * @throws InvalidIdException if user id invalid
   * @throws SQLException if sql error
   * @throws InvalidInputException if input goes worng
   */
  public List<Account> getActiveAccounts(int userId)
      throws InvalidIdException, SQLException, InvalidInputException {
    // get inactive accounts
    List<Integer> userActiveAccountIds = DatabaseSelectHelper.getUserActiveAccounts(userId);
    List<Account> userActiveAccounts = new ArrayList<>();
    for (Integer current : userActiveAccountIds) {
      userActiveAccounts.add(DatabaseSelectHelper.getAccountDetails(current));
    }

    return userActiveAccounts;
  }

  /**
   * Serialize the database.
   * 
   * @throws IOException if file can not be found
   */

  public void serialize() throws IOException {
    // check if admin is logged in
    if (this.currentAdmin != null) {
      // get copy of database and serialize
      SerializeDemo.serialize(DatabaseDriverHelper.connectOrCreateDataBase());
    }
  }

  /**
   * Deserialize the database.
   * 
   * @throws IOException if file not found
   * @throws ClassNotFoundException if class not found
   * 
   */
  public void deserialize() throws ClassNotFoundException, IOException {
    if (this.currentAdmin != null) {
      SerializeDemo.deserialize();
    }
  }


}
