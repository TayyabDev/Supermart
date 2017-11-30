package com.b07.users;

public class Admin extends User {

  /**
   * Admin constructor.
   * 
   * @param id of admin.
   * @param name of admin.
   * @param age of admin.
   * @param address of admin.
   */
  public Admin(int id, String name, int age, String address) {
    // set the id name and age of the admin
    super.setId(id);
    super.setName(name);
    super.setAge(age);
  }

  /**
   * Admin constructor.
   * 
   * @param id of admin.
   * @param name of admin.
   * @param age of admin.
   * @param address of admin.
   * @param authenticated if admin is authenticated.
   */
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    // set the id name and age of the admin
    super.setId(id);
    super.setName(name);
    super.setAge(age);

  }

}
