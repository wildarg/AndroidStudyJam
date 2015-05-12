package com.homesoftwaretools.portmone;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.homesoftwaretools.portmone.activities.JournalActivity;
import com.homesoftwaretools.portmone.fragments.LoginFragmentDialog;
import com.homesoftwaretools.portmone.fragments.SignInFragmentDialog;
import com.homesoftwaretools.portmone.security.AuthorithationManager;


public class StartActivity extends AppCompatActivity {

    private Animation showButtonAnimation;
    private View loginButton;
    private View regButton;
    private Animation showDelayButtonAnimation;
    private Animation slowLRAnimation;
    private View appName;
    private Animation slowRLAnimation;
    private View appDescription;

    public static void show(Context context) {
        Intent intent = new Intent(context, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        showButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_show);
        showDelayButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_delay_show);
        slowLRAnimation = AnimationUtils.loadAnimation(this, R.anim.slow_lr_translate);
        slowRLAnimation = AnimationUtils.loadAnimation(this, R.anim.slow_rl_translate);
        loginButton = findViewById(R.id.loginButtonTextView);
        loginButton.setOnClickListener(new LoginButtonClickListener());
        regButton = findViewById(R.id.regButtonTextView);
        regButton.setOnClickListener(new SignInButtonClickListener());
        appName = findViewById(R.id.appNameTextView);
        appDescription = findViewById(R.id.appDescriptionTextView);

        AuthorithationManager manager = AuthorithationManager.getInstance(this);
        if (manager.isLoggedIn()) {
            JournalActivity.show(this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appName.startAnimation(slowLRAnimation);
        appDescription.startAnimation(slowRLAnimation);
        regButton.startAnimation(showButtonAnimation);
        loginButton.startAnimation(showDelayButtonAnimation);
    }

    private class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LoginFragmentDialog dialog = new LoginFragmentDialog();
            dialog.show(getFragmentManager(), "Login");
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private class SignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SignInFragmentDialog dialog = new SignInFragmentDialog();
            dialog.show(getFragmentManager(), "SignIn");
        }
    }
}
