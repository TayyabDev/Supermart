package group0669.com.example.supermart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.store.AdminInterface;
import com.b07.users.Admin;

public class UserInformationActivity extends AppCompatActivity {

  TextView textUserInformation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_information);

    Bundle userData = getIntent().getExtras();
    if (userData == null) {
      return;
    } else {
      int userId = Integer.parseInt(userData.getString("userID"));

      // create admin interface
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
      AdminInterface adminInterface = new AdminInterface(sel.getInventoryHelper());
      textUserInformation = findViewById(R.id.textUserInformation);
      textUserInformation.setText(adminInterface.getUserInformation(userId, this));

    }
  }

}
