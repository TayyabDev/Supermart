package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Employee extends AppCompatActivity implements View.OnClickListener{

    Button buttonAddOrEdit, buttonInventory, buttonAuthen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);
        buttonAddOrEdit = (Button) findViewById(R.id.buttonAddOrEdit);
        buttonAddOrEdit.setOnClickListener(this);
        buttonInventory = (Button) findViewById(R.id.buttonInventory);
        buttonInventory.setOnClickListener(this);
        buttonAuthen = (Button) findViewById(R.id.buttonAuthen);
        buttonAuthen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddOrEdit:
                startActivity(new Intent(this, AddOrEditActivity.class));
                break;

            case R.id.buttonInventory:
                startActivity(new Intent(this, RestockInventoryActivity.class));
                break;

            case R.id.buttonAuthen:
                startActivity(new Intent(this, MainActivity.class));
            }
    }
}
