package com.b07.users;


public class Customer extends User {
  
  private static final long serialVersionUID = -7845430689919000752L;

  /**
   * Customer constructor.
   * 
   * @param id of Customer.
   * @param name of Customer.
   * @param age of Customer.
   * @param address of Customer.
   */
  public Customer(int id, String name, int age, String address) {
    // set the id name and age of the customer
    super.setId(id);
    super.setName(name);
    super.setAge(age);

  }

  /**
   * Customer constructor.
   * 
   * @param id of Customer.
   * @param name of Customer.
   * @param age of Customer.
   * @param address of Customer.
   * @param authenticated if Customer is authenticated.
   */
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    // set the id name and age of the customer
    super.setId(id);
    super.setName(name);
    super.setAge(age);

  }
}
