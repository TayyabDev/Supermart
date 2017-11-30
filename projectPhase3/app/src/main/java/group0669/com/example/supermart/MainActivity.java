package group0669.com.example.supermart;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonRegister, buttonCustomer, buttonEmployee, buttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        buttonCustomer = (Button) findViewById(R.id.buttonCustomer);
        buttonCustomer.setOnClickListener(this);
        buttonEmployee = (Button) findViewById(R.id.buttonEmployee);
        buttonEmployee.setOnClickListener(this);
        buttonAdmin = (Button) findViewById(R.id.buttonAdmin);
        buttonAdmin.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonRegister:
                startActivity(new Intent(this, AddUserActivity.class));
                break;

            case R.id.buttonCustomer:
                startActivity(new Intent(this, Customer.class));
                break;

            case R.id.buttonAdmin:
                startActivity(new Intent(this, Admin.class));
                break;
            case R.id.buttonEmployee:
                startActivity(new Intent(this, Employee.class));
                break;
        }
    }

}

