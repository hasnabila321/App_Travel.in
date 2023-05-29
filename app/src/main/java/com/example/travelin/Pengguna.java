package com.example.travelin;

public class Pengguna {
    public String email, password,confirmpassword;
    private String key;

    public Pengguna() {
    }

    public Pengguna(String email, String password,String confirmpassword){
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmPassword(String password) {
        this.confirmpassword = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
