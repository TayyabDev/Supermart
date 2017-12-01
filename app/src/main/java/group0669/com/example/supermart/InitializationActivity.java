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

public class InitializationActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonInitialize;
    EditText editName, editAge, editAddress, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);


        buttonInitialize = (Button) findViewById(R.id.buttonInitialize);
        buttonInitialize.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonInitialize:
                try{
                    // initialize the database
                    DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(this);

                    // insert get admin role
                    DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(this);
                    int adminRoleId = (int) ins.insertRole("ADMIN");

                    // insert the admin into the database
                    int adminId = (int) ins.insertNewUser(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), editAddress.getText().toString(), editPassword.getText().toString());

                    // establish user as admin
                    ins.insertUserRole(adminId, adminRoleId);

                    // go to login page
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                } catch(Exception e){
                    e.printStackTrace();
                }

        }
    }
}
