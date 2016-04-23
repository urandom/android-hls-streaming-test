package org.sugr.androidhlsstreaming.api;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Builder {
    private Retrofit.Builder builder;

    public Builder() {
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public <T> T build(Class<T> cls) {
        return builder.build().create(cls);
    }
}
