package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup.Input;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetUserInformationActivity extends AppCompatActivity implements View.OnClickListener {

  Button buttonGetInformation;
  EditText editUserID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_user_information);

    editUserID = findViewById(R.id.editUserID);
    editUserID.setInputType(InputType.TYPE_CLASS_NUMBER);
    buttonGetInformation = (Button) findViewById(R.id.buttonGetInformation);
    buttonGetInformation.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonGetInformation:
        if (editUserID.getText().toString().length() > 0) {
          Intent intent = new Intent(GetUserInformationActivity.this,
              UserInformationActivity.class);
          break;
        }

    }
  }
}
