package com.lzx.BBChat.Common.User;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String userName;
    String password;

    public User(){}
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
