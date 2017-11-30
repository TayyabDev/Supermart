package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonAddUser, buttonEditUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit);

        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);
        buttonAddUser.setOnClickListener(this);
        buttonEditUser = (Button) findViewById(R.id.buttonEditUser);
        buttonEditUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddUser:
                startActivity(new Intent(this, AddUserActivity.class));
                break;
            case R.id.buttonEditUser:
                startActivity(new Intent(this, EditUserActivity.class));
                break;

        }

    }


}
