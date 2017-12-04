package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
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
    public boolean restoreShoppingCart()
            throws SQLException, InvalidInputException, InvalidIdException, ItemNotFoundException,
            InvalidQuantityException, DatabaseInsertException, CustomerNoAccountException {
    	// clear cart
        this.clearCart();

        // get users most recent inactive account
        if (customerHasInactiveAccount()) {

            try {
                cartRestored = false;
                int customerAccountId = getCustomerInactiveAccountId();

                // get account
                Account account = DatabaseSelectHelper.getAccountDetails(customerAccountId);
                List<Integer> itemIds = account.getItemIds();
                List<Integer> itemQuantities = account.getItemQuantities();
                // fill the cart with the items
                int current = 0;
                while (current < itemIds.size()) {
                    // get item object from database
                    Item item = DatabaseSelectHelper.getItem(itemIds.get(current));
                    int quantity = itemQuantities.get(current);

                    if (quantity > 0) {
                        addItem(item, quantity);
                    }

                    current++;
                }
                cartRestored = true;


            } catch (InvalidIdException e) {
            	e.printStackTrace();
            } catch (CustomerNoAccountException e) {
            	e.printStackTrace();
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
            // check if item is valid
            for (Item currentItem : DatabaseSelectHelper.getAllItems()) {
                if (currentItem.getName().equals(item.getName())) {
                    itemFound = true;
                    // check if item is already in the cart
                    boolean cartContainsItem = false;
                    for(Item cartItem : this.getItems()) {
                      if(cartItem.getId() == item.getId()) {
                        cartContainsItem = true;
                        // multiply the price of the item * quantity
                        BigDecimal priceBefore =
                                BigDecimal.valueOf(cart.get(cartItem)).multiply(cartItem.getPrice());

                        // add the quantity of the item to the current quantity
                        cart.replace(cartItem, cart.get(cartItem) + quantity);
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(cartItem))
                                .multiply(currentItem.getPrice()).setScale(2);

                        // update the total
                        total = total.add(priceAfter.subtract(priceBefore));
                        
                        break;
                      }
                    }
                    if(!cartContainsItem) {
                        // otherwise just add the item normally
                        cart.put(currentItem, quantity);
                        System.out.println(cart.get(currentItem));
                        BigDecimal priceAfter = BigDecimal.valueOf(cart.get(currentItem))
                                .multiply(currentItem.getPrice()).setScale(2, BigDecimal.ROUND_UP);

                        // update the total
                        total = total.add(priceAfter).setScale(2, BigDecimal.ROUND_UP);
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
                if (currentItem.getId() == item.getId()) {
                    // found item
                    itemFound = true;

                    // multiply the price of the item * quantity
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

  

    public boolean customerHasInactiveAccount() throws InvalidIdException, SQLException {
        // search database for customers inactive accounts
        if(DatabaseSelectHelper.getUserInactiveAccounts(customer.getId()).size() > 0){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean customerHasActiveAccount() throws InvalidIdException, SQLException {
        // search database for customers inactive accounts
        if(DatabaseSelectHelper.getUserActiveAccounts(customer.getId()).size() > 0){
            return true;
        } else {
            return false;
        }
    }
    
    private int getCustomerActiveAccountId() throws InvalidIdException, CustomerNoAccountException, SQLException {
        // get customers active accounts
        List<Integer> activeAccountIds = DatabaseSelectHelper.getUserActiveAccounts(customer.getId());
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
    

    private int getCustomerInactiveAccountId() throws InvalidIdException, CustomerNoAccountException, SQLException {
        // get customers active accounts
        List<Integer> inactiveAccountIds = DatabaseSelectHelper.getUserInactiveAccounts(customer.getId());
        if (inactiveAccountIds != null && inactiveAccountIds.size() > 0) {
            // get the first active account in the list
            hasInactiveAccount = true;
            return inactiveAccountIds.get(0);

        } else {
            hasInactiveAccount = false;
            throw new CustomerNoAccountException("Customer does not have any inactive accounts.");
        }

    }
    
    public boolean updateAccount() throws SQLException, DatabaseInsertException, InvalidInputException,
    InvalidQuantityException, InvalidIdException, InvalidRoleException {
		
		// get most recent active account id
		try {
		    customerAccountId = getCustomerActiveAccountId();
		    for (Item item : this.getItems()) {
		        // insert account line
		        DatabaseInsertHelper.insertAccountLine(customerAccountId, item.getId(), this.getQuantity(item));
		    }
		    // update account as inactive and then return true
		    DatabaseUpdateHelper.updateAccountStatus(customerAccountId, false);
		    return true;
		
		} catch (CustomerNoAccountException e){
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

	for (Item item : this.getItems()) {
	  // if the quantity of current item is less than the users indicated quantity,
	  // operation
	  // fails.
	  if (DatabaseSelectHelper.getInventoryQuantity(item.getId()) < cart.get(item)) {
	    sufficientInventory = false;
	    return false;
	  }
	}
	if (sufficientInventory) {
	    int saleId = DatabaseInsertHelper.insertSale(customer.getId(), getTotalWithoutTax());
	  // since there is enough inventory, we can proceed with the sale
	  for (Item item : cart.keySet()) {
	    // insert the sale using the quantity * price
	    BigDecimal currentPrice = BigDecimal.valueOf(cart.get(item)).multiply(item.getPrice());
	
	
	    // insert the itemized sale now
	    DatabaseInsertHelper.insertItemizedSale(saleId, item.getId(), cart.get(item));
	    // update the table
	    int newQuantity =
	            DatabaseSelectHelper.getInventoryQuantity(item.getId()) - cart.get(item);
	    DatabaseUpdateHelper.updateInventoryQuantity(newQuantity, item.getId());
	
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
