package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.inventory.Item;
import com.b07.store.AdminInterface;
import java.util.List;

public class RestockInventoryActivity extends AppCompatActivity implements View.OnClickListener {

  EditText editItemName, editQuantity;
  Button buttonConfirm;
  AdminInterface adminInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restock_inventory);

    // get the select helper
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

    // create new admin interface using the inventory
    adminInterface = new AdminInterface(sel.getInventoryHelper());

    editItemName = (EditText) findViewById(R.id.editItemName);
    editQuantity = (EditText) findViewById(R.id.editQuantity);

    buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
    buttonConfirm.setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonConfirm:
        boolean restocked = false;
        // get the item id
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
        List<Item> itemList = sel.getAllItemsHelper();

        for (Item item : itemList) {
          // once we find item restock the inventory
          if (item.getName().equals(editItemName.getText().toString())) {
            // restock inventory if user has entered a quantity
            if (editQuantity.getText().toString().length() > 0) {
              restocked = adminInterface
                  .restockInventory(item, Integer.parseInt(editQuantity.getText().toString()),
                      this);
              if (restocked) {
                Toast.makeText(this, "Item was restocked!", Toast.LENGTH_SHORT).show();
                finish();
              }
            } else {
              // tell user to enter quantity
              Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
              finish();
            }
          }
        }
        // if item was not restocked notify user
        if (!restocked) {
          Toast.makeText(this, "Item was not restocked!", Toast.LENGTH_SHORT).show();
          finish();
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
    if (i == R.id.logout_button) {
      System.out.println("bobmom");
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
