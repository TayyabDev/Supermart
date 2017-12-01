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
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidInputException;

import java.sql.SQLException;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonNext;
    EditText editUsername, editAge, editAddress, editPassword, editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editUsername =  findViewById(R.id.editUsername);
        editAge =  findViewById(R.id.editAge);
        editAddress =  findViewById(R.id.editAddress);
        editPassword =  findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmEmployeeId);
        buttonNext =  findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonNext:
                // check if passwords are equal
                if(editPassword.getText().toString().equals(editConfirmPassword.getText().toString())){
                    // insert user into database
                    try {
                        DatabaseInsertHelper.insertNewUser(editUsername.getText().toString(), Integer.parseInt(editAge.getText().toString()), editAddress.getText().toString(), editPassword.getText().toString());
                    } catch (DatabaseInsertException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InvalidInputException e) {
                        e.printStackTrace();
                    }
                }


        }
    }
}
