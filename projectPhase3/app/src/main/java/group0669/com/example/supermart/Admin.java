package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    Button buttonAddOrEdit;
    Button buttonViewInventory;
    Button buttonViewSale;
    Button buttonInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

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
                startActivity(new Intent(this, AddOrEditActivity.class));
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
