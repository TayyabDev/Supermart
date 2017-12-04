package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.android.DatabaseAndroidInsertHelper;

import java.math.BigDecimal;

public class InitializationActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonInitialize;
    EditText editName, editAge, editAddress, editPassword, editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);

        buttonInitialize = (Button) findViewById(R.id.buttonInitialize);
        buttonInitialize.setOnClickListener(this);

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editName.getText().length() <=  3) {
                    editName.setError("The input name is too short");
                }

            }
        });

        editAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editAge.getText().toString().equals("0")) {
                    editAge.setError("Please input a valid age");
                }

            }
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editPassword.getText().length() < 1) {
                    editPassword.setError("The password is too short. Must be at least 1 character.");
                }
            }
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editPassword.getText().length() > 64) {
                    editPassword.setError("The password is too long. Must be less than 64 characters.");
                }
            }
        });

        editConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!editConfirmPassword.getText().toString().equals(editPassword.getText().toString())) {
                    editConfirmPassword.setError("The passwords must match!");
                }
            }
        });

        editConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editAddress.getText().toString().length() > 100) {
                    editConfirmPassword.setError("Address is too long! Must be less than 100 characters");
                }
            }
        });


        editAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editAddress.getText().length() < 1) {
                    editAddress.setError("Please input a valid address");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonInitialize:
                try{
                    // check if passwords match
                    if(editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                        // initialize the database
                        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(this);

                        // insert get activity_admin role
                        DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(this);
                        int adminRoleId = (int) ins.insertRole("ADMIN");

                        // insert the user into the database
                        int adminId = (int) ins.insertNewUser(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), editAddress.getText().toString(), editPassword.getText().toString());

                        // establish user as admin
                        ins.insertUserRole(adminId, adminRoleId);

                        // tell user the user id
                        Toast.makeText(this, "Admin created! Your user ID is: " + adminId, Toast.LENGTH_SHORT).show();


                        // insert customer role into database
                        ins.insertRole("CUSTOMER");

                        // insert some default items
                        int fishingRodId = (int) ins.insertItem("FISHING_ROD", new BigDecimal("450.00"), this);
                        int skatesId = (int) ins.insertItem("SKATES", new BigDecimal("200.00"), this);
                        int hockeyStickId = (int) ins.insertItem("HOCKEY_STICK", new BigDecimal("100.00"), this);
                        int proteinBarId = (int) ins.insertItem("PROTEIN_BAR", new BigDecimal("5.00"), this);
                        int runningShoesId = (int) ins.insertItem("RUNNING_SHOES", new BigDecimal("70.00"), this);

                        // insert inventory quantities for the items, default 10
                        ins.insertInventoryHelper(fishingRodId, 10, this);
                        ins.insertInventoryHelper(skatesId, 10, this);
                        ins.insertInventoryHelper(hockeyStickId, 10, this);
                        ins.insertInventoryHelper(proteinBarId, 10, this);
                        ins.insertInventoryHelper(runningShoesId, 10, this);

                        // go to login page
                        startActivity(new Intent(this, LoginActivity.class));

                        break;
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

        }
    }
}
