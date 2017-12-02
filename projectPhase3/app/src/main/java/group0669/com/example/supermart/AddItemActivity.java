package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            itemInformation.add(item.getName() + "\t\t Quantity: " + sel.getInventoryQuantity(item.getId()));
        }
        // set list of items name to the listView using adapter
        listItems = findViewById(R.id.listItems);
        adapter = new ArrayAdapter<String>(listItems.getContext(), android.R.layout.simple_list_item_1, itemInformation);
        listItems.setAdapter(adapter);

        listItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String itemName = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(AddItemActivity.this, itemName, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

}
