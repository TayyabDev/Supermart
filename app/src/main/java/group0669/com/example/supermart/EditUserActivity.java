package group0669.com.example.supermart;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.InputType;
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
}
