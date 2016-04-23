package org.sugr.androidhlsstreaming.api;

import org.sugr.androidhlsstreaming.api.args.UserData;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Single;

public interface AuthService {
    @GET("user/{email}")
    Single<Boolean> getUser(@Path("email") String email);

    @POST("user")
    Single<Boolean> createUser(@Body UserData userData);

    @GET("user/{email}/media")
    Single<String> getMedia(@Path("email") String email);
}
