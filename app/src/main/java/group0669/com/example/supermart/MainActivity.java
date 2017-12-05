package group0669.com.example.supermart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonLogin, buttonInitialize;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // check if database needs to be initialized
    DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(this);
    // get list of accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

    // if no users in database then go to initialization page
    if (sel.getUsersDetailsHelper().size() < 1) {
      startActivity(new Intent(this, InitializationActivity.class));
      finish();
    } else {
      // go to the login page
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    }

  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {

      case R.id.buttonInitialize:
        startActivity(new Intent(this, InitializationActivity.class));
        break;

      case R.id.buttonLogin:
        startActivity(new Intent(this, LoginActivity.class));
        break;
    }
  }


}

