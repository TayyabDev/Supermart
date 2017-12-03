package group0669.com.example.supermart;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
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
        }

        final TextView welcomeUser = (TextView) findViewById(R.id.textWelcomeUser);
        welcomeUser.setText("Welcome " + customer.getName() + "!");

        // make the shopping cart
        try {
            sc = new ShoppingCart(customer, this);
        } catch (CustomerNotLoggedInException e) {
            e.printStackTrace();
        }
        System.out.println(sc.getItems());

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
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        List<Item> items;
        String [] itemNames;
        int [] itemQuantities;

        switch (view.getId()){
            case R.id.buttonRestoreShoppingCart:
                startActivity(new Intent(this, RestoreShoppingCartActivity.class));
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
                startActivity(intent);
                break;

            case R.id.buttonCheckOut:
                startActivity(new Intent(this, CheckOutActivity.class));
                break;

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
            System.out.println(sc.getItems() +" testttt");
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
}