package com.b07.serializable;

import java.io.Serializable;

public class SerializableUserPw {

  public class UserPwSerial implements Serializable {

    private static final long serialVersionUID = 8122331894936517645L;
    private int userId;
    private String userPw;

    private UserPwSerial(int userId, String userPw) {
      this.setUserId(userId);
      this.setUserPw(userPw);
    }

    private void setUserId(int userId) {
      this.userId = userId;
    }

    @SuppressWarnings("unused")
    private int getUserId() {
      return this.userId;
    }

    private void setUserPw(String userPw) {
      this.userPw = userPw;
    }

    @SuppressWarnings("unused")
    private String getUserPw() {
      return this.userPw;
    }
  }

}
