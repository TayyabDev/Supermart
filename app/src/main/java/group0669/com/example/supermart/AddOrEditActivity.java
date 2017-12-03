package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonAddUser, buttonEditUser,buttonCreateAccount;
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
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);
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
            case R.id.buttonCreateAccount:
                startActivity(new Intent(this, AddAccountActivity.class));
                break;



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
