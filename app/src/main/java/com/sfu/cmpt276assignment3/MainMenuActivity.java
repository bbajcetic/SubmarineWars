package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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
}
