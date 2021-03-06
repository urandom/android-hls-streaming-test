package org.sugr.androidhlsstreaming.api;

import org.sugr.androidhlsstreaming.api.args.UserData;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Single;

public interface AuthService {
    @GET("user/{email}")
    Single<String> getUser(@Path("email") String email, @Header("Authorization") String authorization);

    @POST("user")
    Single<UserCreateState> createUser(@Body UserData userData);

    @GET("user/{email}/media")
    Single<String> getMedia(@Path("email") String email, @Header("Authorization") String authorization);

    class AuthException extends RuntimeException {}
}
