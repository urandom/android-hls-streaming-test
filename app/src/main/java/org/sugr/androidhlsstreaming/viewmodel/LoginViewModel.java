package org.sugr.androidhlsstreaming.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.AuthService;
import org.sugr.androidhlsstreaming.validation.Validator;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginViewModel implements ViewModel {
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableBoolean emailValid = new ObservableBoolean(false);
    public ObservableBoolean passwordValid = new ObservableBoolean(false);

    public ObservableField<String> loginError = new ObservableField<>(null);

    private Context context;
    private AuthService service;
    private LoginActivator activator;

    public interface LoginActivator {
        void activateLogin(String email);
        void activateRegistration();
    }

    public LoginViewModel(Context context, LoginActivator activator, AuthService service) {
        this.context = context;
        this.activator = activator;
        this.service = service;

        setupValidation();
    }

    public void onLoginSubmit(View unused) {
        service.getUser(email.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(email -> {
                    // Make sure the context is still alive
                    if (context != null && activator != null) {
                        if (email != null) {
                            loginError.set(null);
                            activator.activateLogin(email);
                        } else {
                            loginError.set(context.getString(R.string.login_invalid));
                        }
                    }
                }, err -> {
                    if (context != null) {
                        loginError.set(context.getString(R.string.login_network_error));
                    }
                    err.printStackTrace();
                });
    }

    public void onRegistrationSubmit(View unused) {
        if (this.activator != null) {
            this.activator.activateRegistration();
        }
    }

    @Override
    public void destroy() {
        context = null;
        activator = null;
    }

    private void setupValidation() {
        Observable.OnPropertyChangedCallback validator = new Observable.OnPropertyChangedCallback() {
            @Override public void onPropertyChanged(Observable observable, int i) {
                if (email == observable) {
                    emailValid.set(Validator.email(email.get()));
                } else if (password == observable) {
                    passwordValid.set(Validator.password(password.get()));
                }
            }
        };

        email.addOnPropertyChangedCallback(validator);
        password.addOnPropertyChangedCallback(validator);
    }

}
