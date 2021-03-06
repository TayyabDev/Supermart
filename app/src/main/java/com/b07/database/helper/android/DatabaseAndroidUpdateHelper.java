package com.b07.database.helper.android;

import android.content.Context;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.inventory.Item;
import com.b07.users.Account;
import com.b07.users.User;
import java.math.BigDecimal;
import java.util.List;

/**
 * Class containing methods to update information in the database.
 */
public class DatabaseAndroidUpdateHelper extends DatabaseDriverAndroid {

  /**
   * The public constructor for the class
   *
   * @param context the context of the state of the application
   */
  public DatabaseAndroidUpdateHelper(Context context) {
    super(context);
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
   * check if the item exists in the database.
   *
   * @param itemId the id of the item
   * @return true if item exists in the database
   */
  private static boolean itemExists(int itemId, Context context) {
    boolean idExists = false;
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<Item> itemIdList = sel.getAllItemsHelper();

    // loop through all items
    for (Item item : itemIdList) {
      if (itemId == item.getId()) {
        idExists = true;
      }
    }

    return idExists;
  }

  /**
   * Update the inventory in the store
   *
   * @param quantity the amount you want to put in the store
   * @param itemId the Id of the item
   * @param context the context of the current state of the application.
   * @return true if operation was successful, false otherwise.
   */
  public boolean updateInventoryQuantity(int quantity, int itemId, Context context) {
    boolean quantityCheck = quantity >= 0;
    boolean itemIdCheck = positiveInt(itemId);
    boolean itemExist = itemExists(itemId, context);

    // if inputs valid then update the inventory quantity
    if (quantityCheck && itemIdCheck && itemExist) {
      return super.updateInventoryQuantity(quantity, itemId);
    }
    // if inputs not valid return false
    return false;
  }

  /**
   * Update the name of the role
   *
   * @param name the name of the role
   * @param id the id of the role
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateRoleNameHelper(String name, int id, Context context) {
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<Integer> roleidlist = sel.getRoleIdsHelper();
    // check if name is appropriate (not null or empty)
    boolean nameCheck = goodString(name);
    boolean idCheck = positiveInt(id);

    // if the inputs are good
    if (nameCheck && idCheck) {
      for (Integer roleid : roleidlist) {
        // check if the id exists in the list of roles
        if (id == roleid) {
          //  update role name and return
          boolean complete = super.updateRoleName(name, id);
          return complete;
        }
      }
    }
    // if role name not be updated then return false;
    return false;
  }

  /**
   * Update the name of the user.
   *
   * @param name the new name
   * @param userId the Id of the user
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateUserNameHelper(String name, int userId, Context context) {
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<User> userList = sel.getUsersDetailsHelper();

    // check if name is appropriate (not null or empty)
    boolean nameCheck = goodString(name);
    // check if number is not negative
    boolean userIdCheck = positiveInt(userId);
    // check if user exists in the database
    boolean userIdExist = userExists(userId, context);

    // if inputs are valid then get the user
    if (nameCheck && userIdCheck && userIdExist) {
      for (User user : userList) {
        if (userId == user.getId()) {
          // update user's name
          return super.updateUserName(name, userId);
        }
      }
    }
    // if name could not be updated return false
    return false;
  }

  /**
   * Update the age of the user.
   *
   * @param age the new age
   * @param userId the Id of the user
   * @param context the contect of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateUserAgeHelper(int age, int userId, Context context) {
    // perform check for appropriate values
    boolean ageCheck = age >= 0;
    boolean userIdCheck = positiveInt(userId);
    boolean userIdExist = userExists(userId, context);

    // if inputs valid then check if user exists
    if (ageCheck && userIdCheck && userIdExist) {
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      List<User> userIdlist = sel.getUsersDetailsHelper();
      for (User user : userIdlist) {
        if (userId == user.getId()) {
          // if user exists then update his age with the given age
          return super.updateUserAge(age, userId);
        }
      }
    }
    // if age could not be updated return false
    return false;
  }

  /**
   * Update the Role for a user.
   *
   * @param roleId the Id of the role
   * @param userId the Id of the user
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateUserRoleHelper(int roleId, int userId, Context context) {
    // perform checks
    boolean roleIdCheck = positiveInt(roleId);
    boolean userIdCheck = positiveInt(userId);
    boolean userIdExist = userExists(userId, context);

    // if inputs valid then search for user in database
    if (roleIdCheck && userIdCheck && userIdExist) {
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      List<User> userlist = sel.getUsersDetailsHelper();

      for (User user : userlist) {
        if (userId == user.getId()) {
          // update users role if user found
          return super.updateUserRole(roleId, userId);
        }
      }
    }
    // if user role could not be found then return false
    return false;
  }

  /**
   * Update the name of the item.
   *
   * @param name the name of the item
   * @param itemId the Id of the item
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateItemNameHelper(String name, int itemId, Context context) {
    // perform checks
    boolean nameCheck = goodString(name);
    boolean itemIdCheck = positiveInt(itemId);
    boolean itemExist = itemExists(itemId, context);

    // if inputs valid search get all items in database
    if (nameCheck && itemIdCheck && itemExist) {
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      List<Item> itemidlist = sel.getAllItemsHelper();

      for (Item item : itemidlist) {
        // once item is found then update it's name
        if (itemId == item.getId()) {
          return super.updateItemName(name, itemId);
        }
      }
    }
    // if item could not be updated return false
    return false;
  }

  /**
   * Update the price of the item.
   *
   * @param price the price that is to be set
   * @param itemId the Id of the item
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise.
   */
  public boolean updateItemPriceHelper(BigDecimal price, int itemId, Context context) {
    // perform checks
    boolean priceCheck = (price.doubleValue() > 0);
    boolean itemIdCheck = positiveInt(itemId);
    boolean itemExist = itemExists(itemId, context);

    // if inputs valid then get all items
    if (priceCheck && itemIdCheck && itemExist) {
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      List<Item> itemList = sel.getAllItemsHelper();

      for (Item item : itemList) {
        // once item is found  then update its price
        if (itemId == item.getId()) {
          return super.updateItemPrice(price, itemId);
        }
      }
    }
    // if price not updated return false
    return false;
  }

  /**
   * Update the status of the given account.
   *
   * @param accountId the Id of the account
   * @param active the status of the account
   * @param context the context of the current state of the application
   * @return true if operation was sucessful, false otherwise
   */
  public boolean updateAccountStatus(int accountId, boolean active, Context context) {
    // perform checks
    boolean complete = false;
    boolean accountIdCheck = positiveInt(accountId);
    if (accountIdCheck) {
      // if account id is valid then update the status
      try {
        // try to get the account id
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        Account account = sel.getAccountDetailsHelper(accountId);
        if (account != null) {
          // update account status
          complete = super.updateAccountStatus(accountId, active);
        }
      } catch (Exception e) {
        return false;
      }

    }
    return complete;
  }

  /**
   * Update the password for a user.
   *
   * @param password the new password the user wants
   * @param id the id of the user
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateUserPassword(String password, int id, Context context) {
    // perform checks
    boolean wordCheck = goodString(password);
    boolean userIdCheck = userExists(id, context);
    boolean userExist = userExists(id, context);
    boolean complete = false;

    // if inputs valid then update password
    if (wordCheck && userIdCheck && userExist) {
      complete = super.updateUserPassword(password, id);
    }
    // return if password was updated
    return complete;
  }

  /**
   * Update a Users address.
   *
   * @param address the new address
   * @param userId the Id of the user
   * @param context the context of the current state of the application
   * @return true if operation was successful, false otherwise
   */
  public boolean updateUserAddressHelper(String address, int userId, Context context) {
    // perform check for appropriate values
    boolean addressCheck = goodString(address);
    boolean userIdCheck = positiveInt(userId);
    boolean addressLength = address.length() <= 100;
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    boolean userIdExist = userExists(userId, context);
    List<User> userIdList = sel.getUsersDetailsHelper();

    if (addressCheck == userIdCheck == addressLength == userIdExist == true) {
      for (User user : userIdList) {
        if (userId == user.getId()) {

          boolean complete = super.updateUserAddress(address, userId);
          return complete;
        }
      }
    }
    return false;
  }

  /**
   * check if the user exists in the database.
   *
   * @param userId the user's Id that needs to be checked
   * @return true if the user exists in the database
   */
  private boolean userExists(int userId, Context context) {
    boolean idExists = false;
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<User> userIdList = sel.getUsersDetailsHelper();

    // loop through all users
    for (User user : userIdList) {
      if (user.getId() == userId) {
        idExists = true;
      }
    }
    return idExists;
  }

}


