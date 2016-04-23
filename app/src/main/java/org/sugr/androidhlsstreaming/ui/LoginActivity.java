package org.sugr.androidhlsstreaming.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.MockAuthService;
import org.sugr.androidhlsstreaming.api.UserCreateState;
import org.sugr.androidhlsstreaming.databinding.LoginActivityBinding;
import org.sugr.androidhlsstreaming.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements LoginViewModel.LoginActivator {
    private LoginActivityBinding binding;
    private LoginViewModel viewModel;

    private static final String ARG_EMAIL = "email";
    private static final String ARG_STATE = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        viewModel = new LoginViewModel(this, this, new MockAuthService());

        binding.setViewModel(viewModel);

        if (getIntent().hasExtra(ARG_EMAIL)) {
            String email = getIntent().getStringExtra(ARG_EMAIL);
            String msg;

            if (getIntent().getIntExtra(ARG_STATE, UserCreateState.State.PENDING_ACTIVATION) == UserCreateState.State.PENDING_ACTIVATION) {
                Snackbar.make(binding.loginForm, getString(R.string.registration_activating, email), Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.loginForm, getString(R.string.registration_resending_activation, email), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.destroy();
    }

    @Override
    public void activateLogin(String email) {
    }

    @Override
    public void activateRegistration() {
        startActivity(RegistrationActivity.intent(this));
    }

    public static Intent intent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent intent(Context context, @NonNull String email, int state) {
        Intent intent = intent(context);
        intent.putExtra(ARG_EMAIL, email);
        intent.putExtra(ARG_STATE, state);

        return intent;
    }

}

