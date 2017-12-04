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
import android.widget.ListView;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveItemActivity extends AppCompatActivity {

  ListView listItems;
  ArrayAdapter<String> adapter;
  HashMap<String, Integer> itemNameToQuantity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_shopping_cart);

    itemNameToQuantity = new HashMap<>();
    // get the cart's item names and quantites from previous activity
    Bundle customerData = getIntent().getExtras();
    String[] itemNames = customerData.getStringArray("itemNames");
    int[] itemQuantities = customerData.getIntArray("itemQuantities");

    // SIDE NOTE: I learned how to create a simple_list_item_2 with the help of this youtube video: https://www.youtube.com/watch?v=QsO1_doWcak
    final List<String> cartDisplay = new ArrayList<>();
    if (itemNames != null && itemQuantities != null) {
      for (int i = 0; i < itemNames.length; i++) {
        // get item name followed by its quantity in the cart
        // add the two arrays to the list of item information
        cartDisplay.add(itemNames[i]);

        // add the inforamtion to the hashmap of item name to quantity
        itemNameToQuantity.put(itemNames[i], itemQuantities[i]);
      }
      // set list of items name to the listView using adapter
      listItems = findViewById(R.id.listItems);
      adapter = new ArrayAdapter<String>(listItems.getContext(),
          android.R.layout.simple_list_item_1, cartDisplay);

      // set the listview adapter to the adapter we have just made
      listItems.setAdapter(adapter);

      // when user clicks an item then ask for quantity of that item
      listItems.setOnItemClickListener(
          new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
              final String itemName = String.valueOf(adapterView.getItemAtPosition(i));
              // create alert dialog to ask for quantity
              AlertDialog.Builder a_builder = new AlertDialog.Builder(RemoveItemActivity.this);

              // create new edit text that takes in integer for the quantity
              final EditText quantity = new EditText(RemoveItemActivity.this);
              quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
              a_builder.setView(quantity);

              a_builder.setTitle("Please enter a quantity you wish to remove");

              DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(
                  RemoveItemActivity.this);
              final int cartQuantity = itemNameToQuantity.get(itemName);

              // give user the quantity of the item and determine happens when user selects confirm
              a_builder.setMessage(
                  "You currently have: " + cartQuantity + " " + itemName + " in your cart.")
                  .setCancelable(false)
                  .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      // check if quantity is valid
                      if (Integer.parseInt(quantity.getText().toString()) > 0
                          && Integer.parseInt(quantity.getText().toString()) <= cartQuantity) {
                        Intent intent = new Intent();
                        // get the item user selected and quantity specified
                        intent.putExtra("itemName", itemName);
                        intent
                            .putExtra("quantity", Integer.parseInt(quantity.getText().toString()));

                        // send back to customer activity
                        setResult(RESULT_OK, intent);
                        finish();
                      } else {
                        Toast.makeText(RemoveItemActivity.this, "Please enter a valid quantity!",
                            Toast.LENGTH_SHORT).show();
                      }
                    }
                  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.cancel();
                }
              });
              AlertDialog alert = a_builder.create();
              alert.show();
            }
          }
      );
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
    if (i == R.id.logout_button) {
      System.out.println("bobmom");
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
