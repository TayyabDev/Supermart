package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
