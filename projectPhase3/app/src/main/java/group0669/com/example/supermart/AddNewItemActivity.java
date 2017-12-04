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

    editNewItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if (editNewItemName.getText().length() < 1) {
          editNewItemName.setError("The input name is not valid");
        }
      }
    });
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonAddNewItem:
        int itemId = 0;
        try {
          itemId = adminInterface.addItem(editNewItemName.getText().toString(),
              new BigDecimal(editNewItemPrice.getText().toString()), this);
        } catch (InvalidInputException e) {
          Toast.makeText(this, "Check input.", Toast.LENGTH_SHORT).show();
        } catch (InvalidIdException e) {
          Toast.makeText(this, "Check item id.", Toast.LENGTH_SHORT).show();

        }
        if (itemId > 0) {
          Toast.makeText(this, "Item inserted with id: " + itemId, Toast.LENGTH_SHORT).show();
        } else if (itemId == 0) {
          Toast.makeText(this, "Item already in database.", Toast.LENGTH_SHORT).show();
        } else if (itemId == -1) {
          Toast.makeText(this, "Please enter a valid price!.", Toast.LENGTH_SHORT).show();

        }
        finish();

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
