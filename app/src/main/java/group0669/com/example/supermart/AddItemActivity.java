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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.inventory.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        for(Item item : items){
            itemInformation.add(item.getName());
            itemNameToItem.put(item.getName(), item);
        }
        // set list of items name to the listView using adapter
        listItems = findViewById(R.id.listItems);
        adapter = new ArrayAdapter<String>(listItems.getContext(), android.R.layout.simple_list_item_1, itemInformation);
        listItems.setAdapter(adapter);

        // when user clicks an item then ask for quantity of that item
        listItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                        final String itemName = String.valueOf(adapterView.getItemAtPosition(i));
                        // create alert dialog to ask for quantity
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(AddItemActivity.this);

                        // create new edit text that takes in integer for the quantity
                        final EditText quantity = new EditText(AddItemActivity.this);
                        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                        a_builder.setView(quantity);

                        a_builder.setTitle("Please enter a quantity of the item");

                        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(AddItemActivity.this);
                        final int inventoryQuantity = sel.getInventoryQuantity(itemNameToItem.get(itemName).getId());

                        // give user the quantity of the item and determien waht happens when user selects confirm
                        a_builder.setMessage("The item quantity is: " + inventoryQuantity).setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // check if quanttiy is valid
                                if (Integer.parseInt(quantity.getText().toString()) > 0 && Integer.parseInt(quantity.getText().toString()) <= inventoryQuantity) {
                                    Intent intent = new Intent();
                                    // get the item user selected and quantity specified
                                    intent.putExtra("itemName", itemName);
                                    intent.putExtra("quantity", Integer.parseInt(quantity.getText().toString()));

                                    // send back to customer activity
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddItemActivity.this, "Please enter a valid quantity!", Toast.LENGTH_SHORT).show();
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
