package group0669.com.example.supermart;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class ViewDailyDealActivity extends AppCompatActivity {

  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_daily_deal);
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    ImageSwipeActivity imageSwipeActivity = new ImageSwipeActivity(this);
    viewPager.setAdapter(imageSwipeActivity);

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new myTimerTask(), 4000, 4000);
  }


  public class myTimerTask extends TimerTask {

    @Override
    public void run() {
      // let image slide automatically
      ViewDailyDealActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
          } else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(2);
          } else if (viewPager.getCurrentItem() == 2) {
            viewPager.setCurrentItem(3);
          } else if (viewPager.getCurrentItem() == 3) {
            viewPager.setCurrentItem(4);
          } else {
            viewPager.setCurrentItem(0);
          }
        }
      });
    }
  }

}
