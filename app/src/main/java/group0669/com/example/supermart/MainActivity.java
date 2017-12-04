package group0669.com.example.supermart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonLogin, buttonInitialize;
  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    buttonLogin = findViewById(R.id.buttonLogin);
    buttonLogin.setOnClickListener(this);
    buttonInitialize = findViewById(R.id.buttonInitialize);
    buttonInitialize.setOnClickListener(this);
    viewPager = findViewById(R.id.viewPager);
    ImageSwipeActivity imageSwipeActivity = new ImageSwipeActivity(this);
    viewPager.setAdapter(imageSwipeActivity);
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

