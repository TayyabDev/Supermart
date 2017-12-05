package com.b07.store;

import android.content.Context;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidUpdateHelper;
import com.b07.exceptions.CustomerNoAccountException;
import com.b07.exceptions.CustomerNotLoggedInException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.ItemNotFoundException;
import com.b07.inventory.Item;
import com.b07.users.Account;
import com.b07.users.Customer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ShoppingCart {

  private static final BigDecimal TAXRATE = new BigDecimal("1.13");
  private HashMap<Item, Integer> cart;
  private Customer customer;
  private BigDecimal total;
  private boolean hasActiveAccount;
  private int customerAccountId = -1;
  private boolean cartRestored = false;
  private boolean hasInactiveAccount;

  /**
   * Create a shopping cart for a logged in activity_customer
   *
   * @param customer activity_customer logged in.
   * @throws CustomerNotLoggedInException thrown if the activity_customer does not log in.
   * @throws SQLException on failure
   */
  public ShoppingCart(Customer customer) throws CustomerNotLoggedInException {
    // initialize the tax rate and activity_customer
    this.customer = customer;

    cart = new HashMap<Item, Integer>();
    total = new BigDecimal("0.00");

    // if customer not logged in throw exception
    if (customer == null) {
      throw new CustomerNotLoggedInException("Customer not logged in");
    }
  }

  /**
   * Restore the most recent session for a customer
   *
   * @param context the context of the state of the application
   * @return True if operation was successful, false otherwise.
   * @throws InvalidIdException If an invalid Id error occurs.
   */
  public boolean restoreShoppingCart(Context context) throws InvalidIdException {
    // clear cart
    this.clearCart();

    // get users most recent inactive account
    if (customerHasInactiveAccount(context)) {

      try {
        cartRestored = false;
        int customerAccountId = getCustomerInactiveAccountId(context);

        // get account
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        Account account = sel.getAccountDetailsHelper(customerAccountId);
        List<Integer> itemIds = account.getItemIds();
        List<Integer> itemQuantities = account.getItemQuantities();
        // fill the cart with the items
        int current = 0;
        while (current < itemIds.size()) {
          // get item object from database
          Item item = sel.getItemHelper(itemIds.get(current));
          int quantity = itemQuantities.get(current);

          if (quantity > 0) {
            addItem(item, quantity, context);
          }

          current++;
        }
        cartRestored = true;


      } catch (InvalidIdException e) {
        return false;
      } catch (CustomerNoAccountException e) {
        return false;
      } catch (ItemNotFoundException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (InvalidQuantityException e) {
        e.printStackTrace();
      } catch (DatabaseInsertException e) {
        e.printStackTrace();
      } catch (InvalidInputException e) {
        e.printStackTrace();
      }
      return cartRestored;
    }
    return cartRestored;
  }


  /**
   * Add an item to the cart
   *
   * @param item item in the store
   * @param quantity the amount of the item
   * @param context the context of the state of the application
   * @throws SQLException if an SQL error occurs
   * @throws ItemNotFoundException If an item is not found
   * @throws InvalidQuantityException If and invalid quantity  entered
   * @throws DatabaseInsertException If a database insert error occurs
   * @throws InvalidInputException if an Invalid input error occurs.
   */
  public void addItem(Item item, int quantity, Context context)
      throws SQLException, ItemNotFoundException,
      InvalidQuantityException, DatabaseInsertException, InvalidInputException {
    // check if the quantity is valid
    if (quantity > 0) {
      boolean itemFound = false;
      // check if item is valid
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      for (Item currentItem : sel.getAllItemsHelper()) {
        if (currentItem.getId() == item.getId()) {
          itemFound = true;
          boolean itemInCart = false;
          for (Item cartItem : this.getItems()) {
            // check if item is already in the cart
            if (cartItem.getId() == item.getId()) {
              itemInCart = true;
              // multiply the price of the item * quantity
              BigDecimal priceBefore =
                  BigDecimal.valueOf(cart.get(cartItem)).multiply(cartItem.getPrice());

              // add the quantity of the item to the current quantity
              cart.replace(cartItem, cart.get(cartItem) + quantity);
              BigDecimal priceAfter = BigDecimal.valueOf(cart.get(cartItem))
                  .multiply(cartItem.getPrice()).setScale(2);

              // update the total
              total = total.add(priceAfter.subtract(priceBefore));
              // exit the loop since we have added the item.
              break;
            }
          }
          if (!itemInCart) {
            // otherwise just add the item normally
            cart.put(currentItem, quantity);
            System.out.println(cart.get(currentItem));
            BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                .multiply(currentItem.getPrice()).setScale(2, BigDecimal.ROUND_UP);

            // update the total
            total = total.add(priceAfter).setScale(2, BigDecimal.ROUND_UP);
            break;
          }
        }

      }
      if (!itemFound) {
        throw new ItemNotFoundException("The item could not be found!");
      }

    } else {
      // if quantity is invalid throw an exception
      throw new InvalidQuantityException("The quantity entered is invalid!");
    }
  }


  /**
   * Remove the quantity given of the item from items. If the number becomes zero, remove it
   * entirely from the items list, and then update the total.
   *
   * @param item is the item will be removed.
   * @param quantity the quantity of the item will be removed.
   * @throws ItemNotFoundException thrown if the item is not found.
   * @throws SQLException if connection can not connect to database
   * @throws InvalidQuantityException if the quantity entered is invalid
   * @throws InvalidInputException on invalid input
   * @throws DatabaseInsertException on failure insert
   */
  public void removeItem(Item item, int quantity) throws ItemNotFoundException, SQLException,
      InvalidQuantityException, DatabaseInsertException, InvalidInputException {
    // check if the quantity is valid
    if (quantity >= 0) {
      boolean itemFound = false;
      for (Item currentItem : this.getItems()) {
        if (currentItem.getName().equals(item.getName())) {
          // found item
          itemFound = true;

          // multiply the price of the item * quantity
          System.out.println(cart.get(item));
          BigDecimal priceBefore = BigDecimal.valueOf(cart.get(currentItem))
              .multiply(item.getPrice());

          // replace the quantity of the item to the current quantity
          cart.replace(currentItem, cart.get(currentItem) - quantity);
          BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
              .multiply(item.getPrice());

          if (cart.get(currentItem) == 0) {
            // if item quantity 0 or less remove it from the items
            cart.remove(currentItem);
            total = total.subtract(priceBefore);
          } else {
            // update the total
            total = total.add(priceAfter.subtract(priceBefore));
          }
          // exit the loop because we have removed item from cart
          break;
        }

      }
      if (!itemFound) {
        // if the item could not be found throw an exception
        throw new ItemNotFoundException("The item was not found!");
      }


    } else {
      // if quantity is invalid throw exception
      throw new InvalidQuantityException("The quantity entered is invalid!");
    }
  }


  /**
   * Get the customer of the cart
   *
   * @return return the customer using the cart
   */
  public Customer getCustomer() {
    return this.customer;
  }

  /**
   * get the total of the cart
   *
   * @return total of the cart
   */
  public BigDecimal getTotal() {
    // return the total * tax rate
    return this.total.multiply(getTaxRate()).setScale(2, BigDecimal.ROUND_UP);
  }

  /**
   * the total of the cart without tax
   *
   * @return total without tax
   */
  public BigDecimal getTotalWithoutTax() {
    // return the total * tax rate
    return this.total;
  }


  /**
   * get the taxrate
   *
   * @return taxrate
   */
  public BigDecimal getTaxRate() {
    return ShoppingCart.TAXRATE;
  }

  /**
   * Remove all the items in the cart.
   */

  public void clearCart() {
    // clear cart and update total
    cart.clear();
    total = new BigDecimal("0.00");
  }


  /**
   * Check if customer has an active account.
   *
   * @param context the context of the state of the application
   * @return true if there is an active account, false otherwise.
   */
  public boolean customerHasActiveAccount(Context context) throws InvalidIdException {
    // search database for customers inactive accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    if (sel.getUserActiveAccountsHelper(customer.getId()).size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * check if customer has an inactive account
   *
   * @param context the context of the state of the application
   * @return true if a inactive account exists, falase otherwise
   * @throws InvalidIdException if an invalid id error occurs.
   */
  public boolean customerHasInactiveAccount(Context context) throws InvalidIdException {
    // search database for customers inactive accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    if (sel.getUserInactiveAccountsHelper(customer.getId()).size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * get the customer's active account.
   *
   * @param context the contetext of the state of the application.
   * @return the id of the active account.
   * @throws InvalidIdException if an invalid id error occurs.
   * @throws CustomerNoAccountException if the customer does not have an account.
   */
  private int getCustomerActiveAccountId(Context context)
      throws InvalidIdException, CustomerNoAccountException {
    // get customers active accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<Integer> activeAccountIds = sel.getUserActiveAccountsHelper(customer.getId());
    if (activeAccountIds != null && activeAccountIds.size() > 0) {
      // get the first active account in the list
      hasActiveAccount = true;
      customerAccountId = activeAccountIds.get(0);
      return activeAccountIds.get(0);

    } else {
      hasActiveAccount = false;
      throw new CustomerNoAccountException("Customer does not have any active accounts.");
    }

  }

  /**
   * get the customer's inactive account
   *
   * @param context the context of the state of the application
   * @return the id of the inactive account
   * @throws InvalidIdException if an invalid id error occurs
   * @throws CustomerNoAccountException if customer does not have an account
   */
  private int getCustomerInactiveAccountId(Context context)
      throws InvalidIdException, CustomerNoAccountException {
    // get customers active accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
    List<Integer> inactiveAccountIds = sel.getUserInactiveAccountsHelper(customer.getId());
    if (inactiveAccountIds != null && inactiveAccountIds.size() > 0) {
      // get the first active account in the list
      hasInactiveAccount = true;
      return inactiveAccountIds.get(0);

    } else {
      hasInactiveAccount = false;
      throw new CustomerNoAccountException("Customer does not have any inactive accounts.");
    }

  }


  /**
   * update the account with information about the cart
   *
   * @param context the context of the state of the application
   * @return true if operation was successful, false otherwise
   * @throws SQLException if SQL error occurs
   * @throws DatabaseInsertException if database insert error occurs
   * @throws InvalidInputException if and invalid input error occurs
   * @throws InvalidQuantityException if an invalid quantity is enterede
   * @throws InvalidIdException if an invalid id error occurs
   */
  public boolean updateAccount(Context context)
      throws SQLException, DatabaseInsertException, InvalidInputException,
      InvalidQuantityException, InvalidIdException {

    // get most recent active account id
    try {
      customerAccountId = getCustomerActiveAccountId(context);
      DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);
      for (Item item : this.getItems()) {
        // insert account line
        ins.insertAccountLine(customerAccountId, item.getId(), this.getQuantity(item), context);
      }
      // update account as inactive and then return true
      DatabaseAndroidUpdateHelper upd = new DatabaseAndroidUpdateHelper(context);
      upd.updateAccountStatus(customerAccountId, false, context);
      return true;

    } catch (CustomerNoAccountException e) {
      // customer has no account so return false
      return false;
    }
  }

  /**
   * Get all the items in the cart.
   *
   * @return a list of items in the cart.
   */
  public List<Item> getItems() {
    Set<Item> keySet = cart.keySet();
    return new ArrayList<Item>(keySet);
  }

  /**
   * get the quantity of the item in the cart
   *
   * @param item the item in the cart
   * @return the quantity of the item
   */
  public int getQuantity(Item item) {
    return cart.get(item);
  }

  /**
   * Get everything in the cart
   *
   * @return a HashMap with the item and quantity
   */
  public HashMap<Item, Integer> getCart() {
    return this.cart;
  }


  /**
   * Perform a checkout of the shopping cart
   *
   * @param shoppingCart the shopping cart that holds all the items
   * @param context the context of the state of the application
   * @return true if operation was successful, false otherwise.
   * @throws SQLException if SQL error occurs
   * @throws DatabaseInsertException if Database insert error occurs
   * @throws InvalidQuantityException if an invalid quantity is enetered
   * @throws InvalidInputException if an invalid input is entered
   * @throws InvalidRoleException if an invalid role is used
   * @throws InvalidIdException if an  invalid id is used
   */
  public boolean checkOut(ShoppingCart shoppingCart, Context context)
      throws SQLException, DatabaseInsertException,
      InvalidQuantityException, InvalidInputException, InvalidRoleException, InvalidIdException {
    boolean checkedOut = false;

    // check if activity_customer is logged in
    if (shoppingCart.getCustomer().equals(customer)) {
      boolean sufficientInventory = true;
      // check inventory for all the objects
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      for (Item item : this.getItems()) {
        // if the quantity of current item is less than the users indicated quantity,
        // operation
        // fails.
        if (sel.getInventoryQuantity(item.getId()) < cart.get(item)) {
          sufficientInventory = false;
          return false;
        }
      }
      if (sufficientInventory) {
        DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);
        DatabaseAndroidUpdateHelper upd = new DatabaseAndroidUpdateHelper(context);

        int saleId = (int) ins.insertSale(customer.getId(), getTotalWithoutTax(), context);
        // since there is enough inventory, we can proceed with the sale
        for (Item item : cart.keySet()) {
          // insert the sale using the quantity * price
          BigDecimal currentPrice = BigDecimal.valueOf(cart.get(item)).multiply(item.getPrice());

          // insert the itemized sale now
          ins.insertItemizedSale(saleId, item.getId(), cart.get(item), context);
          // update the table
          int newQuantity =
              sel.getInventoryQuantity(item.getId()) - cart.get(item);
          upd.updateInventoryQuantity(newQuantity, item.getId(), context);

          // set checkout to true since we have completed the sales

        }
        // clear cart and exit
        checkedOut = true;
        shoppingCart.clearCart();
      }
    }
    return checkedOut;
  }


}
