package group0669.com.example.supermart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.store.AdminInterface;

public class ViewSaleActivity extends AppCompatActivity {

  TextView textStatement;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_sale);

    // get the inventory object to initialize an admin interface
    DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
    AdminInterface adminInterface = new AdminInterface(sel.getInventoryHelper());

    // set text view to the statement
    textStatement = findViewById(R.id.textStatement);
    try {
      textStatement.setText(adminInterface.viewBooks(this));
    } catch (InvalidIdException e) {
      e.printStackTrace();
    } catch (InvalidRoleException e) {
      e.printStackTrace();
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
    if (i == R.id.logout_button) {
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
