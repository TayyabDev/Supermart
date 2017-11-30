package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthenticateEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editEmployyeeId, editConfirmEmplyeeId;
    Button buttonAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_employee);

        editEmployyeeId = (EditText) findViewById(R.id.editEmployeeId);
        editConfirmEmplyeeId = (EditText) findViewById(R.id.editConfirmEmployeeId);

        buttonAuthenticate = (Button) findViewById(R.id.buttonAuthenticate);
        buttonAuthenticate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAuthenticate:
                startActivity(new Intent(this, Employee.class));
        }
    }
}
