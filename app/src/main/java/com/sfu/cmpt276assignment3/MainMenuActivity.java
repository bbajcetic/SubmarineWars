package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        displayOptions();
        setupButtons();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButtons() {
        Button playButton = findViewById(R.id.button_play);
        Button optionsButton = findViewById(R.id.button_options);
        Button helpButton = findViewById(R.id.button_help);
        ButtonTouchListener buttonTouchListener = new ButtonTouchListener();
        playButton.setOnTouchListener(buttonTouchListener);
        optionsButton.setOnTouchListener(buttonTouchListener);
        helpButton.setOnTouchListener(buttonTouchListener);
    }

    public class ButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundResource(R.drawable.menu_button_selected);
                    return false;
                case MotionEvent.ACTION_UP:
                    v.setBackgroundResource(R.drawable.menu_button);
                    return false;
            }
            return false;
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        return intent;
    }

    public void playGame(View view) {
        Intent gameIntent = GameActivity.makeIntent(this);
        startActivity(gameIntent);
    }
    public void openOptions(View view) {
        Intent optionsIntent = OptionsActivity.makeIntent(this);
        startActivity(optionsIntent);
    }

    public void openHelp(View view) {
        Intent helpIntent = HelpActivity.makeIntent(this);
        startActivity(helpIntent);
    }

    private void displayOptions() {
        int x = OptionsActivity.getBoardSizeX(this);
        int y = OptionsActivity.getBoardSizeY(this);
        int numSubmarines = OptionsActivity.getNumberOfSubmarines(this);
        String toastString = String.format("Board size: %dx%d\nNumber of submarines: %d", x, y, numSubmarines);
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }
}
