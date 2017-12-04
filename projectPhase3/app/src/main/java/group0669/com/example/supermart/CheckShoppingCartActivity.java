package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class CheckShoppingCartActivity extends AppCompatActivity {

  ListView listItems;
  ArrayAdapter<String[]> adapter;
  TextView textShoppingTotal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_shopping_cart);

    // get the cart's item names and quantities and total from previous activity
    Bundle customerData = getIntent().getExtras();

    // get total
    textShoppingTotal = findViewById(R.id.textShoppingTotal);
    textShoppingTotal.setText("Total after tax: " + customerData.get("total"));

    // get item names and quantities
    String[] itemNames = customerData.getStringArray("itemNames");
    int[] itemQuantities = customerData.getIntArray("itemQuantities");
    // SIDE NOTE: I learned how to create a simple_list_item_2 with the help of this youtube video: https://www.youtube.com/watch?v=QsO1_doWcak
    final List<String[]> cartDisplay = new ArrayList<>();
    if (itemNames != null && itemQuantities != null) {
      for (int i = 0; i < itemNames.length; i++) {
        // get item name followed by its quantity in the cart
        String[] current = new String[2];
        current[0] = itemNames[i];
        current[1] = "Quantity: " + itemQuantities[i];
        // add the two arrays to the list of item information
        cartDisplay.add(current);
      }
      // set list of items name to the listView using adapter
      listItems = findViewById(R.id.listItems);
      adapter = new ArrayAdapter<String[]>(listItems.getContext(),
          android.R.layout.simple_list_item_2, android.R.id.text1, cartDisplay) {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          // get view and array of current items data
          View view = super.getView(position, convertView, parent);
          String[] currentItem = cartDisplay.get(position);

          // get the text fields of the adapter
          TextView text1 = view.findViewById(android.R.id.text1);
          TextView text2 = view.findViewById(android.R.id.text2);

          // set the first text to the item name and second text to the item id and quantity
          text1.setText(currentItem[0]);
          text2.setText(currentItem[1]);
          return view;
        }
      };
      // set the listview adapter to the adapter we have just made
      listItems.setAdapter(adapter);


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
