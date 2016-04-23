package org.sugr.androidhlsstreaming.api;

public class UserCreateState {
    public String email;
    public int state;

    public static class State {
        public static final int PENDING_ACTIVATION = 1;
        public static final int WAITING_FOR_ACTIVATION = 2;
        public static final int ALREADY_EXISTS = 3;
        public static final int CREATED = 4;
    }

    public UserCreateState(String email) {
        this.email = email;
    }
}
