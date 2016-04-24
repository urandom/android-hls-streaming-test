package org.sugr.androidhlsstreaming.api;

import org.sugr.androidhlsstreaming.api.args.UserData;

import java.util.Random;

import retrofit2.http.Body;
import rx.Single;

public class MockAuthService implements AuthService {
    private Random rnd;

    private static final String email = "foo@example.com";
    private static final String auth = email + ":foobar";

    public MockAuthService() {
        rnd = new Random();
    }

    @Override
    public Single<String> getUser(String email, String authorization) {
        if (MockAuthService.email.equals(email) && auth.equals(authorization)) {
            return Single.just(email);
        } else {
            return Single.error(new AuthException());
        }
    }

    @Override
    public Single<UserCreateState> createUser(@Body UserData userData) {
        UserCreateState state = new UserCreateState(userData.email);
        if (userData.email.equals("foo@example.com")) {
            state.state = UserCreateState.State.ALREADY_EXISTS;
        } else {
            int r = rnd.nextInt(10);

            if (r < 4) {
                state.state = UserCreateState.State.PENDING_ACTIVATION;
            } else if (r > 3 && r < 8) {
                state.state = UserCreateState.State.ALREADY_EXISTS;
            }
        }

        return Single.just(state);
    }

    @Override
    public Single<String> getMedia(String email, String authorization) {
        if (MockAuthService.email.equals(email) && auth.equals(authorization)) {
            return Single.just("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
        } else {
            return Single.error(new AuthException());
        }
    }
}
