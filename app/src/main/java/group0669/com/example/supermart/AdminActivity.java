package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.Admin;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAddOrEdit;
    Button buttonViewInventory;
    Button buttonViewSale;
    Button buttonInventory;
    Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // get the intent data
        Bundle loginData = getIntent().getExtras();
        if(loginData == null){
            return;
        }
        // get the userid from the bundle
        int userId = loginData.getInt("userId");

        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
        // get the user
        admin = null;
        try {
            admin = (Admin) sel.getUser(userId);
        } catch (InvalidRoleException e) {
            e.printStackTrace();
        }

        final TextView welcomeAdmin= (TextView) findViewById(R.id.textWelcomeAdmin);
        welcomeAdmin.setText("Welcome " + admin.getName() + "!");


        buttonAddOrEdit = (Button) findViewById(R.id.buttonAddOrEdit);
        buttonAddOrEdit.setOnClickListener(this);
        buttonViewInventory = (Button) findViewById(R.id.buttonViewInventory);
        buttonViewInventory.setOnClickListener(this);
        buttonViewSale = (Button) findViewById(R.id.buttonViewSale);
        buttonViewSale.setOnClickListener(this);
        buttonInventory = (Button) findViewById(R.id.buttonInventory);
        buttonInventory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddOrEdit:
                Intent intent = new Intent(this, AddOrEditActivity.class);
                intent.putExtra("adminId", admin.getId());
                startActivity(intent);
                break;
            case R.id.buttonViewInventory:
                startActivity(new Intent(this, ViewInventoryActivity.class));
                break;
            case R.id.buttonViewSale:
                startActivity(new Intent(this, ViewSaleActivity.class));
                break;
            case R.id.buttonInventory:
                startActivity(new Intent(this, RestockInventoryActivity.class));
                break;
        }
    }
}
