package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
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
   * Create a new customer with the information provided.
   * 
   * @param name name of the customer.
   * @param age age of the customer.
   * @param address address of the customer.
   * @param password password of the customer.
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
    // try inserting the customer into the database. an exception will be raised if not possible
    int customerId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    // get the roleId's in the databases
    List<Integer> roleIds = DatabaseSelectHelper.getRoleIds();
    // search for the role id of customer
    for (Integer roleId : roleIds) {
      // once role id of customer is found, establish the customer as a customer in the database
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
      // add the customer name
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
   * Inserts any item into the database.
   * 
   * @param itemName the name of the item to be inserted
   * @return the id of the inserted item, -1 if item cold not be inserted
   * @throws InvalidInputException
   * @throws SQLException
   * @throws DatabaseInsertException
   */
  public int addItem(String itemName, int price)
      throws DatabaseInsertException, SQLException, InvalidInputException {
    int itemId = -1;
    // convert the price to big decimal
    BigDecimal itemPrice = new BigDecimal(price);

    // insert item into database
    itemId = DatabaseInsertHelper.insertItem(itemName, itemPrice);


    return itemId;
  }


}