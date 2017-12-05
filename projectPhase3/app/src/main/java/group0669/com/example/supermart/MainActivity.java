package group0669.com.example.supermart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonLogin, buttonInitialize;
  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // check if database needs to be initialized
    DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(this);
    // get list of accounts
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);

    // if no users in database then go to initialization page
    if(sel.getUsersDetailsHelper().size() < 1){
      startActivity(new Intent(this, InitializationActivity.class));
      finish();
    } else {
      // go to the login page
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    }

    /**
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new myTimerTask(), 4000, 4000);
     */
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

  /**
  public class myTimerTask extends TimerTask {

    @Override
    public void run() {
      MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
          } else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(2);
          } else if (viewPager.getCurrentItem() == 2) {
            viewPager.setCurrentItem(3);
          } else {
            viewPager.setCurrentItem(0);
          }
        }
      });
    }
  }
   */


}

