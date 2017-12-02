package group0669.com.example.supermart;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    Button buttonSave;
    ListView listItems;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // initialize the list of items
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
        // get list of items
        List<Item> items = sel.getAllItemsHelper();

        // get a list with name of each item
        List<String> itemInformation = new ArrayList<>();
        for(Item item : items){
            itemInformation.add(item.getName());
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

                        // set message of the dialog box and determine what happens when user confirms quantity
                        a_builder.setMessage("Please select a quantity").setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                // get the item user selected and quantity specified
                                intent.putExtra("itemName", itemName);
                                intent.putExtra("quantity", Integer.parseInt(quantity.getText().toString()));

                                // send back to customer activity
                                setResult(RESULT_OK, intent);
                                finish();
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
