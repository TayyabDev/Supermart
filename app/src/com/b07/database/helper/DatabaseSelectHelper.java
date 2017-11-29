package com.b07.database.helper;

import com.b07.database.DatabaseSelector;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseSelectHelper extends DatabaseSelector {

  /**
   * Help get all roles.
   * 
   * @return a list of integer that represent roles' id.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  public static List<Integer> getRoleIds() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getRoles(connection);
    // initialize the list of role ids and loop through the roles result set
    List<Integer> roleIds = new ArrayList<>();
    while (results.next()) {
      // add current id to the list of role ids
      roleIds.add(results.getInt("ID"));
    }
    // close results and connection adn then return list of role ids
    results.close();
    connection.close();
    return roleIds;
  }

  /**
   * Help get role with role id.
   * 
   * @param roleId is the id of the role
   * @return a string that is the name of the role.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidRoleException thrown if the role is invalid.
   */
  public static String getRoleName(int roleId) throws SQLException, InvalidRoleException {
    if (roleId <= DatabaseSelectHelper.getRoleIds().size()) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      String role = DatabaseSelector.getRole(roleId, connection);
      connection.close();
      return role;
    } else {
      throw new InvalidRoleException();
    }
  }

  /**
   * Check if the user id is in the database.
   * 
   * @param userId id of the user.
   * @return true if the user is in database, otherwise false.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  private static boolean checkUserId(int userId) throws SQLException {
    boolean check = false;
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
    // if the users id is not null in the database, user is in database
    try {
      check = (results.getInt("ID")) == userId;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    connection.close();
    results.close();
    return check;
  }

  /**
   * Help get user role id.
   * 
   * @param userId is the id of the user.
   * @return id of role.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static int getUserRoleId(int userId) throws SQLException, InvalidIdException {
    // check if user id is in database
    if (checkUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int roleId = DatabaseSelector.getUserRole(userId, connection);
      connection.close();
      return roleId;
    } else {
      throw new InvalidIdException("User is not in database");
    }
  }

  /**
   * Help get users by their role.
   * 
   * @param roleId id of the role.
   * @return roleId if the id of the role.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static List<Integer> getUsersByRole(int roleId)
      throws SQLException, InvalidInputException, InvalidIdException, InvalidRoleException {
    List<User> userList = DatabaseSelectHelper.getUsersDetails();
    // check if the userId is valid through reviewing all users stored in database
    for (User user : userList) {
      if (user.getRoleId() == roleId) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection);
        List<Integer> userIds = new ArrayList<>();
        while (results.next()) {
          userIds.add(results.getInt("USERID"));
          results.close();
          connection.close();
          return userIds;
        }
      } else {
        // throw an InvalidInputException if the userId is not found in all users.
        throw new InvalidInputException("This is an invalid userId");
      }
    }
    // if the userlist is empty, return null
    return null;
  }

  /**
   * Help to get all details of user.
   * 
   * @return a list of Users with all details.
   * @throws InvalidIdException thrown if id is invalid.
   * @throws InvalidRoleException thrown if the role is invalid.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  public static List<User> getUsersDetails()
      throws InvalidRoleException, InvalidIdException, SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    List<User> users = new ArrayList<>();
    results = DatabaseSelector.getUsersDetails(connection);
    while (results.next()) {
      int id = results.getInt("ID");
      String name = results.getString("NAME");
      int age = results.getInt("AGE");
      String address = results.getString("ADDRESS");
      User newUser = createUser(id, name, age, address, DatabaseSelectHelper.getUserRoleId(id));
      users.add(newUser);
    }
    results.close();
    connection.close();
    return users;
  }

  /**
   * Help to get all the details about a given user.
   * 
   * @param userId is the id of the user.
   * @return a user type object.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static User getUserDetails(int userId)
      throws SQLException, InvalidRoleException, InvalidIdException {
    // check if user is in database
    if (checkUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
      // initialize the users info
      int id = -1;
      String name = "";
      int age = -1;
      String address = "";
      // get the users info
      while (results.next()) {
        id = results.getInt("ID");
        name = results.getString("NAME");
        age = results.getInt("AGE");
        address = results.getString("ADDRESS");
      }
      results.close();
      connection.close();
      // return the user formed by his information
      int roleId = getUserRoleId(userId);
      return createUser(id, name, age, address, roleId);
    } else {
      throw new InvalidIdException("User is not in database");
    }
  }

  /**
   * A user constructor.
   * 
   * @param id of the user.
   * @param name of the user.
   * @param age of the user.
   * @param address of the user.
   * @param roleId of the user.
   * @return a user type object.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidRoleException thrown if the role is invalid.
   */
  private static User createUser(int id, String name, int age, String address, int roleId)
      throws SQLException, InvalidRoleException {
    // initialize the user
    User user = null;
    // get the role name using the id
    String roleName = getRoleName(roleId);
    // if role is admin create an admin
    if (roleName.equals(Roles.ADMIN.toString())) {
      user = new Admin(id, name, age, address);
    } else if (roleName.equals(Roles.CUSTOMER.toString())) {
      user = new Customer(id, name, age, address);
    }
    return user;
  }

  /**
   * Help to get password.
   * 
   * @param userId the id of the user.
   * @return the hashed password.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static String getPassword(int userId) throws SQLException, InvalidIdException {
    if (checkUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      String password = DatabaseSelector.getPassword(userId, connection);
      connection.close();
      return password;
    } else {
      throw new InvalidIdException("User is not in database");
    }
  }

  /**
   * Help to get all items.
   * 
   * @return a list of items.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  public static List<Item> getAllItems() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAllItems(connection);
    // initialize list of items to return
    List<Item> items = new ArrayList<>();
    while (results.next()) {
      // get the information about the current item and create an item about with it
      int id = results.getInt("ID");
      String name = results.getString("NAME");
      BigDecimal price = (new BigDecimal(results.getString("PRICE")));
      Item item = new ItemImpl();
      item.setId(id);
      item.setName(name);
      item.setPrice(price);
      // add the current item to the list of items
      items.add(item);
    }
    results.close();
    connection.close();
    return items;
  }

  /**
   * Help to get item.
   * 
   * @param itemId the id of the item.
   * @return a Item type object.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static Item getItem(int itemId) throws SQLException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItem(itemId, connection);
    // initialize the id, name, and price of the object
    int id = 0;
    String name = null;
    BigDecimal price = null;
    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      price = new BigDecimal(results.getString("PRICE"));
    }
    results.close();
    connection.close();
    // if id is greater than 0, the item exists in the table
    if (id > 0) {
      // return the item with the id, price, and name
      Item a = new ItemImpl();
      a.setId(id);
      a.setPrice(price);
      a.setName(name);
      return a;
    } else {
      // item does not exist in table, throw invalid id exception
      throw new InvalidIdException("Item is not in database");
    }
  }

  /**
   * Help to get the inventory.
   * 
   * @return a Inventory type object.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws NumberFormatException thrown if the format of number is wrong.
   * @throws InvalidIdException thrown if something is invalid.
   */
  public static Inventory getInventory()
      throws SQLException, NumberFormatException, InvalidIdException, InventoryFullException {
    // initialize an inventory
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getInventory(connection);
    Inventory inventory = new InventoryImpl();
    while (results.next()) {
      // update the item map with the current table data
      inventory.updateItemMap(getItem(results.getInt("ITEMID")),
          Integer.parseInt(results.getString("QUANTITY")));
    }
    results.close();
    connection.close();
    return inventory;
  }

  /**
   * Help to get the quantity of the item with item id.
   * 
   * @param itemId the id of the item.
   * @return the quantity of the item.
   * @throws SQLException thrown if something goes wrong with the query.
   */
  public static int getInventoryQuantity(int itemId) throws SQLException {
    // initialize quantity and search inventory for it 
    int quantity = -1;
    List<Item> itemlist = DatabaseSelectHelper.getAllItems();
    for (Item item : itemlist) {
      if (item.getId() == itemId) {
        // connect to database and get the inventory quantity once we have found the item
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
        connection.close();
        return quantity;
      }
    }
    return quantity;
  }

  /**
   * Help to get sales.
   * 
   * @return a salelog object.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public static SalesLog getSales() throws InvalidRoleException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    try {
      // get result set and initalize a saleslog to return
      results = DatabaseSelector.getSales(connection);
      SalesLog salesLog = new SalesLogImpl();
      while (results.next()) {
        // add the sales to the saleslog
        int id = results.getInt("ID");
        Sale sale = DatabaseSelectHelper.getSaleById(id);
        salesLog.addSale(sale);
      }
      // return sales log, close connection and result set
      return salesLog;
    } catch (SQLException e) {
      System.out.println("Select error:");
      System.out.println("Please check UserId and totalPrice");
      System.err.println(e.toString());
    } finally {
      try {
        results.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
    }
    return null;
  }

  /**
   * Help to get sale by sale id.
   * 
   * @param saleId the id of sale.
   * @return a sale object.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public static Sale getSaleById(int saleId) throws InvalidRoleException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    try {
      // get the result set with sale by id
      results = DatabaseSelector.getSaleById(saleId, connection);
      while (results.next()) {
        // create a sale with the id, user, and total price from the table
        int id = results.getInt("ID");
        int userId = results.getInt("USERID");
        BigDecimal price = new BigDecimal(results.getString("TOTALPRICE"));
        Sale sale = new SaleImpl();
        sale.setId(id);
        sale.setUser(DatabaseSelectHelper.getUserDetails(userId));
        sale.setTotalPrice(price);
        
        // return the sale
        return sale;
      }
      // close results and connection
    } catch (SQLException e) {
      System.err.println(e.toString());
    } finally {
      try {
        results.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
    }
    return null;
  }

  /**
   * Help get sales to user.
   * 
   * @param userId is the id of the user.
   * @return a list of sales.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public static List<Sale> getSalesToUser(int userId)
      throws InvalidRoleException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    
    // initialize sales array list
    List<Sale> sales = new ArrayList<>();
    try {
      // get the sales to user result set
      results = DatabaseSelectHelper.getSalesToUser(userId, connection);
      while (results.next()) {
        
        // create a sale with the information found from the talble
        int id = results.getInt("ID");
        int usersId = results.getInt("USERID");
        BigDecimal price = new BigDecimal(results.getString("TOTALPRICE"));
        Sale sale = new SaleImpl();
        sale.setId(id);
        sale.setUser(DatabaseSelectHelper.getUserDetails(usersId));
        sale.setTotalPrice(price);
        
        // add sale to the list of sales
        sales.add(sale);
      }
      
      // close result and connection, then return sale
    } catch (SQLException e) {
      System.err.println(e.toString());
    } finally {
      try {
        results.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
    }
    return sales;
  }

  /**
   * Help to get itemsized sale.
   * 
   * @param saleId is the id of the sale.
   * @param sale is the sale.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public static void getItemizedSaleById(int saleId, Sale sale) throws InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    try {
      // get the itemized sale result set
      results = DatabaseSelector.getItemizedSaleById(saleId, connection);
      while (results.next()) {
        // set the sale id and place the item and quantiy into its item map
        sale.setId(results.getInt("SALEID"));
        HashMap<Item, Integer> itemMap = new HashMap<>();
        itemMap.put(DatabaseSelectHelper.getItem(results.getInt("ITEMID")),
            new Integer(results.getInt("QUANTITY")));
        sale.setItemMap(itemMap);
      }
      // catch SQL exception
    } catch (SQLException e) {
      System.err.println(e.toString());
    } finally {
      try {
        results.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
    }
  }

  /**
   * Help to get itemsized sales.
   * 
   * @param salesLog is the salelog object.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public static void getItemizedSales(SalesLog salesLog)
      throws InvalidIdException, InvalidRoleException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = null;
    try {
      // get the itemized sale result set and loop through it
      results = DatabaseSelector.getItemizedSales(connection);
      while (results.next()) {
        // create an itemized sale and initialize it using get itemized sale by id method
        ItemizedSaleImpl sale = new ItemizedSaleImpl();
        int saleId = results.getInt("SALEID");
        DatabaseSelectHelper.getItemizedSaleById(saleId, sale);
        
        // add the itemized sale to the sales log
        salesLog.addSale(sale);
      }
    } catch (SQLException e) {
      System.err.println(e.toString());
    } finally {
      
      // close results and connection
      try {
        results.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(e.toString());
      }
    }
  }

  /**
 * returnt all the accountIds associated with the given customer.
 * @param userId of the customer
 * @return a list of integer that contains all accountIds that the user have
 * @throws SQLException on failure
 */
  public static List<Integer> getUserAccounts(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserAccounts(userId, connection);
    // initialize the list of account ids and loop through the roles result set
    List<Integer> accountIds = new ArrayList<>();
    while (results.next()) {
      // add current account id to list of account ids
      accountIds.add(results.getInt("ID"));
    }
    // close results and connection and then return list of users account ids
    results.close();
    connection.close();
    return accountIds;
  }

  /**
 * return the account details for a given accountId.
 * @param accountId of the customer
 * @return the account with details
 * @throws SQLException on failure
 * @throws InvalidInputException on invalid input
 */
  public static Account getAccountDetails(int accountId)
      throws SQLException, InvalidInputException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();

    ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
    List<Integer> itemIdList = new ArrayList<Integer>();
    List<Integer> quantityList = new ArrayList<Integer>();
    // get the users info
    while (results.next()) {
      itemIdList.add(results.getInt("ITEMID"));
      quantityList.add(results.getInt("QUANTITY"));
    }
    results.close();
    connection.close();
    // return the user formed by his information
    return new Account(accountId, itemIdList, quantityList);


  }


}

