package org.sugr.androidhlsstreaming.api.args;

public class UserData {
    public String email;
    public String password;
    public boolean activated;

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
