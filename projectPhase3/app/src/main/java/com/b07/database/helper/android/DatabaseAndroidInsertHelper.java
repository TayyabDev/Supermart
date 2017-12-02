package com.b07.database.helper.android;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.DatabaseInserter;
import com.b07.enumerators.Roles;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidInsertHelper extends DatabaseDriverAndroid{
    public DatabaseAndroidInsertHelper(Context context) {
        super(context);
    }
    public long insertRole(String role){
        long roleId = -1;
        // check if role is valid
        if(role.equals(Roles.ADMIN.toString()) || role.equals(Roles.CUSTOMER.toString())){
            // insert role
            roleId = super.insertRole(role);
        }
        return roleId;
    }
    public long insertNewUser(String name, int age, String address, String password){
        long userId  = -1;
        // check if input parameters are valid
        if(name != null && age >= 0 && address != null && password != null && password.length() <= 64){
            // insert user into database
            userId =  super.insertNewUser(name, age, address, password);
        }
        return userId;
    }

    public long insertUserRole(int userId, int roleId){
        int userRoleId = -1;
        // insert user with the given role
        super.insertUserRole(userId, roleId);
        return userRoleId;
    }

    public long insertItem(String name, BigDecimal price){
        // insert item into database if price is >= 0, else return 0
        if(price.compareTo(new BigDecimal("0.00")) == 0 || price.compareTo(new BigDecimal("0.00")) == 1){
            return super.insertItem(name, price);
        } else{
            return 0;
        }

    }





}
