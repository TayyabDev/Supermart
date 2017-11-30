package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InitializationActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonInitialize;
    EditText editName, editAge, editAddress, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);


        buttonInitialize = (Button) findViewById(R.id.buttonInitialize);
        buttonInitialize.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonInitialize:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
