package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidInputException;

import java.sql.SQLException;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonNext;
    EditText editUsername, editAge, editAddress, editPassword, editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editUsername = findViewById(R.id.editUsername);
        editAge = findViewById(R.id.editAge);
        editAddress = findViewById(R.id.editAddress);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNext:
                // check if passwords are equal
                if (editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                    // initialize the insert helper
                    DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(this);

                    // insert user
                    String name = editUsername.getText().toString();
                    int age = Integer.parseInt(editAge.getText().toString());
                    String address = editAddress.getText().toString();
                    String password = editPassword.getText().toString();
                    int userId = (int) ins.insertNewUser(name, age, address, password);
                    System.out.println("User id is: " + userId);

                    // initialize select helper
                    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

                    // search for customer role id
                    List<Integer> roleIds = sel.getRoleIdsHelper();
                    System.out.println(roleIds);
                    for(Integer roleId : roleIds){
                        System.out.println(roleId);

                        // once we have found customer role id, establish user as customer
                        if(sel.getRoleName(roleId).equals("CUSTOMER")){
                            ins.insertUserRole(userId, roleId);
                            break;
                        }
                    }
                }
        }
    }
}
