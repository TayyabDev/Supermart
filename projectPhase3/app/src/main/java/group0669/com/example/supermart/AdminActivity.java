package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.Admin;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonAddOrEdit;
  Button buttonViewInventory;
  Button buttonViewSale;
  Button buttonRestockInventory;
  Button buttonAddNewItem;
  Button buttonUserInformation;
  Admin admin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    // get the intent data
    Bundle loginData = getIntent().getExtras();
    if (loginData == null) {
      return;
    }
    // get the userid from the bundle
    int userId = loginData.getInt("userId");

    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
    // get the user
    admin = null;
    try {
      admin = (Admin) sel.getUser(userId);
    } catch (InvalidRoleException e) {
      Toast.makeText(this, "Something went wrong. Perhaps you are not an admin?", Toast.LENGTH_LONG)
          .show();
    } catch (InvalidIdException e) {
      Toast.makeText(this, "Something went wrong. Perhaps you are not in database?",
          Toast.LENGTH_LONG).show();
    }

    final TextView welcomeAdmin = (TextView) findViewById(R.id.textWelcomeAdmin);
    welcomeAdmin.setText("Welcome " + admin.getName() + "!");

    buttonAddOrEdit = (Button) findViewById(R.id.buttonAddOrEdit);
    buttonAddOrEdit.setOnClickListener(this);
    buttonViewInventory = (Button) findViewById(R.id.buttonViewInventory);
    buttonViewInventory.setOnClickListener(this);
    buttonViewSale = (Button) findViewById(R.id.buttonViewSale);
    buttonViewSale.setOnClickListener(this);
    buttonRestockInventory = (Button) findViewById(R.id.buttonRestockInventory);
    buttonRestockInventory.setOnClickListener(this);
    buttonAddNewItem = findViewById(R.id.buttonAddNewItem);
    buttonAddNewItem.setOnClickListener(this);
    buttonUserInformation = (Button) findViewById(R.id.buttonUserInformation);
    buttonUserInformation.setOnClickListener(this);


  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonAddOrEdit:
        Intent intent = new Intent(this, AddOrEditActivity.class);
        intent.putExtra("adminId", admin.getId());
        startActivity(intent);
        break;
      case R.id.buttonViewInventory:
        startActivity(new Intent(this, ViewInventoryActivity.class));
        break;
      case R.id.buttonViewSale:
        startActivity(new Intent(this, ViewSaleActivity.class));
        break;
      case R.id.buttonRestockInventory:
        startActivity(new Intent(this, RestockInventoryActivity.class));
        break;
      case R.id.buttonAddNewItem:
        startActivity(new Intent(this, AddNewItemActivity.class));
        break;
      case R.id.buttonUserInformation:
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        View myView = (LayoutInflater.from(AdminActivity.this))
            .inflate(R.layout.activity_get_user_information, null);
        final EditText editUserInformaion = (EditText) myView.findViewById(R.id.editUserID);
        Button getInformation = (Button) myView.findViewById(R.id.buttonGetInformation);

        getInformation.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (!editUserInformaion.getText().toString().isEmpty()) {
              Toast.makeText(AdminActivity.this, "Getting Information",
                  Toast.LENGTH_SHORT).show();
              startActivity(new Intent(AdminActivity.this, UserInformationActivity.class));

            } else {
              Toast.makeText(AdminActivity.this, "Wrong User Id",
                  Toast.LENGTH_SHORT).show();
            }

          }

        });
        builder.setView(myView);
        AlertDialog dialog = builder.create();
        dialog.show();
        break;

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
