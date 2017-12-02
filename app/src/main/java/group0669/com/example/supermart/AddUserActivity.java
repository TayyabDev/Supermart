package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.InventoryFullException;
import com.b07.store.AdminInterface;
import com.b07.users.Admin;

import java.sql.SQLException;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonNext;
    EditText editUsername, editAge, editAddress, editPassword, editConfirmPassword;
    AdminInterface adminInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // get the admin user id
        Bundle adminActivityData = getIntent().getExtras();
        int adminId = adminActivityData.getInt("adminId");

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

        // create admin from the id
        Admin admin = null;
        try {
            admin = (Admin) sel.getUser(adminId);
        } catch (InvalidRoleException e) {
            Toast.makeText(this, "Something went wrong. Perhaps you are not an admin?", Toast.LENGTH_LONG).show();
        }

        // get the admin interface
        adminInterface = new AdminInterface(admin, sel.getInventoryHelper());

        // get the buttons
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
                    // create customer and close activity
                    int customerId = adminInterface.createCustomer(editUsername.getText().toString(), Integer.parseInt(editAge.getText().toString()), editAddress.getText().toString(), editPassword.getText().toString(), this);
                    Toast.makeText(this, "Customer created with id: " + customerId, Toast.LENGTH_LONG).show();
                    finish();
                }
                }
        }
    }

