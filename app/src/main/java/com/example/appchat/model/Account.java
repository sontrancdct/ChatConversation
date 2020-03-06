package com.example.appchat.model;

import java.io.Serializable;

public class Account  implements Serializable {
   private String id;
   private String userName;
   private String password;
   private String avatar;

   public Account(String id, String userName, String password, String avatar) {
      this.id = id;
      this.userName = userName;
      this.password = password;
      this.avatar = avatar;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Account() {
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getAvatar() {
      return avatar;
   }

   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }
}
