package com.notmiyouji.newsapp.java.Global.Signed;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

public class SignInForm extends AppCompatActivity {

    Button SignUpBtn, forgotpassbtn;
    Intent intent;
    LoadFollowLanguageSystem loadFollowLanguageSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        SignUpBtn = findViewById(R.id.SignUpBtn);
        SignUpBtn.setOnClickListener(v -> {
            intent = new Intent(this, SignUpForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        //Forgot Password form only access from Sign In form
        forgotpassbtn = findViewById(R.id.ForgotPasswordBtn);
        forgotpassbtn.setOnClickListener(v -> {
            intent = new Intent(this, ForgotPasswordForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}