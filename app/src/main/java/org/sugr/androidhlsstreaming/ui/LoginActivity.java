package org.sugr.androidhlsstreaming.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.databinding.LoginActivityBinding;
import org.sugr.androidhlsstreaming.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        viewModel = new LoginViewModel(this);

        binding.setViewModel(viewModel);
    }

}

