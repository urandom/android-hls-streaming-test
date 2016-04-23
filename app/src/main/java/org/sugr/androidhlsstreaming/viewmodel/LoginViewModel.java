package org.sugr.androidhlsstreaming.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

public class LoginViewModel implements ViewModel {
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableBoolean emailValid = new ObservableBoolean(false);
    public ObservableBoolean passwordValid = new ObservableBoolean(false);

    public ObservableField<String> loginError = new ObservableField<>(null);

    private Context context;

    public LoginViewModel(Context context) {
        this.context = context;

        setupValidation();
    }

    public void onLoginSubmit(View unused) {

    }

    public void onRegistrationSubmit(View unused) {

    }

    @Override
    public void destroy() {
    }

    private void setupValidation() {
        Observable.OnPropertyChangedCallback validator = new Observable.OnPropertyChangedCallback() {
            @Override public void onPropertyChanged(Observable observable, int i) {
                if (email == observable) {
                    emailValid.set(android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches());
                } else if (password == observable) {
                    passwordValid.set(!TextUtils.isEmpty(password.get()) && password.get().length() > 4);
                }
            }
        };

        email.addOnPropertyChangedCallback(validator);
        password.addOnPropertyChangedCallback(validator);
    }

}
