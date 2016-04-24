package org.sugr.androidhlsstreaming.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.sugr.androidhlsstreaming.R;
import org.sugr.androidhlsstreaming.api.MockAuthService;
import org.sugr.androidhlsstreaming.databinding.MediaActivityBinding;
import org.sugr.androidhlsstreaming.viewmodel.MediaViewModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MediaActivity extends AppCompatActivity {
    private MediaActivityBinding binding;
    private MediaViewModel viewModel;

    private static final String ARG_EMAIL = "email";
    private static final String ARG_AUTH = "auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.media_activity);
        viewModel = new MediaViewModel(this, intent.getStringExtra(ARG_EMAIL),
                intent.getStringExtra(ARG_AUTH), new MockAuthService(), binding);

        binding.setViewModel(viewModel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.destroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent intent(Context context, String email, String auth) {
        Intent intent = new Intent(context, MediaActivity.class);
        intent.putExtra(ARG_EMAIL, email);
        intent.putExtra(ARG_AUTH, auth);

        return intent;
    }
}
