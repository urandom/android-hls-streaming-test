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
        return Single.just("foo@example.com".equals(email));
    }

    @Override
    public Single<Void> createUser(@Body UserData userData) {
        return Single.just(null);
    }

    @Override
    public Single<String> getMedia(@Path("email") String email) {
        return Single.just("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
    }
}
