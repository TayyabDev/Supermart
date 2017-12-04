package group0669.com.example.supermart;


import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.CustomerNotLoggedInException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidQuantityException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.ItemNotFoundException;
import com.b07.inventory.Item;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener{

    ShoppingCart sc;
    Button buttonRestoreShoppingCart;
    Button buttonAddItem;
    Button buttonRemoveItem;
    Button buttonCheckShoppingCart;
    Button buttonCheckOut;
    Button buttonSave;
    TextView textTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // get the intent information from login activity
        Bundle loginData = getIntent().getExtras();
        if(loginData == null){
           return;
        }
        // get the user ID from the bundle
        int userId = loginData.getInt("userId");

        // get user object given the id
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
        Customer customer = null;
        try {
            customer = (Customer) sel.getUser(userId);
        } catch (InvalidRoleException e) {
            Toast.makeText(this, "Something went wrong. Perhaps you are not an customer?", Toast.LENGTH_LONG).show();
        } catch (InvalidIdException e) {
            Toast.makeText(this, "Something went wrong. Perhaps someone else is using the app?", Toast.LENGTH_LONG).show();
        } finally{
            // close the helper or else it leaks database
            sel.close();
        }

        final TextView welcomeUser = (TextView) findViewById(R.id.textWelcomeUser);
        welcomeUser.setText("Welcome " + customer.getName() + "!");

        // make the shopping cart
        try {
            sc = new ShoppingCart(customer);
        } catch (CustomerNotLoggedInException e) {
            e.printStackTrace();
        }

        textTotal = findViewById(R.id.textTotal);
        textTotal.setText("Your total is: $" + sc.getTotal());
        buttonRestoreShoppingCart = (Button) findViewById(R.id.buttonRestoreShoppingCart);
        buttonRestoreShoppingCart.setOnClickListener(this);
        buttonAddItem = (Button) findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(this);
        buttonRemoveItem = (Button) findViewById(R.id.buttonRemoveItem);
        buttonRemoveItem.setOnClickListener(this);
        buttonCheckShoppingCart = (Button) findViewById(R.id.buttonCheckShoppingCart);
        buttonCheckShoppingCart.setOnClickListener(this);
        buttonCheckOut = (Button) findViewById(R.id.buttonCheckOut);
        buttonCheckOut.setOnClickListener(this);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        List<Item> items;
        String [] itemNames;
        int [] itemQuantities;

        switch (view.getId()){
            case R.id.buttonRestoreShoppingCart:
                try {
                    if(sc.restoreShoppingCart(this)){
                        Toast.makeText(this, "Your items have been restored", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Your items were not restored.", Toast.LENGTH_SHORT).show();
                    }
                } catch (InvalidIdException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.buttonAddItem:
                intent = new Intent(this, AddItemActivity.class);
                startActivityForResult(intent, 1);
                break;


            case R.id.buttonRemoveItem:
                intent = new Intent(this, RemoveItemActivity.class);
                items = sc.getItems();
                // create two new arrays for item names and quantities
                itemNames = new String [items.size()];
                itemQuantities = new int [items.size()];

                // populate each array with its respective data
                for(int i = 0; i < itemNames.length; i++){
                    itemNames[i] = items.get(i).getName();
                    itemQuantities[i] = sc.getQuantity(items.get(i));
                }

                intent.putExtra("itemNames", itemNames);
                intent.putExtra("itemQuantities", itemQuantities);
                startActivityForResult(intent, 2);
                break;

            case R.id.buttonCheckShoppingCart:
                intent = new Intent(this, CheckShoppingCartActivity.class);

                items = sc.getItems();
                // create two new arrays for item names and quantities
                itemNames = new String [items.size()];
                itemQuantities = new int [items.size()];

                // populate each array with its respective data
                for(int i = 0; i < itemNames.length; i++){
                    itemNames[i] = items.get(i).getName();
                    itemQuantities[i] = sc.getQuantity(items.get(i));
                 }

                intent.putExtra("itemNames", itemNames);
                intent.putExtra("itemQuantities", itemQuantities);
                intent.putExtra("total", sc.getTotal());
                startActivity(intent);
                break;

            case R.id.buttonCheckOut:
                // check if customer has items in his cart 
                if(sc.getItems().size() > 0) {
                    // create alert dialog to ask user to confirm checkout
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(CustomerActivity.this);

                    // set title of alert dialog
                    a_builder.setTitle("Confirm your checkout");

                    // give user the total and ask him to confirm
                    a_builder.setMessage("Your total is: $" + sc.getTotal() + " after tax.").setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // try checking out
                            boolean checkedOut = false;
                            try {
                                checkedOut = sc.checkOut(sc, CustomerActivity.this);
                            } catch (InvalidIdException e) {
                                e.printStackTrace();
                            } catch (InvalidRoleException e) {
                                e.printStackTrace();
                            } catch (InvalidQuantityException e) {
                                e.printStackTrace();
                            } catch (DatabaseInsertException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (InvalidInputException e) {
                                e.printStackTrace();
                            } finally {
                                // give user message if checkout was successful or not
                                if (checkedOut) {
                                    Toast.makeText(CustomerActivity.this, "You have successfully checked out!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(CustomerActivity.this, "Checkout not successful. Check your cart!", Toast.LENGTH_LONG).show();
                                }
                                // update the total
                                updateTotal();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if user cancels then close dialog box
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alert = a_builder.create();
                    alert.show();
                } else {
                    Toast.makeText(this, "Please select some items first!", Toast.LENGTH_SHORT).show();
                }
            case R.id.buttonSave:
                // show alert dialog if customer has account
                    // create alert dialog to ask user to confirm checkout
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(CustomerActivity.this);

                    // set title of alert dialog
                    a_builder.setTitle("Save your cart's items for future use.");

                    // give user message and ask him to confirm
                    a_builder.setMessage("Please note that this can only be done once per account. You will have to" +
                            "contact an Admin to create a new account if you'd like to save your cart again").setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if user confirms then save cart
                            try{
                                boolean update = sc.updateAccount(CustomerActivity.this);
                                if(update){
                                    Toast.makeText(CustomerActivity.this, "Items saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CustomerActivity.this, "Items not saved", Toast.LENGTH_SHORT).show();
                                }
                            } catch(Exception e) {
                                Toast.makeText(CustomerActivity.this, "Contact admin to make a new account. You have already saved once.", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if user cancels then close dialog box
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alert = a_builder.create();
                    alert.show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if request code is 1 then add item to cart
        if(requestCode == 1 && resultCode == RESULT_OK){
            // get item name
            String itemName = data.getStringExtra("itemName");
            // get quantity
            int quantity = data.getIntExtra("quantity", 0);

            // search for item and add it to cart once found
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
            List<Item> itemList = sel.getAllItemsHelper();
            for(Item item : itemList){
                // add item to cart with quantity
                if(item.getName().equals(itemName)){
                    try {
                        sc.addItem(item, quantity, this);
                        Toast.makeText(this, "Item added to cart.", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(this, "Error with SQL", Toast.LENGTH_SHORT).show();
                    } catch (ItemNotFoundException e) {
                        Toast.makeText(this, "Item could not be found.", Toast.LENGTH_SHORT).show();
                    } catch (InvalidQuantityException e) {
                        Toast.makeText(this, "Quantity specified was invalid.", Toast.LENGTH_SHORT).show();
                    } catch (DatabaseInsertException e) {
                        Toast.makeText(this, "Error inserting.", Toast.LENGTH_SHORT).show();
                    } catch (InvalidInputException e) {
                        Toast.makeText(this, "Error in input", Toast.LENGTH_SHORT).show();
                    } break;      // break from loop since we are done
                }
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK){
            // get item name
            String itemName = data.getStringExtra("itemName");
            // get quantity
            int quantity = data.getIntExtra("quantity", 0);
            System.out.println(itemName  +  quantity);
            // search for item and add it to cart once found
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
            List<Item> itemList = sel.getAllItemsHelper();
            for(Item item : itemList){
                // add item to cart with quantity
                if(item.getName().equals(itemName)){
                    try {
                        sc.removeItem(item, quantity);
                        Toast.makeText(this, "Item removed from cart.", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(this, "Error with SQL", Toast.LENGTH_SHORT).show();
                    } catch (ItemNotFoundException e) {
                        Toast.makeText(this, "Item could not be found.", Toast.LENGTH_SHORT).show();
                    } catch (InvalidQuantityException e) {
                        Toast.makeText(this, "Quantity specified was invalid.", Toast.LENGTH_SHORT).show();
                    } catch (DatabaseInsertException e) {
                        Toast.makeText(this, "Error inserting.", Toast.LENGTH_SHORT).show();
                    } catch (InvalidInputException e) {
                        Toast.makeText(this, "Error in input", Toast.LENGTH_SHORT).show();
                    } break;      // break from loop since we are done
                }
            }
        }
        // update the total if we have added or removed an amount from the cart
        updateTotal();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the item id (id of button user picks from menu)
        int i = item.getItemId();
        System.out.println(i);
        if(i == R.id.logout_button){
            // if user clicks logout button then logout and clear the activity stack
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // give user toast thats hes logging out
            Toast.makeText(this, "Succesfully logged out!", Toast.LENGTH_SHORT).show();
            // go to login page
            startActivity(intent);
            finish();
        }
        return super.onContextItemSelected(item);
    }

    private void updateTotal(){
        textTotal.setText("Your total is: $" + sc.getTotal());
    }
}
