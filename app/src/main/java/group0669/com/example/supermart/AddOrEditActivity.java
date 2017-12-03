package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonAddUser, buttonEditUser;
    int adminId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit);

        // get the admin user id
        Bundle adminActivityData = getIntent().getExtras();
        adminId = adminActivityData.getInt("adminId");

        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);
        buttonAddUser.setOnClickListener(this);
        buttonEditUser = (Button) findViewById(R.id.buttonEditUser);
        buttonEditUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddUser:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra("adminId", adminId);
                startActivity(intent);
                break;
            case R.id.buttonEditUser:
                Intent intentEdit = new Intent(this, EditUserActivity.class);
                intentEdit.putExtra("adminId", adminId);
                startActivity(intentEdit);
                break;

        }

    }


}
