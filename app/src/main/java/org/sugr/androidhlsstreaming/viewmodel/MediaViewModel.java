package org.sugr.androidhlsstreaming.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.widget.MediaController;

import org.sugr.androidhlsstreaming.api.AuthService;
import org.sugr.androidhlsstreaming.databinding.MediaActivityBinding;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaViewModel implements ViewModel {
    public ObservableBoolean loading = new ObservableBoolean(true);
    public ObservableField<Uri> videoURI = new ObservableField<>();

    private Context context;
    private String email;
    private String auth;
    private AuthService service;
    private MediaActivityBinding binding;
    private MediaController mediaController;

    public MediaViewModel(Context context, String email, String auth, AuthService service, MediaActivityBinding binding) {
        this.context = context;
        this.email = email;
        this.auth = auth;
        this.service = service;
        this.binding = binding;
        this.mediaController = new MediaController(context);

        mediaController.setMediaPlayer(binding.video);
        binding.video.setMediaController(mediaController);
        binding.video.requestFocus();

        fetchVideo();
    }

    @Override
    public void destroy() {
        context = null;
        service = null;
        binding = null;
        mediaController = null;
    }

    private void fetchVideo() {
        service.getMedia(email, auth).map(Uri::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    loading.set(false);
                    videoURI.set(uri);
                    binding.video.start();
                }, err -> {
                    loading.set(false);
                    System.out.println(err);
                });
    }
}
