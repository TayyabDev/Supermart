package group0669.com.example.supermart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        buttonCheckOut = (Button) findViewById(R.id.buttonCheckOut);
        buttonCheckOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCheckOut:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the item id (id of button user picks from menu)
        int i = item.getItemId();
        System.out.println(i);
        if(i == R.id.logout_button){
            System.out.println("bobmom");
            // if user clicks logout button then logout and clear the activity stack
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // give user toast thats hes logging out
            Toast.makeText(this, "Succesfully logged out!", Toast.LENGTH_SHORT).show();
            // go to login page
            startActivity(intent);
            finish();
        }
        return super.onContextItemSelected(item);
    }
}
