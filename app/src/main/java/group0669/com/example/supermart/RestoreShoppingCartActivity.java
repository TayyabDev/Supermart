package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RestoreShoppingCartActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonYes, buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_shopping_cart);

        buttonYes = (Button) findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(this);
        buttonNo = (Button) findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonYes:
                startActivity(new Intent(this, CustomerActivity.class));
                break;

            case R.id.buttonNo:
                startActivity(new Intent(this, CustomerActivity.class));
                break;
        }
    }
}
