package com.b07.database.helper.android;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.enumerators.Roles;
import com.b07.exceptions.InvalidRoleException;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.User;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidSelectHelper extends DatabaseDriverAndroid {
    public DatabaseAndroidSelectHelper(Context context) {
        super(context);
    }

    public User getUser(int userId) throws InvalidRoleException {
        User user = null;
        Cursor c = super.getUserDetails(userId);
        int id = -1;
        String name = "";
        int age = -1;
        String address = "";

        // get user info
        while(c.moveToNext()){
            id = c.getInt(c.getColumnIndex("ID"));
            name = c.getString(c.getColumnIndex("NAME"));
            age =  c.getInt(c.getColumnIndex("AGE"));
            address = c.getString(c.getColumnIndex("ADDRESS"));
        }
        c.close();
        // get users role id
        int roleId = getUserRole(userId);
        // construct user and return
        user = createUser(id, name, age, address, roleId);
        return user;

    }
    private User createUser(int id, String name, int age, String address, int roleId)
            throws SQLException, InvalidRoleException {
        // initialize the user
        User user = null;
        // get the role name using the id
        String roleName = getRoleName(roleId);
        // if role is admin create an admin
        if (roleName.equals(Roles.ADMIN.toString())) {
            user = new Admin(id, name, age, address);
        } else if (roleName.equals(Roles.CUSTOMER.toString())) {
            user = new Customer(id, name, age, address);
        }
        return user;
    }

    public String getRoleName(int roleId){
        return super.getRole(roleId);
    }

    public int getUserRole(int userId){
        return super.getUserRole(userId);
    }

}
