package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        displayOptions();
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
        int numMines = OptionsActivity.getNumberOfMines(this);
        String toastString = String.format("Board size: %dx%d\nNumber of mines: %d", x, y, numMines);
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }
}
