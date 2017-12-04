package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.User;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonLogin;
  EditText editUserName, editPassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    editUserName = (EditText) findViewById(R.id.editUsername);
    editUserName.setInputType(InputType.TYPE_CLASS_NUMBER);
    editPassword = (EditText) findViewById(R.id.editPassword);
    buttonLogin = (Button) findViewById(R.id.buttonLogin);

    buttonLogin.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonLogin:
        // get the data from the username and password to see if its correct
        try {
          // check if the user entered an id
          if (editUserName.getText().toString().length() > 0) {
            // get user type from its id
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
            User user = sel.getUser(Integer.parseInt(editUserName.getText().toString()));
            if (user == null) {
              // if user id is invalid tell user
              Toast.makeText(this, "User ID is not valid.", Toast.LENGTH_SHORT).show();
            } else {
              // if user is aadmin go to admin interface
              if (user.authenticate(editPassword.getText().toString(), this)) {
                Intent intent = new Intent(this, MainActivity.class);
                if (sel.getRoleName(user.getRoleId(this)).equals("ADMIN")) {
                  intent = new Intent(this, AdminActivity.class);
                } else if (sel.getRoleName(user.getRoleId(this)).equals("CUSTOMER")) {
                  // of user is activity_customer go to activity_customer interface
                  intent = new Intent(this, CustomerActivity.class);
                }
                intent.putExtra("userId", user.getId());
                startActivity(intent);
                break;
              } else {
                // show a message when the password is incorrect
                Toast.makeText(this, "Wrong username or password. Please try again.",
                    Toast.LENGTH_SHORT).show();
              }
            }
          } else {
            // show a message to tell user to enter password
            Toast.makeText(this, "Please enter a user ID!.", Toast.LENGTH_SHORT).show();
          }
        } catch (InvalidRoleException e) {
          Toast.makeText(this, "Role ID not in database yet. Try contacting an admin.",
              Toast.LENGTH_SHORT).show();
        } catch (InvalidIdException e) {
          Toast.makeText(this, "User ID not in database", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
          Toast.makeText(this, "SQL error", Toast.LENGTH_SHORT).show();
        }

    }
  }
}
