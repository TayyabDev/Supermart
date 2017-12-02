package group0669.com.example.supermart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.store.AdminInterface;

import java.math.BigDecimal;

public class AddNewItemActivity extends AppCompatActivity implements View.OnClickListener {
    AdminInterface adminInterface;
    EditText editNewItemName, editNewItemPrice;
    Button buttonAddNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        // get select helper referernce obj
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

        // create new admin interface
        adminInterface = new AdminInterface(sel.getInventoryHelper());

        // get the button and edittext's
        editNewItemName = findViewById(R.id.editNewItemName);
        editNewItemPrice = findViewById(R.id.editNewItemPrice);
        buttonAddNewItem = findViewById(R.id.buttonAddNewItem);
        buttonAddNewItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddNewItem:
                int itemId = 0;
                try {
                    itemId = adminInterface.addItem(editNewItemName.getText().toString(), new BigDecimal(editNewItemPrice.getText().toString()), this);
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                } catch (InvalidIdException e) {
                    e.printStackTrace();
                }
                if(itemId != -1){
                    Toast.makeText(this, "Item inserted with id: "+itemId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Item not inserted", Toast.LENGTH_SHORT).show();
                }
                finish();

        }
    }
}
