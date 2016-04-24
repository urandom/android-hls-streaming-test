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
    public ObservableBoolean working = new ObservableBoolean(false);

    public ObservableField<String> loginError = new ObservableField<>(null);

    private Context context;
    private AuthService service;
    private LoginActivator activator;

    public interface LoginActivator {
        void activateLogin(String email, String auth);
        void activateRegistration();
    }

    public LoginViewModel(Context context, LoginActivator activator, AuthService service) {
        this.context = context;
        this.activator = activator;
        this.service = service;

        setupValidation();
    }

    public void onLoginSubmit(View unused) {
        loginError.set(null);
        working.set(true);

        String auth = email.get() + ":" + password.get();
        service.getUser(email.get(), auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(email -> {
                    working.set(false);
                    // Make sure the context is still alive
                    if (context != null && activator != null) {
                        activator.activateLogin(email, auth);
                    }
                }, err -> {
                    working.set(false);
                    err.printStackTrace();
                    if (context != null) {
                        if (err instanceof AuthService.AuthException) {
                            loginError.set(context.getString(R.string.login_invalid));
                        } else {
                            loginError.set(context.getString(R.string.login_network_error));
                        }
                    }
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
