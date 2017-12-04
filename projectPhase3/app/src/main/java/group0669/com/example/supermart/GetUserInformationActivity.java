package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GetUserInformationActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonGetInformation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_user_information);

    buttonGetInformation = (Button) findViewById(R.id.buttonGetInformation);
    buttonGetInformation.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonGetInformation:
        startActivity(new Intent(this, UserInformationActivity.class));
        break;
    }
  }
}
