package group0669.com.example.supermart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.inventory.Item;
import com.b07.store.DailyDeals;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AddItemActivity extends AppCompatActivity {

  ListView listItems;
  ListAdapter adapter;
  HashMap<String, Item> itemNameToItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_item);

    itemNameToItem = new HashMap<>();
    // initialize the list of items
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
    // get list of items
    List<Item> items = sel.getAllItemsHelper();

    // get a list with name of each item
    List<String> itemInformation = new ArrayList<>();
    for (Item item : items) {
      itemInformation.add(item.getName());
      itemNameToItem.put(item.getName(), item);
    }
    // set list of items name to the listView using adapter
    listItems = findViewById(R.id.listItems);
    adapter = new ArrayAdapter<String>(listItems.getContext(), android.R.layout.simple_list_item_1,
        itemInformation);
    listItems.setAdapter(adapter);

    // when user clicks an item then ask for quantity of that item
    listItems.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
            final String itemName = String.valueOf(adapterView.getItemAtPosition(i));
            // create alert dialog to ask for quantity
            AlertDialog.Builder a_builder = new AlertDialog.Builder(AddItemActivity.this);

            // create new edit text that takes in integer for the quantity
            final EditText quantity = new EditText(AddItemActivity.this);
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            a_builder.setView(quantity);

            a_builder.setTitle("Please enter a quantity of the item you would like to add");

            // get inventory quantity and price of item
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(AddItemActivity.this);
            final int inventoryQuantity = sel
                .getInventoryQuantity(itemNameToItem.get(itemName).getId());
            BigDecimal price = itemNameToItem.get(itemName).getPrice();

            // give user the quantity of the item and determine happens when user selects confirm

            // get the daily deal
            DailyDeals dailyDeals = new DailyDeals(AddItemActivity.this);
            HashMap<Item, BigDecimal> todaysDeal = dailyDeals.getTodaysDiscount();
            Item todaysItem =(Item) todaysDeal.keySet().toArray()[0];

            // check if user selected the daily deal item
            if(todaysItem.getId() == itemNameToItem.get(itemName).getId()){
              // apply the discount to the price
              BigDecimal discountPrice = price.subtract(todaysItem.getPrice().multiply(todaysDeal.get(todaysItem))).setScale(2);
              a_builder.setMessage("Today the item has a discount! The price of item is $" + discountPrice + " today,"
                  + " originally $" + price+ "\nThe inventory quantity is "
                  + inventoryQuantity);
            } else {
              // if user did not select daily deal item then give him normal price
              a_builder.setMessage("The price of item is $" + price + "\nThe inventory quantity is "
                  + inventoryQuantity);
            }
            a_builder.setCancelable(false).setPositiveButton
                ("Confirm", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    // check if quanttiy is valid
                    if (Integer.parseInt(quantity.getText().toString()) > 0
                        && Integer.parseInt(quantity.getText().toString()) <= inventoryQuantity) {
                      Intent intent = new Intent();
                      // get the item user selected and quantity specified
                      intent.putExtra("itemName", itemName);
                      intent.putExtra("quantity", Integer.parseInt(quantity.getText().toString()));

                      // send back to customer activity
                      setResult(RESULT_OK, intent);
                      finish();
                    } else {
                      Toast.makeText(AddItemActivity.this, "Please enter a valid quantity!",
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                // close dailog
                dialogInterface.cancel();
              }
            });
            AlertDialog alert = a_builder.create();
            alert.show();
          }
        }
    );
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // inflate the menu so we get logout button
    getMenuInflater().inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // get the item id (id of button user picks from menu)
    int i = item.getItemId();
    System.out.println(i);
    if (i == R.id.logout_button) {
      // if user clicks logout button then logout and clear the activity stack
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

      // give user toast thats hes logging out
      Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
      // go to login page
      startActivity(intent);
      finish();
    }
    return super.onContextItemSelected(item);
  }


}
