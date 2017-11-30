package com.b07.database.helper;

import com.b07.database.DatabaseInserter;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.enumerators.Roles;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseInsertHelper extends DatabaseInserter {

  /**
   * Return a roleId by given role name
   * 
   * @param name of the user.
   * @return roleId of the user.
   * @throws DatabaseInsertException thrown if something goes wrong with the database insert.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidInputException thrown if something is invalid.
   */
  public static int insertRole(String name)
      throws SQLException, InvalidRoleException, DatabaseInsertException, InvalidInputException {
    int roleId = -1;
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // check if the role is a valid enum
    if (name.equals(Roles.ADMIN.toString()) || name.equals(Roles.EMPLOYEE.toString())
        || name.equals(Roles.CUSTOMER.toString())) {
      List<Integer> allrolesids = DatabaseSelectHelper.getRoleIds();
      // first check the database if that name has been inserted
      for (int id : allrolesids) {
        if (DatabaseSelectHelper.getRoleName(id).equals(name)) {
          throw new DatabaseInsertException("The role has already exist in Database");
          // if not, we can create a new roleId
        }
      }
      roleId = DatabaseInserter.insertRole(name, connection);
      connection.close();
      return roleId;
    } else {
      // raise an InvalidInputException if the name is not in enum type
      throw new InvalidRoleException("This is an invalid Rolename");
    }
  }

  /**
   * use this to get the userId.
   * 
   * @param name of the user.
   * @param age of the user.
   * @param address of the user.
   * @param password of the user.
   * @return the id of the role that was inserted
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException on invalid input.
   */

  public static int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException, InvalidInputException {
    // check if the input is valid, o/w raise InvalidInputException
    if (age >= 0 & address.length() <= 100 & password != null) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
      connection.close();
      return userId;
    } else {
      throw new InvalidInputException("This is an invalid input");
    }
  }

  /**
   * use this to get the userRoleId.
   * 
   * @param userId of the user.
   * @param roleId of the user.
   * @return userRoleId role id of the user.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException thrown on invalid inputs.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   * @throws InvalidRoleException thrown if role is invalid.
   */

  public static int insertUserRole(int userId, int roleId) throws DatabaseInsertException,
      SQLException, InvalidInputException, InvalidIdException, InvalidRoleException {
    int userRoleId = -1;
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
    connection.close();
    return userRoleId;
  }

  /**
   * Return the itemId by given an item's name and its price
   * 
   * @param name of the item.
   * @param price of the item.
   * @return itemId id of the item.
   * @throws InvalidInputException thrown if input is invalid.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  public static int insertItem(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException, InvalidInputException {

    // initialize the item id and make a big decimal for 0
    int itemId = -1;
    BigDecimal zero = new BigDecimal("0");
    // set the bigdecimal for 0 and price to have presicision of 2 decimal points
    zero = zero.setScale(2);
    price = price.setScale(2);

    List<Item> allItemsList = DatabaseSelectHelper.getAllItems();
    // if the price user input is a negative or name is null, throw an invalid input
    // exception
    // if item name is less 64 and is in ItemTypes, and price is greater than 0,
    // insert the item
    if ((name.length() < 64 && name != null) && (zero.compareTo(price) < 0)) {
      for (Item item : allItemsList) {
        if (item.getName().equals(name)) {
          throw new DatabaseInsertException("The item is already in Database");
        }
      }
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      itemId = DatabaseInserter.insertItem(name, price, connection);
      connection.close();
      return itemId;
    } else {
      // otherwise throw exception
      throw new InvalidInputException("Check name or price is valid");
    }
  }

  /**
   * Use this method to return an inventory number.
   * 
   * @param itemId id of the item.
   * @param quantity of the item.
   * @return inventoryId id of the inventory.
   * @throws InvalidInputException thrown if the input is invalid.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   */

  public static int insertInventory(int itemId, int quantity)
      throws InvalidInputException, DatabaseInsertException, SQLException {
    int inventoryId = -1;
    List<Item> allitemslist = DatabaseSelectHelper.getAllItems();
    // if the price user input is a negative number, throw an invalid input
    // exception
    if (quantity >= 0) {
      // check if the itemId is already exist in our database
      for (Item item : allitemslist) {
        if (item.getId() == itemId) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
          connection.close();
          return inventoryId;
        }
      }
      throw new DatabaseInsertException();
      // if the input is correct, use database function
    } else {
      throw new InvalidInputException("The quantity is invalid");
    }
  }

  /**
   * return a saleId with given userId and totalPrice.
   * 
   * @param userId id of the user.
   * @param totalPrice price of the total item.
   * @return saleId id of the sale.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException thrown if input is invalid.
   * @throws InvalidIdException thrown if id is invalid.
   * @throws InvalidRoleException thrown if role is invalid.
   */
  public static int insertSale(int userId, BigDecimal totalPrice) throws DatabaseInsertException,
      SQLException, InvalidInputException, InvalidRoleException, InvalidIdException {
    // check if the input totalPrice is a negative number
    BigDecimal leastprice = new BigDecimal("0");
    int saleId = -1;
    if (totalPrice.compareTo(leastprice) >= 0) {
      List<Sale> salelist = DatabaseSelectHelper.getSalesToUser(userId);
      // check if the userId has been inserted
      for (Sale sale : salelist) {
        if (sale.getTotalPrice() == totalPrice) {
          throw new DatabaseInsertException("The sale is already in Databse");
        }
      }
      // connect with database and call function
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
      connection.close();
      return saleId;
    } else {
      throw new InvalidInputException("The totalprice is invalid");
    }

  }

  /**
   * return a itemizedId by given saleId, itemId and quantity.
   * 
   * @param saleId id of sale.
   * @param itemId id of item.
   * @param quantity of the item.
   * @return itemizedId id of item sold.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException thrown if input is invalid.
   * @throws InvalidIdException thrown if id is invalid.
   * @throws InvalidRoleException thrown if role is invalid.
   */
  public static int insertItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, InvalidInputException, InvalidRoleException,
      InvalidIdException {
    int itemizedId = -1;
    List<Item> itemslist = DatabaseSelectHelper.getAllItems();
    SalesLog salesLog = DatabaseSelectHelper.getSales();
    for (Item item : itemslist) {
      if (item.getId() == itemId) {
        for (Sale sale : salesLog.getSales()) {
          if (sale.getId() == saleId) {
            if (quantity >= 0 & quantity <= DatabaseSelectHelper.getInventoryQuantity(itemId)) {
              Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
              itemizedId =
                  DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
              connection.close();
              return itemizedId;
            }
          }
        }
      }
    }
    throw new DatabaseInsertException("Check the value of sale/item/quantity");
  }

  /**
   * . return the accoutId for a given userId.
   * 
   * @param userId of a customer
   * @return accountId.
   * @throws DatabaseInsertException on failure
   * @throws InvalidRoleException on invalid role input
   * @throws InvalidIdException on invalid userId input
   * @throws SQLException on failure
   */
  public static int insertAccount(int userId)
      throws DatabaseInsertException, InvalidRoleException, InvalidIdException, SQLException {
    int accountId = -1;
    // check if the userId is a valid userId in database
    List<User> userslist = DatabaseSelectHelper.getUsersDetails();
    for (User user : userslist) {
      if (user.getId() == userId) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        accountId = DatabaseInserter.insertAccount(userId, connection);
        connection.close();
        return accountId;
      }
    }
    // else throw an exception
    throw new InvalidRoleException("This is an invalid userId");

  }

  /**
   * insert a single item into a given account.
   * 
   * @param accountId of a customer
   * @param itemId of an item
   * @param quantity of the item
   * @return the recordId
   * @throws SQLException on failure
   * @throws DatabaseInsertException on failure insert
   * @throws InvalidInputException on invalid input
   * @throws InvalidQuantityException If the quantity is negative
   */
  public static int insertAccountLine(int accountId, int itemId, int quantity) throws SQLException,
      DatabaseInsertException, InvalidInputException, InvalidQuantityException {
    // get list of all items
    if (quantity >= 0) {
      int recordId = -1;
      List<Item> itemlist = DatabaseSelectHelper.getAllItems();
      for (Item item : itemlist) {
        // if item is valid then
        if (item.getId() == itemId) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          // try to insert the account line
          try {
            recordId = DatabaseInserter.insertAccountLine(accountId, itemId, quantity, connection);
          } catch (DatabaseInsertException e) {
            // the item has already been modified once
          } finally {
            connection.close();
          }
          return recordId;
        }
      }
      throw new InvalidInputException("This is an invalid itemId");
    } else {
      throw new InvalidQuantityException("Quantity entered is not valid.");
    }
  }
}
