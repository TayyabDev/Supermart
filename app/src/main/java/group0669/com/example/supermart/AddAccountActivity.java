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
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.exceptions.InvalidRoleException;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener {

  EditText editUserId;
  Button buttonCreateAccount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_account);

    editUserId = findViewById(R.id.editUserIdForAccount);
    buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
    buttonCreateAccount.setOnClickListener(this);
    //
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonCreateAccount:
        // add account for the user
        DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(AddAccountActivity.this);
        try {
          int accountId = ins.insertAccount(Integer.parseInt(editUserId.getText().toString()),
              AddAccountActivity.this);

          // if account created notify user
          if (accountId > 0) {
            Toast.makeText(this, "Account created for user, the account id is " + accountId,
                Toast.LENGTH_SHORT).show();
          }
        } catch (InvalidRoleException e) {
          Toast.makeText(this, "Invalid role, contact administrator", Toast.LENGTH_SHORT).show();
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
