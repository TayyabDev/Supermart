package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RestockInventoryActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editItemId, editQuantity;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_inventory);

        editItemId = (EditText) findViewById(R.id.editItemId);
        editQuantity = (EditText) findViewById(R.id.editQuantity);

        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonConfirm:
                startActivity(new Intent(this, Admin.class));
        }
    }
}
