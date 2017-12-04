package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
        } catch (InvalidIdException e) {
            Toast.makeText(this, "Something went wrong. Perhaps your user ID got changed?", Toast.LENGTH_LONG).show();
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

        Spinner spinnerRoleType = (Spinner) findViewById(R.id.spinnerRoleType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddUserActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.role_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoleType.setAdapter(adapter);

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
        if(i == R.id.logout_button){
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

