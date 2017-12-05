package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.store.AdminInterface;
import com.b07.users.Account;
import com.b07.users.Admin;
import java.sql.SQLException;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener {

  EditText editUserId;
  Button buttonCreateAccount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_account);

    editUserId = findViewById(R.id.editUserIdForAccount);
    editUserId.setInputType(InputType.TYPE_CLASS_NUMBER);
    buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
    buttonCreateAccount.setOnClickListener(this);
    //
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonCreateAccount:
        // try to inserr the account for the user if user id is defined
        if (editUserId.getText().toString().length() > 0) {
          try {
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(
                AddAccountActivity.this);
            AdminInterface adminInterface = new AdminInterface(sel.getInventoryHelper());

            Account account = adminInterface
                .createAccount(Integer.parseInt(editUserId.getText().toString()),
                    AddAccountActivity.this);
            // if account created notify user
            if (account.getId() > 0) {
              Toast.makeText(this, "Account created for user, the account id is " + account.getId(),
                  Toast.LENGTH_SHORT).show();
            }

          } catch (InvalidRoleException e) {
            Toast.makeText(this, "Invalid role, contact administrator", Toast.LENGTH_SHORT).show();
          } catch (InvalidIdException e) {
            e.printStackTrace();
          } catch (InvalidInputException e) {
            e.printStackTrace();
          } catch (SQLException e) {
            e.printStackTrace();
          } catch (DatabaseInsertException e) {
            e.printStackTrace();
          }
        } else {
          Toast.makeText(this, "Please enter a user ID!", Toast.LENGTH_SHORT).show();
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
