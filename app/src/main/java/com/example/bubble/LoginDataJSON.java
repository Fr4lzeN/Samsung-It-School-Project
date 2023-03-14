package com.example.bubble;

public class LoginDataJSON {
    public Boolean result;
    public String login;
    public String password;
    public String email;
    public String error;



    public LoginDataJSON(String login, String password){
        this.login=login;
        this.password=password;
    }

    public LoginDataJSON(String login, String password, String email){
        this.login=login;
        this.password=password;
        this.email=email;
    }

}

