package com.b07.serializable;

import java.io.Serializable;

public class SerializableUserRole implements Serializable {

  private static final long serialVersionUID = 8939705577822164223L;
  private int userId;
  private int roleId;

  private SerializableUserRole(int userid, int roleid) {
    this.setUserId(userid);
    this.setRoleId(roleid);
  }

  private int getUserId() {
    return this.userId;
  }

  private void setUserId(int userid) {
    this.userId = userid;
  }

  private int getRoleId() {
    return this.roleId;
  }

  private void setRoleId(int roleid) {
    this.roleId = roleid;
  }
}

