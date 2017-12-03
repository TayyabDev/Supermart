package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.b07.users.Admin;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonCreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);
        //
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCreateAccount:
                startActivity(new Intent(this, AdminActivity.class));
                break;
        }
    }
}
