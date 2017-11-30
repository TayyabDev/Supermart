package group0669.com.example.supermart;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Customer extends AppCompatActivity implements View.OnClickListener{

    Button buttonRestoreShoppingCart;
    Button buttonAddItem;
    Button buttonRemoveItem;
    Button buttonCheckShoppingCart;

    Button buttonCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);

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
                startActivity(new Intent(this, AddItemActivity.class));
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
}
