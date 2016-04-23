package org.sugr.androidhlsstreaming.api;

import org.sugr.androidhlsstreaming.api.args.UserData;

import java.util.Random;

import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Single;

public class MockAuthService implements AuthService {
    private Random rnd;

    public MockAuthService() {
        rnd = new Random();
    }

    @Override
    public Single<String> getUser(@Path("email") String email) {
        if ("foo@example.com".equals(email)) {
            return Single.just(email);
        } else {
            return Single.just(null);
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
    public Single<String> getMedia(@Path("email") String email) {
        return Single.just("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
    }
}
