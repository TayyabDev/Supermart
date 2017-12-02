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

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.inventory.Item;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

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
        List<String> itemsNames = new ArrayList<>();
        for(Item item : items){
            itemsNames.add(item.getName());
        }
        listItems = findViewById(R.id.listItems);
        adapter = new ArrayAdapter<String>(listItems.getContext(), android.R.layout.simple_list_item_1, itemsNames);
        listItems.setAdapter(adapter);


        buttonSave = (Button) findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSave:
                startActivity(new Intent(this, CustomerActivity.class));
                break;
        }
    }
}
