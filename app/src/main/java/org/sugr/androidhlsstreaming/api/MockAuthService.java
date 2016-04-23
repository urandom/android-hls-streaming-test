package org.sugr.androidhlsstreaming.api;

import org.sugr.androidhlsstreaming.api.args.UserData;

import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Single;

public class MockAuthService implements AuthService {
    public MockAuthService() {
    }

    @Override
    public Single<Boolean> getUser(@Path("email") String email) {
        return null;
    }

    @Override
    public Single<Boolean> createUser(@Body UserData userData) {
        return null;
    }

    @Override
    public Single<String> getMedia(@Path("email") String email) {
        return null;
    }
}
