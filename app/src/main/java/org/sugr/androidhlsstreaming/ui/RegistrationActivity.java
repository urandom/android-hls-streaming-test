package org.sugr.androidhlsstreaming.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.MockAuthService;
import org.sugr.androidhlsstreaming.api.UserCreateState;
import org.sugr.androidhlsstreaming.databinding.RegistrationActivityBinding;
import org.sugr.androidhlsstreaming.viewmodel.RegistrationViewModel;

public class RegistrationActivity extends AppCompatActivity implements RegistrationViewModel.RegistrationActivator {
    private RegistrationActivityBinding binding;
    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.registration_activity);
        viewModel = new RegistrationViewModel(this, this, new MockAuthService());

        binding.setViewModel(viewModel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.destroy();
    }

    @Override
    public void activateRegistration(String email, int state) {
        switch (state) {
            case UserCreateState.State.PENDING_ACTIVATION:
            case UserCreateState.State.WAITING_FOR_ACTIVATION:
                startActivity(LoginActivity.intent(this, email, state));
                break;
            default:
                startActivity(LoginActivity.intent(this));
                break;
        }
    }

    public static Intent intent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}
