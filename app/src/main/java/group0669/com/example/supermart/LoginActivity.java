package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b07.database.helper.android.DatabaseAndroidSelectHelper;

import com.b07.exceptions.InvalidRoleException;

import com.b07.users.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonLogin;
    EditText editUserName, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserName = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonLogin:
                // get the data from the username and password to see if its correct
                try{
                    // get user type from its id
                    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
                    User user = sel.getUser(Integer.parseInt(editUserName.getText().toString()));

                    // if user is activity_admin go to activity_admin interface
                    Intent intent = new Intent(this, MainActivity.class);
                    if(sel.getRoleName(user.getRoleId(this)).equals("ADMIN")){
                        intent = new Intent(this, AdminActivity.class);
                    } else if(sel.getRoleName(user.getRoleId(this)).equals("CUSTOMER")){
                        // of user is activity_customer go to activity_customer interface
                        intent = new Intent(this, CustomerActivity.class);
                        intent.putExtra("userId", user.getId());
                    }
                    startActivity(intent);
                    break;
                } catch (InvalidRoleException e) {
                    e.printStackTrace();
                }

        }
    }
}
