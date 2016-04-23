package org.sugr.androidhlsstreaming.validation;

import android.text.TextUtils;

public class Validator {
    public static boolean email(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean password(String password) {
        return !TextUtils.isEmpty(password) && password.length() > 4;
    }

    public static boolean passwordMatch(String password, String control) {
        return password(password) && password.equals(control);
    }
}
