package org.sugr.androidhlsstreaming.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.AuthService;
import org.sugr.androidhlsstreaming.api.UserCreateState;
import org.sugr.androidhlsstreaming.api.args.UserData;
import org.sugr.androidhlsstreaming.validation.Validator;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegistrationViewModel implements ViewModel {
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordConfirm = new ObservableField<>("");

    public ObservableBoolean emailValid = new ObservableBoolean(false);
    public ObservableBoolean passwordValid = new ObservableBoolean(false);
    public ObservableBoolean passwordConfirmValid = new ObservableBoolean(false);
    public ObservableBoolean working = new ObservableBoolean(false);

    public ObservableField<String> registrationError = new ObservableField<>(null);
    public ObservableField<String> passwordError = new ObservableField<>(null);

    private Context context;
    private AuthService service;
    private RegistrationActivator activator;

    public interface RegistrationActivator {
        void activateRegistration(String email, boolean requiresActivation);
    }

    public RegistrationViewModel(Context context, RegistrationActivator activator, AuthService service) {
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

    public void onRegistrationSubmit(View unused) {
        working.set(true);
        service.createUser(new UserData(email.get(), password.get()))
                .map(state -> {
                    switch (state.state) {
                        case UserCreateState.State.CREATED:
                            return new RegistrationPayload(state.email, true);
                        case UserCreateState.State.PENDING_ACTIVATION:
                            return new RegistrationPayload(state.email, false);
                        case UserCreateState.State.ALREADY_EXISTS:
                            throw new AlreadyExistsException();
                        default:
                            throw new UnknownStateException();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(payload -> {
                    working.set(false);
                    if (this.activator != null) {
                        registrationError.set(null);
                        this.activator.activateRegistration(payload.email, payload.requiresActivation);
                    }
                }, err -> {
                    err.printStackTrace();
                    working.set(false);
                    if (context != null) {
                        if (err instanceof AlreadyExistsException) {
                            registrationError.set(context.getString(R.string.registration_user_exists));
                        } else if (err instanceof UnknownStateException) {
                            registrationError.set(context.getString(R.string.registration_unknown_state));
                        } else {
                            registrationError.set(context.getString(R.string.registration_network_error));
                        }
                    }
                });
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

    private class RegistrationPayload {
        String email;
        boolean requiresActivation;

        public RegistrationPayload(String email, boolean requiresActivation) {
            this.email = email;
            this.requiresActivation = requiresActivation;
        }

    }

    private class AlreadyExistsException extends RuntimeException {}
    private class UnknownStateException extends RuntimeException {}
}
