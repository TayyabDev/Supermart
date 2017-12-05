package com.b07.serializable;

import java.io.Serializable;

public class SerializableItemizedSales implements Serializable {

  private static final long serialVersionUID = 4735638486986484424L;
  private int roleId;
  private String roleName;

  private SerializableItemizedSales(int roleid, String rolename) {
    this.setRoleId(roleid);
    this.setRoleName(rolename);
  }

  @SuppressWarnings("unused")
  private int getRoleId() {
    return this.roleId;
  }

  private void setRoleId(int id) {
    this.roleId = id;
  }

  @SuppressWarnings("unused")
  private String getRoleName() {
    return this.roleName;
  }

  private void setRoleName(String name) {
    this.roleName = name;
  }

}
