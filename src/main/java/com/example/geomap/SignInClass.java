package com.example.geomap;


public class SignInClass {

    String FullName,Email,Phone,Username,Password;

    public SignInClass() {
    }

    public SignInClass(String fullName, String email, String phone, String username, String password) {
        FullName = fullName;
        Email = email;
        Phone = phone;
        Username = username;
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}



