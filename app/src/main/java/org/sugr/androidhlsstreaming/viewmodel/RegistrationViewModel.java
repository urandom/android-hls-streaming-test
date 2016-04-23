package org.sugr.androidhlsstreaming.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.AuthService;
import org.sugr.androidhlsstreaming.validation.Validator;

public class RegistrationViewModel implements ViewModel {
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordConfirm = new ObservableField<>("");

    public ObservableBoolean emailValid = new ObservableBoolean(false);
    public ObservableBoolean passwordValid = new ObservableBoolean(false);
    public ObservableBoolean passwordConfirmValid = new ObservableBoolean(false);

    public ObservableField<String> loginError = new ObservableField<>(null);
    public ObservableField<String> passwordError = new ObservableField<>(null);

    private Context context;
    private AuthService service;
    private LoginActivator activator;

    public interface LoginActivator {
        void activateRegistration(String email);
    }

    public RegistrationViewModel(Context context, LoginActivator activator, AuthService service) {
        this.context = context;
        this.activator = activator;
        this.service = service;

        setupValidation();
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
                } else if (passwordConfirm == observable) {
                    passwordConfirmValid.set(Validator.passwordMatch(passwordConfirm.get(), password.get()));

                    passwordError.set(passwordConfirmValid.get()
                            ? null : context.getString(R.string.registration_password_mismatch));
                }
            }
        };

        email.addOnPropertyChangedCallback(validator);
        password.addOnPropertyChangedCallback(validator);
    }

}
