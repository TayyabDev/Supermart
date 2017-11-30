package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RemoveItemActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSave:
                startActivity(new Intent(this, Customer.class));
                break;
        }
    }
}
