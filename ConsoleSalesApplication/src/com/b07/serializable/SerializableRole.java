package com.b07.serializable;

import java.io.Serializable;

// Serialize the Role table
public class SerializableRole implements Serializable{
  
  private static final long serialVersionUID = 4735638486986484424L;
  private int roleId;
  private String roleName;
  
  private SerializableRole(int id, String name) {
    this.setRoleId(id);
    this.setRoleName(name);
  }
  
  private void setRoleId(int id) {
    this.roleId = id;
  }
  
  @SuppressWarnings("unused")
  private int getRoleId() {
    return this.roleId;
  }
  
  private void setRoleName(String name) {
    this.roleName = name;
  }
  
  @SuppressWarnings("unused")
  private String getRoleName() {
    return this.roleName;
  }

}
