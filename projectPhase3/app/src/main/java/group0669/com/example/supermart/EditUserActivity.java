package group0669.com.example.supermart;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.InputType;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
        import com.b07.exceptions.InvalidIdException;
        import com.b07.exceptions.InvalidRoleException;
        import com.b07.store.AdminInterface;
        import com.b07.users.Admin;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonUpdate;
    EditText editUsername, editAge, editAddress, editUserID;
    AdminInterface adminInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // get the admin id and construct admin
        Bundle adminData = getIntent().getExtras();
        int adminId = (int) adminData.get("adminId");

        Admin admin = null;
        try{
            DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(this);
            admin = (Admin) sel.getUser(adminId);

            // make the admin interface
            adminInterface = new AdminInterface(admin, sel.getInventoryHelper());
        } catch (InvalidIdException e) {
            e.printStackTrace();
        } catch (InvalidRoleException e) {
            e.printStackTrace();
        }


        editUserID = (EditText) findViewById(R.id.editUserID);
        editUserID.setInputType(InputType.TYPE_CLASS_NUMBER);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editAge = (EditText) findViewById(R.id.editAge);
        editAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        editAddress = (EditText) findViewById(R.id.editAddress);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(this);

        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editUsername.getText().length() < 1) {
                    editUsername.setError("The input name is too short");
                }

            }
        });

        editAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(editAge.getText().toString().length() > 0){
                    if (Integer.parseInt(editAge.getText().toString()) < 13) {
                        editAge.setError("User must be 13 to use the application.");
                    }
                }


            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonUpdate:
                // update the user with the given information
                if(editUserID.getText().toString().length() > 0){
                    boolean edited = false;
                    try {
                        adminInterface.editUser(Integer.parseInt(editUserID.getText().toString()),
                                editUsername.getText().toString(),Integer.parseInt(editAge.getText().toString()), editAddress.getText().toString(), this);

                        Toast.makeText(this, "User has been edited!", Toast.LENGTH_SHORT).show();
                    } catch (InvalidIdException e) {
                        e.printStackTrace();
                    } catch (InvalidRoleException e) {
                        e.printStackTrace();
                    }
                    finish();

                } else {
                    Toast.makeText(this, "Please enter a user ID", Toast.LENGTH_SHORT).show();
                }

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
