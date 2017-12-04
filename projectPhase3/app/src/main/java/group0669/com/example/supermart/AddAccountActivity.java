package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.database.helper.android.DatabaseAndroidInsertHelper;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.Admin;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener{
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
        switch (view.getId()){
            case R.id.buttonCreateAccount:
                // add account for the user
                DatabaseAndroidInsertHelper ins = new DatabaseAndroidInsertHelper(AddAccountActivity.this);
                try {
                    int accountId = ins.insertAccount(Integer.parseInt(editUserId.getText().toString()), AddAccountActivity.this);

                    // if account created notify user
                    if(accountId > 0 ){
                        Toast.makeText(this, "Account created for user, the account id is " +accountId, Toast.LENGTH_SHORT).show();
                    }
                } catch (InvalidRoleException e) {
                    Toast.makeText(this, "Invalid role, contact administrator", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
