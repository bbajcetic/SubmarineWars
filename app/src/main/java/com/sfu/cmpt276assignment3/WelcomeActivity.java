package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonSkip = findViewById(R.id.button_skip);
    }

    public void skipToMainMenu(View view) {
        Intent mainMenuIntent = MainMenuActivity.makeIntent(this);
        startActivity(mainMenuIntent);
        finish();
    }
}
