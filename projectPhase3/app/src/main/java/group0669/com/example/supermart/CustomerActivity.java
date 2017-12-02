package group0669.com.example.supermart;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.CustomerNotLoggedInException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;

import java.sql.SQLException;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonRestoreShoppingCart;
    Button buttonAddItem;
    Button buttonRemoveItem;
    Button buttonCheckShoppingCart;
    Button buttonCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // get the intent informationn from login activity
        Bundle loginData = getIntent().getExtras();
        if(loginData == null){
           return;
        }
        // get the userid from the bundle
        int userId = loginData.getInt("userId");

        // get user object given the id
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
        Customer customer = null;
        try {
            customer = (Customer) sel.getUser(userId);
        } catch (InvalidRoleException e) {
            Toast.makeText(this, "Something went wrong. Perhaps you are not an customer?", Toast.LENGTH_LONG).show();
        } catch (InvalidIdException e) {
            Toast.makeText(this, "Something went wrong. Perhaps someone else is using the app?", Toast.LENGTH_LONG).show();
        }

        final TextView welcomeUser = (TextView) findViewById(R.id.textWelcomeUser);
        welcomeUser.setText("Welcome " + customer.getName() + "!");

        // make the shopping cart
        /**
         *
        try {
            ShoppingCart sc = new ShoppingCart(customer);
        } catch (CustomerNotLoggedInException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
         */




        buttonRestoreShoppingCart = (Button) findViewById(R.id.buttonRestoreShoppingCart);
        buttonRestoreShoppingCart.setOnClickListener(this);
        buttonAddItem = (Button) findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(this);
        buttonRemoveItem = (Button) findViewById(R.id.buttonRemoveItem);
        buttonRemoveItem.setOnClickListener(this);
        buttonCheckShoppingCart = (Button) findViewById(R.id.buttonCheckShoppingCart);
        buttonCheckShoppingCart.setOnClickListener(this);
        buttonCheckOut = (Button) findViewById(R.id.buttonCheckOut);
        buttonCheckOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonRestoreShoppingCart:
                startActivity(new Intent(this, RestoreShoppingCartActivity.class));
                break;

            case R.id.buttonAddItem:
                Intent intent = new Intent(this, AddItemActivity.class);
                startActivityForResult(intent, 1);
                break;


            case R.id.buttonRemoveItem:
                startActivity(new Intent(this, RemoveItemActivity.class));
                break;

            case R.id.buttonCheckShoppingCart:
                startActivity(new Intent(this, CheckShoppingCartActivity.class));
                break;

            case R.id.buttonCheckOut:
                startActivity(new Intent(this, CheckOutActivity.class));
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if request code is 1 then add item to cart
        if(requestCode == 1 && resultCode == RESULT_OK){
            System.out.println(data.getStringExtra("itemName") + data.getIntExtra("quantity", 0));

        }
    }
}
