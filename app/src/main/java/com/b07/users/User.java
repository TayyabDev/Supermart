package com.b07.users;

import android.content.Context;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.android.DatabaseAndroidSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.security.PasswordHelpers;
import java.sql.SQLException;

import java.io.Serializable;


public abstract class User implements Serializable{
  
  private static final long serialVersionUID = 1472686819255975506L;
  
  private int id;
  private String name;
  private int age;
  @SuppressWarnings("unused")
  private String address;
  @SuppressWarnings("unused")
  // don't serialize these into database
  private transient int roleId;
  private transient boolean authenticated;


  /**
   * Get id of the user.
   * 
   * @return int id of the user.
   */
  public int getId() {
    return id;
  }

  /**
   * set if the user.
   * 
   * @param id to set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get name of the user.
   * 
   * @return name of the user.
   */
  public String getName() {
    return name;
  }

  /**
   * set user's name.
   * 
   * @param name name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * get uesr's age.
   * 
   * @return age of the user.
   */
  public int getAge() {
    return age;
  }

  /**
   * set user age.
   * 
   * @param age to set.
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Get role id.
   * 
   * @return role id of the user.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public int getRoleId() throws SQLException, InvalidIdException {
    return DatabaseSelectHelper.getUserRoleId(this.id);
  }
  public int getRoleId(Context context){
      DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
      return sel.getUserRoleId(this.getId());
  }

  /**
   * Check if the user is authenticated.
   * 
   * @param password user input.
   * @return true if the user is authenticated, false otherwise.
   * @throws SQLException thrown if something goes wrong with the query.
   * @throws InvalidIdException thrown if something goes wrong with the id.
   */
  public final boolean authenticate(String password) throws SQLException, InvalidIdException {
    // get the hashed password from the database
    String databasePassword = DatabaseSelectHelper.getPassword(this.id);
    // check if the hashed password is equal to the input password
    this.authenticated = PasswordHelpers.comparePassword(databasePassword, password);
    return this.authenticated;
  }

    public final boolean authenticate(String password, Context context) throws SQLException, InvalidIdException {
        // get the hashed password from the database
        DatabaseAndroidSelectHelper sel = new DatabaseAndroidSelectHelper(context);
        String databasePassword = sel.getPasswordHelper(this.id);
        // check if the hashed password is equal to the input password
        this.authenticated = PasswordHelpers.comparePassword(databasePassword, password);
        return this.authenticated;
    }
}
