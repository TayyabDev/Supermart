package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonCheckOut, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        buttonCheckOut = (Button) findViewById(R.id.buttonCheckOut);
        buttonCheckOut.setOnClickListener(this);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCheckOut:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.buttonBack:
                startActivity(new Intent(this, Customer.class));
                break;
        }
    }
}
