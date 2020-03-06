package com.example.appchat.model;

public class Message {
   private String userName;
   private int    type;
   private String message;
   private String path;
   private long   date;
   boolean isChecked = false;
   private String avatar;

   public Message() {
   }
   public String getAvatar() {
      return avatar;
   }

   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }

   public boolean isChecked() {
      return isChecked;
   }

   public void setChecked(boolean checked) {
      isChecked = checked;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public long getDate() {
      return date;
   }

   public void setDate(long date) {
      this.date = date;
   }

}

