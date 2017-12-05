package com.b07.database.helper;

import com.b07.database.DatabaseUpdater;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.InvalidStringException;
import com.b07.inventory.Item;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * Update the role name of a given role in the role table.
   * 
   * @param name the new name of the role.
   * @param id the current ID of the role.
   * @return true if successful, false otherwise.
   * @throws InvalidStringException if the String is not valid
   * @throws InvalidIdException if the Id is not valid
   */
  public static boolean updateRoleName(String name, int id)
      throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException {

    List<Integer> roleidlist = DatabaseSelectHelper.getRoleIds();
    // check if name is appropriate (not null or empty)
    boolean nameCheck = goodString(name);
    boolean idCheck = positiveInt(id);

    if (nameCheck == idCheck == true) {
      for (Integer roleid : roleidlist) {
        // check if the id exists in the list of roles
        if (id == roleid) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
          connection.close();
          return complete;
        }
      }
    }

    if (nameCheck == false) {
      throw new InvalidStringException("The name entered is not valid.");
    }

    if (idCheck == false) {
      throw new InvalidIdException("The Id that was inputed was not not valid.");
    }

    return false;
  }

  /**
   * Use this to update the user's name.
   * 
   * @param name the new name
   * @param userId user's id
   * @return true if it works, false otherwise.
   * @throws InvalidStringException if an invalid String is given
   * @throws InvalidIdException if invalid id is entered
   * @throws InvalidRoleException if the role is invalid
   */
  public static boolean updateUserName(String name, int userId) throws SQLException,
      InvalidInputException, InvalidStringException, InvalidIdException, InvalidRoleException {
    List<User> userlist = DatabaseSelectHelper.getUsersDetails();

    // check if name is appropriate (not null or empty)
    boolean nameCheck = goodString(name);
    // check if number is not negative
    boolean userIdCheck = positiveInt(userId);
    // check if user exists in the database
    boolean userIdExist = userExists(userId);

    if (nameCheck == userIdCheck == userIdExist == true) {
      for (User user : userlist) {
        if (userId == user.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
          connection.close();
          return complete;
        }
      }
    }

    if (nameCheck == false) {
      throw new InvalidStringException("The name entered is not valid.");
    }

    if (userIdCheck == false) {
      throw new InvalidIdException("The Id that was inputed was not not valid.");
    }

    if (userIdExist = false) {
      throw new InvalidIdException("The Id of the user does not exist.");
    }
    return false;
  }

  /**
   * Update the user's age.
   * 
   * @return true for success, o/w false
   * @throws SQLException on failure
   * @throws InvalidInputException an invalid input
   * @throws InvalidIdException if invalid id is entered
   * @throws InvalidRoleException if the role in invalid
   */
  public static boolean updateUserAge(int age, int userId)
      throws SQLException, InvalidInputException, InvalidIdException, InvalidRoleException {

    // perform check for appropriate values
    boolean ageCheck = age >= 0;
    boolean userIdCheck = positiveInt(userId);
    boolean userIdExist = userExists(userId);
    List<User> userIdlist = DatabaseSelectHelper.getUsersDetails();

    if (ageCheck == userIdCheck == userIdExist == true) {
      for (User user : userIdlist) {
        if (userId == user.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
          connection.close();
          return complete;
        }
      }
    }

    if (ageCheck == false) {
      throw new InvalidInputException("The age entered is not valid.");
    }

    if (userIdCheck == false) {
      throw new InvalidIdException("The Id that was inputed was not not valid.");
    }

    if (userIdExist = false) {
      throw new InvalidIdException("The Id of the user does not exist.");
    }
    return false;
  }

  /**
   * Use this to update user's address.
   * 
   * @param address the new address.
   * @param userId the current id
   * @return true if successful, false otherwise.
   * @throws InvalidStringException an invalid string was entered
   * @throws InvalidIdException the id in not valid
   * @throws InvalidRoleException if the role is invalid
   */
  public static boolean updateUserAddress(String address, int userId) throws SQLException,
      InvalidInputException, InvalidStringException, InvalidIdException, InvalidRoleException {

    // perform check for appropriate values
    boolean addressCheck = goodString(address);
    boolean userIdCheck = positiveInt(userId);
    boolean addressLength = address.length() <= 100;
    boolean userIdExist = userExists(userId);
    List<User> userIdlist = DatabaseSelectHelper.getUsersDetails();

    if (addressCheck == userIdCheck == addressLength == userIdExist == true) {
      for (User user : userIdlist) {
        if (userId == user.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
          connection.close();
          return complete;
        }
      }
    }

    if (addressCheck == false) {
      throw new InvalidStringException("The adress entered is not valid.");
    }

    if (userIdCheck == false) {
      throw new InvalidIdException("The Id that was inputed was not not valid.");
    }

    if (userIdExist = false) {
      throw new InvalidIdException("The Id of the user does not exist.");
    }

    if (addressLength == false) {
      throw new InvalidInputException("The address length input is not valid");
    }

    return false;
  }

  /**
   * Update the user's role
   * 
   * @param roleId.
   * @param userId.
   * @return true for success, o/w false
   * @throws SQLException on failure
   * @throws InvalidInputException on invalid inputs
   * @throws InvalidIdException the id was not found or valid
   * @throws InvalidRoleException if the role is invalid
   */
  public static boolean updateUserRole(int roleId, int userId)
      throws SQLException, InvalidInputException, InvalidIdException, InvalidRoleException {
    List<User> userlist = DatabaseSelectHelper.getUsersDetails();

    // perform checks
    boolean roleIdCheck = positiveInt(roleId);
    boolean userIdCheck = positiveInt(userId);
    boolean userIdExist = userExists(userId);

    if (roleIdCheck == userIdCheck == userIdExist == true) {
      for (User user : userlist) {
        if (userId == user.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
          connection.close();
          return complete;
        }
      }
    }
    // deal with all the exceptions
    if (roleIdCheck == false) {
      throw new InvalidInputException("The role Id entered is not valid.");
    }

    if (userIdCheck == false) {
      throw new InvalidInputException("The Id that was inputed was not not valid.");
    }

    if (userIdExist = false) {
      throw new InvalidIdException("The Id of the user does not exist.");
    }

    return false;
  }

  /**
   * Update the name of an item currently in the database.
   * 
   * @param name the new name.
   * @param itemId the id of the current item.
   * @return true if successful, false otherwise.
   * @throws InvalidStringException if the name is not valid
   * @throws InvalidIdException if the Id does not exist in the database
   * @throws InvalidInputException if an invalid input was entered
   */
  public static boolean updateItemName(String name, int itemId)
      throws SQLException, InvalidInputException, InvalidStringException, InvalidIdException {
    List<Item> itemidlist = DatabaseSelectHelper.getAllItems();

    // perform checks
    boolean nameCheck = goodString(name);
    boolean itemIdCheck = positiveInt(itemId);
    boolean itemExist = itemExists(itemId);

    if (nameCheck == itemIdCheck == itemExist == true) {
      for (Item item : itemidlist) {
        if (itemId == item.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
          connection.close();
          return complete;
        }
      }
    }
    // deal with all the exceptions
    if (nameCheck == false) {
      throw new InvalidStringException("The name of the item entered is not valid.");
    }

    if (itemIdCheck == false) {
      throw new InvalidInputException("The Id that was inputed was not not valid.");
    }

    if (itemExist == false) {
      throw new InvalidIdException("The Id that was inputed was not in the database");
    }

    return false;
  }

  /**
   * update the price of an item in the database.
   * 
   * @param price the new price for the item.
   * @param itemId the id of the item.
   * @return true if successful, false otherwise.
   * @throws InvalidIdException the id does not exist
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId)
      throws SQLException, InvalidInputException, InvalidIdException {
    List<Item> itemidlist = DatabaseSelectHelper.getAllItems();

    // perform checks
    boolean priceCheck = (price.doubleValue() > 0);
    boolean itemIdCheck = positiveInt(itemId);
    boolean itemExist = itemExists(itemId);

    if (priceCheck == itemIdCheck == itemExist == true) {
      for (Item item : itemidlist) {
        if (itemId == item.getId()) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
          connection.close();
          return complete;
        }
      }
    }
    // deal with all the exceptions
    if (priceCheck == false) {
      throw new InvalidInputException("The price of the item entered is not valid.");
    }

    if (itemIdCheck == false) {
      throw new InvalidInputException("The Id that was inputed was not not valid.");
    }

    if (itemExist == false) {
      throw new InvalidIdException("The Id does not exist in the database");
    }

    return false;
  }

  /**
   * update the quantity available in inventory for a given item.
   * 
   * @param quantity the new quantity.
   * @param itemId the item to be updated.
   * @return true if successful, false otherwise.
   * @throws InvalidIdException the id does not exist in the database
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId)
      throws SQLException, InvalidInputException, InvalidIdException {
    // THIS LINE IS NOT NECESSARY
    // Inventory inventory = DatabaseSelectHelper.getInventory();

    // perform checks
    boolean quantityCheck = quantity >= 0;
    boolean itemExist = itemExists(itemId);
    boolean itemIdCheck = positiveInt(itemId);

    if (quantityCheck == itemIdCheck == itemExist == true) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
      connection.close();
      return complete;

    }
    // deal with all the exceptions
    if (quantityCheck == false) {
      throw new InvalidInputException("The quantity of the item entered is not valid.");
    }

    if (itemIdCheck == false) {
      throw new InvalidInputException("The Id that was inputed was not not valid.");
    }

    if (itemExist == false) {
      throw new InvalidIdException("The Id does not exist in the database");
    }

    return false;

  }

  /**
   * change the active status of an account.
   * 
   * @param accountId the id of the account
   * @param active if the account is active(true) or inactive(false)
   * @return true if operation was sucesful, false otherwise
   * @throws SQLException if SQL error occurs
   * @throws InvalidInputException if input was invalid
   * @throws InvalidRoleException if role is invalid
   * @throws InvalidIdException if id is invalid
   */
  public static boolean updateAccountStatus(int accountId, boolean active)
      throws SQLException, InvalidInputException, InvalidRoleException, InvalidIdException {

    boolean complete = false;
    boolean accountIdcheck = positiveInt(accountId) == userExists(accountId);

    if (accountIdcheck == true) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      complete = DatabaseUpdater.updateAccountStatus(accountId, active, connection);
      connection.close();
      return complete;
    }

    if (accountIdcheck == false) {
      throw new InvalidIdException("The Id is not valid");
    }
    return complete;
  }

  /**
   * update the password of the user.
   * 
   * @param password the new password
   * @param id the id of the user
   * @return true if operation was sucessful, false otherwise
   * @throws SQLException if SQL error occurs
   * @throws InvalidInputException if input is not valid
   * @throws InvalidRoleException if role is not valid
   * @throws InvalidIdException if id is not valid
   * @throws InvalidStringException if string is not valid
   */
  public static boolean updateUserPassword(String password, int id) throws SQLException,
      InvalidInputException, InvalidRoleException, InvalidIdException, InvalidStringException {
    // perform checks
    boolean wordCheck = goodString(password);
    boolean userIdCheck = positiveInt(id);
    boolean userExist = userExists(id);
    boolean complete = false;
    if (wordCheck == userIdCheck == userExist == true) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      complete = DatabaseUpdater.updateUserPassword(password, id, connection);

      connection.close();
    }
    // deal with all the exceptions
    if (wordCheck == false) {
      throw new InvalidStringException("The password entered is not valid.");
    }

    if (userIdCheck == false) {
      throw new InvalidInputException("The Id that was inputed was not not valid.");
    }

    if (userExist == false) {
      throw new InvalidIdException("The Id that was inputed was not in the database");
    }

    return complete;

  }

  /**
   * check if a name is in a correct format.
   * 
   * @param name the name that needs to be checked.
   * @return false if name is null or blank, otherwise true
   */

  private static boolean goodString(String name) {
    boolean good = true;
    if (name.equals(null)) {
      good = false;
    }
    if (name.equals("")) {
      good = false;
    }
    return good;
  }

  /**
   * check if a number is positive
   * 
   * @param num the number that needs to be checked
   * @return false if num is less than or equals 0. True otherwise
   */
  private static boolean positiveInt(int num) {
    boolean good = true;
    if (num <= 0) {
      good = false;
    }
    return good;
  }

  /**
   * check if the user exists in the database.
   * 
   * @param userId the user's Id that needs to be checked
   * @return true if the user exists in the database
   * @throws SQLException if an SQL error exists
   * @throws InvalidInputException if an invalid input exists
   * @throws InvalidIdException if the Id is invalid
   * @throws InvalidRoleException if the role is invalid
   */
  private static boolean userExists(int userId)
      throws SQLException, InvalidInputException, InvalidRoleException, InvalidIdException {
    boolean idExists = false;
    List<User> userIdList = DatabaseSelectHelper.getUsersDetails();

    for (User user : userIdList) {
      if (user.getId() == userId) {
        idExists = true;
      }
    }
    return idExists;
  }

  /**
   * check if the item exists in the database.
   * 
   * @param itemId the id of the item
   * @return true if item exists in the database
   * @throws SQLException if SQL error occurs
   */
  private static boolean itemExists(int itemId) throws SQLException {
    boolean idExists = false;

    List<Item> itemIdList = DatabaseSelectHelper.getAllItems();

    for (Item item : itemIdList) {
      if (itemId == item.getId()) {
        idExists = true;
      }
    }

    return idExists;
  }
}
