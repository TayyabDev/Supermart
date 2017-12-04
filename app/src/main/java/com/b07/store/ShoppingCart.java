package com.b07.store;

import android.content.Context;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
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
    private HashMap<Item, Integer> cart;
    private Customer customer;
    private BigDecimal total;
    private static final BigDecimal TAXRATE = new BigDecimal("1.13");
    private boolean hasActiveAccount;
    private int customerAccountId = -1;
    private boolean cartRestored = false;
    private boolean hasInactiveAccount;

    /**
     * Create a shopping cart for a logged in activity_customer
     *
     * @param customer activity_customer logged in.
     * @throws CustomerNotLoggedInException thrown if the activity_customer does not log in.
     * @throws SQLException                 on failure
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
     * . restore the shopping cart with the previous account
     *
     * @throws SQLException               on failure
     * @throws InvalidInputException      on invalid input
     * @throws InvalidIdException         on invalid input id
     * @throws ItemNotFoundException      on invalid item
     * @throws InvalidQuantityException   on invalid quantity
     * @throws DatabaseInsertException    on failure insert
     * @throws CustomerNoAccountException if activity_customer tries to restore cart without an account
     */
    public void restoreShoppingCart()
            throws SQLException, InvalidInputException, InvalidIdException, ItemNotFoundException,
            InvalidQuantityException, DatabaseInsertException, CustomerNoAccountException {
        if (hasInactiveAccount && !cartRestored) {
            cartRestored = true;
            Account customerAccount = DatabaseSelectHelper.getAccountDetails(customerAccountId);
            int current = 0;
            while (current < customerAccount.getItemIds().size()) {
                // get the quantity of item
                Item item = DatabaseSelectHelper.getItem(customerAccount.getItemIds().get(current));
                int quantity = customerAccount.getItemQuantities().get(current);
                if (quantity > 0) {
                    // add item to cart
                    addItem(item, quantity);
                }
                // increment the counter
                current++;
            }
        } else {
            throw new CustomerNoAccountException("You do not have an account!");
        }
    }

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
     * Add the item with the quantity and update the total price.
     *
     * @param item     item to add.
     * @param quantity quantity of the item to add.
     * @throws SQLException             if connection can not be connected with database
     * @throws ItemNotFoundException    if item could not be found
     * @throws InvalidQuantityException if quantity is invalid
     * @throws InvalidInputException    on invalid input
     * @throws DatabaseInsertException  on failure insert
     */
    public void addItem(Item item, int quantity) throws SQLException, ItemNotFoundException,
            InvalidQuantityException, DatabaseInsertException, InvalidInputException {
        // check if the quantity is valid
        if (quantity > 0) {
            boolean itemFound = false;
            for (Item currentItem : DatabaseSelectHelper.getAllItems()) {
                if (currentItem.getName().equals(item.getName())) {
                    itemFound = true;
                    // check if item is already in the cart
                    if (cart.containsKey(currentItem)) {
                        // multiply the price of the item * quantity
                        BigDecimal priceBefore =
                                BigDecimal.valueOf(cart.get(currentItem)).multiply(currentItem.getPrice());

                        // add the quantity of the item to the current quantity
                        cart.replace(currentItem, cart.get(currentItem) + quantity);
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                                .multiply(currentItem.getPrice()).setScale(2);

                        // update the total
                        total = total.add(priceAfter.subtract(priceBefore));
                    } else {
                        // otherwise just add the item normally
                        cart.put(currentItem, quantity);
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                                .multiply(currentItem.getPrice()).setScale(2, BigDecimal.ROUND_UP);

                        // update the total
                        total = total.add(priceAfter).setScale(2, BigDecimal.ROUND_UP);
                    }
                    // exit the loop since we have added the item.
                    break;
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

    public void addItem(Item item, int quantity, Context context) throws SQLException, ItemNotFoundException,
            InvalidQuantityException, DatabaseInsertException, InvalidInputException {
        // check if the quantity is valid
        if (quantity > 0) {
            boolean itemFound = false;
            // check if item is valid
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
            for (Item currentItem : sel.getAllItemsHelper()) {
                if (currentItem.getName().equals(item.getName())) {
                    itemFound = true;
                    // check if item is already in the cart
                    if (cart.containsKey(currentItem)) {
                        // multiply the price of the item * quantity
                        BigDecimal priceBefore =
                                BigDecimal.valueOf(cart.get(currentItem)).multiply(currentItem.getPrice());

                        // add the quantity of the item to the current quantity
                        cart.replace(currentItem, cart.get(currentItem) + quantity);
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                                .multiply(currentItem.getPrice()).setScale(2);

                        // update the total
                        total = total.add(priceAfter.subtract(priceBefore));
                    } else {
                        // otherwise just add the item normally
                        cart.put(currentItem, quantity);
                        System.out.println(cart.get(currentItem));
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                                .multiply(currentItem.getPrice()).setScale(2, BigDecimal.ROUND_UP);

                        // update the total
                        total = total.add(priceAfter).setScale(2, BigDecimal.ROUND_UP);
                    }
                    // exit the loop since we have added the item.
                    break;
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
     * @param item     is the item will be removed.
     * @param quantity the quantity of the item will be removed.
     * @throws ItemNotFoundException    thrown if the item is not found.
     * @throws SQLException             if connection can not connect to database
     * @throws InvalidQuantityException if the quantity entered is invalid
     * @throws InvalidInputException    on invalid input
     * @throws DatabaseInsertException  on failure insert
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
                    BigDecimal priceBefore = BigDecimal.valueOf(cart.get(currentItem)).multiply(item.getPrice());

                    // replace the quantity of the item to the current quantity
                    cart.replace(currentItem, cart.get(currentItem) - quantity);
                    BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem)).multiply(item.getPrice());

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


    public Customer getCustomer() {
        return this.customer;
    }

    public BigDecimal getTotal() {
        // return the total * tax rate
        return this.total.multiply(getTaxRate()).setScale(2, BigDecimal.ROUND_UP);
    }
    public BigDecimal getTotalWithoutTax() {
        // return the total * tax rate
        return this.total;
    }


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

    public void updateAccount() throws SQLException, DatabaseInsertException, InvalidInputException,
            InvalidQuantityException {
        if (hasActiveAccount) {
            for (Item item : this.getItems()) {
                // insert account line
                DatabaseInsertHelper.insertAccountLine(this.customerAccountId, item.getId(),
                        cart.get(item));
            }
        }
    }

    public boolean customerHasActiveAccount(Context context) throws InvalidIdException {
        // search database for customers inactive accounts
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        if(sel.getUserActiveAccountsHelper(customer.getId()).size() > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean customerHasInactiveAccount(Context context) throws InvalidIdException {
        // search database for customers inactive accounts
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        if(sel.getUserInactiveAccountsHelper(customer.getId()).size() > 0){
            return true;
        } else {
            return false;
        }
    }

    private int getCustomerActiveAccountId(Context context) throws InvalidIdException, CustomerNoAccountException {
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

    private int getCustomerInactiveAccountId(Context context) throws InvalidIdException, CustomerNoAccountException {
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



    public boolean updateAccount(Context context) throws SQLException, DatabaseInsertException, InvalidInputException,
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

        } catch (CustomerNoAccountException e){
            // customer has no account so return false
            return false;
        }
    }

        /*
         *

        if (hasActiveAccount) {
            // check if account is active
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
            List<Integer> userActiveAccounts = sel.getUserActiveAccountsHelper(customer.getId());

            if(userActiveAccounts.contains(customerAccountId)){
                DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(context);
                for (Item item : this.getItems()) {
                    // insert account line
                    try {
                        ins.insertAccountLine(customerAccountId, item.getId(), this.getQuantity(item), context);
                    } catch (InvalidIdException e) {
                        // expected if trying to change item quantity
                    }

                }
                // update account status since we are done with it now
                DatabaseAndroidUpdateHelper upd = new DatabaseAndroidUpdateHelper(context);
                upd.updateAccountStatus(customerAccountId, false, context);

            }
            */




  /**
   * Get all the items in the cart.
   * 
   * @return a list of items in the cart.
   */
  public List<Item> getItems() {
    Set<Item> keySet = cart.keySet();
    return new ArrayList<Item>(keySet);
  }

  public int getQuantity(Item item){
    return cart.get(item);
  }

  public HashMap<Item, Integer> getCart(){
      return this.cart;
  }


  /**
   * Take a shopping cart with associated activity_customer then calculate the total after tax and submit to
   * database. If the item are enough to be purchased, update the table and clear the shopping cart.
   * 
   * @param shoppingCart shopping cart activity_customer is using.
   * @return true if the item are enough to be purchased, update the table and clear the shopping
   *         cart, false otherwise.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws DatabaseInsertException thrown if something goes wrong with database insertion.
   * @throws InvalidQuantityException thrown if something goes wrong with the quantity.
   * @throws InvalidInputException thrown if something goes wrong with the input.
   * @throws InvalidRoleException thrown if something goes wrong with the role.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */

  public boolean checkOut(ShoppingCart shoppingCart) throws SQLException, DatabaseInsertException,
      InvalidQuantityException, InvalidInputException, InvalidRoleException, InvalidIdException {
    boolean checkedOut = false;

    // check if activity_customer is logged in
    if (shoppingCart.getCustomer().equals(customer)) {
      boolean sufficientInventory = true;
      // check inventory for all the objects

      for (Item item : cart.keySet()) {
        // if the quantity of current item is less than the users indicated quantity,
        // operation
        // fails.
        if (DatabaseSelectHelper.getInventoryQuantity(item.getId()) < cart.get(item)) {
          sufficientInventory = false;
          return false;
        }
      }
      if (sufficientInventory) {
        // since there is enough inventory, we can proceed with the sale
        for (Item item : cart.keySet()) {
          // insert the sale using the quantity * price
          BigDecimal currentPrice = BigDecimal.valueOf(cart.get(item)).multiply(item.getPrice());
          int saleId = DatabaseInsertHelper.insertSale(customer.getId(), currentPrice);

          // insert the itemized sale now
          DatabaseInsertHelper.insertItemizedSale(saleId, item.getId(), cart.get(item));
          // update the table
          int newQuantity =
              DatabaseSelectHelper.getInventoryQuantity(item.getId()) - cart.get(item);
          DatabaseUpdateHelper.updateInventoryQuantity(newQuantity, item.getId());

          // set checkout to true since we have completed the sales

        }
        checkedOut = true;
        shoppingCart.clearCart();
      }
    }
    return checkedOut;
  }


  public boolean checkOut(ShoppingCart shoppingCart, Context context) throws SQLException, DatabaseInsertException,
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
