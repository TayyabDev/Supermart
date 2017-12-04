package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.inventory.Item;
import com.b07.users.Account;
import com.b07.users.Admin;
import com.b07.users.Customer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * @param argv unused.
   */
  public static void main(String[] argv) {

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("NOOO");
    }
    try {
      // create the buffered reader
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      // check wats in argv
      if (argv.length >= 0) {

        if (argv.length > 0 && argv[0].equals("-1")) {
          DatabaseDriverExtender.initialize(connection);

          // get the name,age, and address of the activity_admin
          System.out.println("Input your name");
          String name = br.readLine();
          System.out.println("Input your age");
          int age = Integer.parseInt(br.readLine());
          System.out.println("Input your address");
          String address = br.readLine();
          System.out.println("Enter the password you would like to use");
          String password = br.readLine();

          // insert the role of activity_admin into the dataabase and get the roleid
          int adminRoleId = DatabaseInsertHelper.insertRole("ADMIN");

          // insert the activity_admin into the database and get the id
          int adminId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
          // establish the activity_admin relationsihp

          DatabaseInsertHelper.insertUserRole(adminId, adminRoleId);

          // store the activity_admin account
          Admin admin = new Admin(adminId, name, age, address, true);

          System.out.println("Your activity_admin ID is: " + admin.getId());

          // insert activity_customer into the roleid's
          DatabaseInsertHelper.insertRole("CUSTOMER");

          // now populate the database with items and get the item id's
          int fish = DatabaseInsertHelper.insertItem("FISHING_ROD", new BigDecimal("450.00"));
          int hockey = DatabaseInsertHelper.insertItem("HOCKEY_STICK", new BigDecimal("80.00"));
          int skates = DatabaseInsertHelper.insertItem("SKATES", new BigDecimal("200.00"));
          int running = DatabaseInsertHelper.insertItem("RUNNING_SHOES", new BigDecimal("120.00"));
          int protein = DatabaseInsertHelper.insertItem("PROTEIN_BAR", new BigDecimal("5.00"));

          // insert the inventories using the item id's and quantity of 3
          DatabaseInsertHelper.insertInventory(fish, 3);
          DatabaseInsertHelper.insertInventory(hockey, 3);
          DatabaseInsertHelper.insertInventory(skates, 3);
          DatabaseInsertHelper.insertInventory(running, 3);
          DatabaseInsertHelper.insertInventory(protein, 3);



        } else {
          // list the options
          System.out.println("Please choose from one of the following options:");
          System.out.println("1 - AdminActivity Login");
          System.out.println("2 - CustomerActivity Login");
          System.out.println("0 - Exit");
          int choice = Integer.parseInt(br.readLine());

          // if user doesnt select valid option keep prompting
          while (!(choice == 1 || choice == 2 || choice == 0)) {
            System.out.println("Please select a valid option.");
            choice = Integer.parseInt(br.readLine());
          }

          if (choice == 1) {
            // ask user for id and password
            System.out.println("Welcome to the AdminActivity Login!");
            System.out.println("Please enter your id");
            int id = Integer.parseInt(br.readLine());
            System.out.println("Please enter your password");
            String password = br.readLine();

            // check if id is for an AdminActivity
            if (DatabaseSelectHelper.getRoleName(DatabaseSelectHelper.getUserRoleId(id))
                .equals("ADMIN")) {
              // check if password is correct
              Admin admin = (Admin) DatabaseSelectHelper.getUserDetails(id);
              if (admin.authenticate(password)) {
                // if password was correct then we can access the AdminActivity interface
                AdminInterface adminInterface =
                    new AdminInterface(admin, DatabaseSelectHelper.getInventory());
                System.out.println("Login successful! Welcome " + admin.getName()
                    + ", please choose from one of the following options:");
                System.out.println("1. Authenticate new activity_admin");
                System.out.println("2. Make new activity_customer");
                System.out.println("3. Make new account");
                System.out.println("4. Make new activity_admin");
                System.out.println("5. Restock inventory");
                System.out.println("6. View the sales");
                System.out.println("7. Exit");

                int adminInterfaceChoice = 0;
                // keep looping until AdminActivity wants to exit the AdminActivity portal
                while (adminInterfaceChoice != 7) {
                  adminInterfaceChoice = Integer.parseInt(br.readLine());
                  // user wants to authenticate new AdminActivity
                  if (adminInterfaceChoice == 1) {
                    // ask for id and password for the new AdminActivity
                    System.out.println("What is the Admins id");
                    int authId = Integer.parseInt(br.readLine());

                    System.out.println("What is the Admins password");
                    String authPassword = br.readLine();

                    // authenticate the new AdminActivity if possible
                    if (DatabaseSelectHelper.getRoleName(DatabaseSelectHelper.getUserRoleId(authId))
                        .equals("AdminActivity")) {
                      Admin authAdmin = (Admin) DatabaseSelectHelper.getUserDetails(id);
                      if (authAdmin.authenticate(authPassword)) {
                        System.out.println("AdminActivity authenticated.");
                      } else {
                        System.out.println("Invalid password");
                      }
                    } else {
                      System.out.println("Not an AdminActivity");
                    }
                  } else if (adminInterfaceChoice == 2) {
                    // ask for new activity_customer info
                    System.out.println("What is the name of the activity_customer");
                    String newName = br.readLine();
                    System.out.println("What is the age of the activity_customer");
                    int newAge = Integer.parseInt(br.readLine());
                    System.out.println("What is the address of the activity_customer");
                    String newAddress = br.readLine();
                    System.out.println("Enter a password");
                    String newPassword = br.readLine();

                    // now create the activity_customer
                    int newId =
                        adminInterface.createCustomer(newName, newAge, newAddress, newPassword);

                    System.out.println("New activity_customer created, with id of " + newId);
                  } else if (adminInterfaceChoice == 3) {
                    System.out.println("What is the activity_customer's id?");
                    int customerId = Integer.parseInt(br.readLine());

                    // try creating account for the activity_customer
                    try {
                      Account customerAccount = adminInterface.createAccount(customerId);
                      System.out.println("The new account ID is " + customerAccount.getId());
                    } catch (InvalidIdException e) {
                      System.out.println("The ID you entered is invalid!");
                    }

                  } else if (adminInterfaceChoice == 4) {
                    System.out.println("What is the name of the AdminActivity");
                    String newName = br.readLine();
                    System.out.println("What is the age of the AdminActivity");
                    int newAge = Integer.parseInt(br.readLine());
                    System.out.println("What is the address of the AdminActivity");
                    String newAddress = br.readLine();
                    System.out.println("Enter a password");
                    String newPassword = br.readLine();

                    // now create the AdminActivity
                    int newId =
                        adminInterface.createAdmin(newName, newAge, newAddress, newPassword);
                    System.out.println("New AdminActivity created with id " + newId);
                  } else if (adminInterfaceChoice == 5) {
                    // add skates and hockey sticks to the database for purpose of restocking

                    System.out.println("Enter the item name you want to restock");
                    String itemName = br.readLine();
                    // get all the items in the inventory
                    List<Item> items = DatabaseSelectHelper.getAllItems();
                    for (Item item : items) {
                      if (item.getName().equals(itemName)) {
                        // once we have found the item we can ask user for the new quantity
                        System.out.println("What is the new quantity");
                        int newQuantity = Integer.parseInt(br.readLine());

                        // restock inventory with new quantity
                        adminInterface.restockInventory(item, newQuantity);
                        System.out
                            .println("There are " + newQuantity + " " + item.getName() + " now.");
                        // end loop
                        break;
                      }
                    }
                  } else if (adminInterfaceChoice == 6) {
                    // get the sales string
                    System.out.println(adminInterface.viewBooks());
                  }
                }

              } else {
                // user entered wrong passwoird
                System.out.println("Wrong password. Try again.");
              }
            } else {
              // user not AdminActivity
              System.out.println("Not an AdminActivity.");
            }

          } else if (choice == 2) {
            System.out.println("Welcome to the activity_customer portal!");
            boolean validCustomer = false;

            while (!validCustomer) {
              System.out.println("Please enter your ID");
              int id = Integer.parseInt(br.readLine());
              System.out.println("Please enter your password");
              String password = br.readLine();
              // check if user is activity_customer
              if (DatabaseSelectHelper.getRoleName(DatabaseSelectHelper.getUserRoleId(id))
                  .equals("CUSTOMER")) {
                // check if password is correct
                Customer customer = (Customer) DatabaseSelectHelper.getUserDetails(id);
                if (customer.authenticate(password)) {
                  // if password correct then open activity_customer menu
                  validCustomer = true;
                  System.out.println("Login succesful! Welcome " + customer.getName() + "!");
                  System.out.println(
                      "If you want to restore previous cart, enter yes, otherwise enter anything");
                  String restoreChoice = br.readLine();
                  ShoppingCart sc = new ShoppingCart(customer);
                  if (restoreChoice.equals("yes")) {
                    sc.restoreShoppingCart();
                  }

                  System.out.println("Please select one of the following options");
                  System.out.println("1. List current items in cart");
                  System.out.println("2. Add a quantity of an item to the cart");
                  System.out.println("3. Check total price of items in the cart");
                  System.out.println("4. Remove a quantity of an item from the cart");
                  System.out.println("5. check out");
                  System.out.println("6. Exit");

                  int customerChoice = -1;
                  // keep looping until activity_customer wants to exit (by entering 6)
                  while (customerChoice != 6) {
                    customerChoice = Integer.parseInt(br.readLine());

                    if (customerChoice == 1) {
                      // print the items in cart
                      List<Item> itemsList = sc.getItems();
                      System.out.println("Here are the items in your cart");
                      for (Item item : itemsList) {
                        System.out.println(item.getName());
                      }

                    } else if (customerChoice == 2) {
                      // ask for item name and quantity
                      System.out.println("Enter the item name");
                      String itemName = br.readLine();
                      System.out.println("Enter the quantity you want to add");
                      int quantity = Integer.parseInt(br.readLine());

                      // add the item and quantity to the cart
                      for (Item item : DatabaseSelectHelper.getAllItems()) {
                        if (item.getName().equals(itemName)) {
                          try {
                            sc.addItem(item, quantity);
                            System.out.println("Item " + item.getName() + " of quantity " + quantity
                                + " was added.");
                          } catch (InvalidQuantityException e) {
                            System.out.println("The quantity you entered is invalid.");
                          }
                          break;
                        }
                      }
                    } else if (customerChoice == 3) {
                      // state the total value of the items in the cart
                      System.out.println("The total is " + sc.getTotal());
                    } else if (customerChoice == 4) {
                      // ask user for the item name and quantity they want to remove
                      System.out.println("Enter the item name");
                      String itemName = br.readLine();

                      System.out.println("Enter the quantity you want to remove");
                      int quantity = Integer.parseInt(br.readLine());

                      // remove the item and quantity from the cart
                      for (Item item : sc.getItems()) {
                        if (item.getName().equals(itemName)) {
                          try {
                            sc.removeItem(item, quantity);
                            System.out.println("Item " + item.getName() + " with quantity "
                                + quantity + " was removed.");
                          } catch (InvalidQuantityException e) {
                            System.out.println("The quantity you entered is invalid.");
                          }
                          // stop searching for item
                          break;
                        }

                      }
                    } else if (customerChoice == 5) {
                      // checkout the cart

                      // tell user the total
                      System.out.println("Your total is: " + sc.getTotal());
                      if (sc.checkOut(sc)) {
                        // if checkout is succesful, let user know
                        System.out.println("Thank you for shopping! Your purchase is complete.");
                      } else {
                        // if not successfull then let user know
                        System.out
                            .println("We do not have enough inventory space. Please try again.");
                      }

                      // ask user if he wants to continue shopping
                      System.out.println("Would you like to continue shopping? (yes/no)");
                      String cont = br.readLine();
                      if (cont.equals("no")) {
                        System.out.println("Ok, thanks for shopping!");
                        System.exit(0);
                      } else if (cont.equals("yes")) {
                        System.out.println("K. You can continue shopping.");

                      }
                    } else if (customerChoice == 6) {
                      // exit and clear the cart
                      sc.updateAccount();
                      System.out.println("Ok.....exiting...");
                    }
                  }
                }
              } else {
                System.out.println("Not a activity_customer.");
              }
            }
          }
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }

  }
}
