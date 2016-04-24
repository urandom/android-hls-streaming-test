package org.sugr.androidhlsstreaming.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;

import org.sugr.androidhlsstreaming.api.AuthService;
import org.sugr.androidhlsstreaming.databinding.MediaActivityBinding;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaViewModel implements ViewModel {
    public ObservableBoolean loading = new ObservableBoolean(true);
    public ObservableField<Uri> videoURI = new ObservableField<>();

    private AppCompatActivity activity;
    private String email;
    private String auth;
    private AuthService service;
    private MediaActivityBinding binding;
    private MediaController mediaController;
    private boolean toolbarVisible = true;

    public MediaViewModel(AppCompatActivity activity, String email, String auth, AuthService service, MediaActivityBinding binding) {
        this.activity = activity;
        this.email = email;
        this.auth = auth;
        this.service = service;
        this.binding = binding;
        this.mediaController = new MediaController(activity);

        mediaController.setMediaPlayer(binding.video);
        binding.video.setMediaController(mediaController);
        binding.video.requestFocus();
        binding.video.setOnPreparedListener(mp -> {
            loading.set(false);
            hideToolbar();
        });
        binding.video.setOnTouchListener((v, event) -> {
            if (toolbarVisible) {
                hideToolbar();
            } else {
                showToolbar();
            }
            return false;
        });

        fetchVideo();
    }

    @Override
    public void destroy() {
        activity = null;
        service = null;
        binding = null;
        mediaController = null;
    }

    private void showToolbar() {
        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.show();
            toolbarVisible = true;
        }

    }

    private void hideToolbar() {
        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.hide();
            toolbarVisible = false;
        }
    }

    private void fetchVideo() {
        service.getMedia(email, auth).map(Uri::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    videoURI.set(uri);
                    binding.video.start();
                }, err -> {
                    loading.set(false);
                    System.out.println(err);
                });
    }
}
