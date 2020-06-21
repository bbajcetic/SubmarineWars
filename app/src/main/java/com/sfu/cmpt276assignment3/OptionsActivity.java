package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String SETTINGS_FILE = "settings_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        prefs = this.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);

        createRadioButtons();
    }

    private void createRadioButtons() {
        int[] number_of_mines_options = this.getResources().getIntArray(R.array.number_of_mines_options);
        int[] board_size_options_x = this.getResources().getIntArray(R.array.board_size_options_x);
        int[] board_size_options_y = this.getResources().getIntArray(R.array.board_size_options_y);

        RadioGroup group = findViewById(R.id.radio_group_board_size);
        // Create the radio buttons
        for (int i = 0; i < board_size_options_x.length; i++) {
            final int boardX = board_size_options_x[i];
            final int boardY = board_size_options_y[i];
            RadioButton button = new RadioButton(this);
            button.setText(getBoardSizeString(boardX, boardY));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveBoardSize(boardX, boardY);
                }
            });
            group.addView(button);

            // Select default button:
            if (boardX == getBoardSizeX(this)) {
                button.setChecked(true);
            }
        }

        group = findViewById(R.id.radio_group_number_of_mines);
        // Create the radio buttons
        for (int i = 0; i < number_of_mines_options.length; i++) {
            final int numMines = number_of_mines_options[i];
            RadioButton button = new RadioButton(this);
            button.setText(getNumberOfMinesString(numMines));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNumberOfMines(numMines);
                }
            });
            group.addView(button);

            if (numMines == getNumberOfMines(this)) {
                button.setChecked(true);
            }
        }
    }

    private String getBoardSizeString(int boardX, int boardY) {
        return String.format("%dx%d", boardX, boardY);
    }
    private String getNumberOfMinesString(int numMines) {
        return String.format("%d mines", numMines);
    }

    private void saveBoardSize(int boardX, int boardY) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("board_x", boardX);
        editor.putInt("board_y", boardY);
        editor.apply();
    }
    private void saveNumberOfMines(int numMines) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("num_mines", numMines);
        editor.apply();
    }

    static public int getNumberOfMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        int defaultNumMines = context.getResources().getInteger(R.integer.default_number_of_mines);
        return prefs.getInt("num_mines", defaultNumMines);
    }
    static public int getBoardSizeX(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        int defaultBoardSizeX = context.getResources().getInteger(R.integer.default_board_size_x);
        return prefs.getInt("board_x", defaultBoardSizeX);
    }
    static public int getBoardSizeY(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        int defaultBoardSizeY = context.getResources().getInteger(R.integer.default_board_size_y);
        return prefs.getInt("board_y", defaultBoardSizeY);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, OptionsActivity.class);
        return intent;
    }

}
