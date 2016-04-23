package org.sugr.androidhlsstreaming.api;

public class UserCreateState {
    public String email;
    public int state;

    public static class State {
        public static final int PENDING_ACTIVATION = 1;
        public static final int ALREADY_EXISTS = 2;
        public static final int CREATED = 3;
    }

    public UserCreateState(String email) {
        this.email = email;
    }
}
