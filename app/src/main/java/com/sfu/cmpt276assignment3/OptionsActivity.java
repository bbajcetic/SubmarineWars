package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String SETTINGS_FILE = "settings_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        prefs = this.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);

        setupBackArrow();
        createRadioButtons();
    }

    @SuppressLint("RestrictedApi")
    private void createRadioButtons() {
        int[] number_of_submarines_options = this.getResources().getIntArray(R.array.number_of_submarines_options);
        int[] board_size_options_x = this.getResources().getIntArray(R.array.board_size_options_x);
        int[] board_size_options_y = this.getResources().getIntArray(R.array.board_size_options_y);
        // used to change radio button selected color
        ColorStateList colorStateList=  new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_enabled}},
                new int[] {Color.WHITE}
        );

        RadioGroup group = findViewById(R.id.radio_group_board_size);
        // Create the radio buttons
        for (int i = 0; i < board_size_options_x.length; i++) {
            final int boardX = board_size_options_x[i];
            final int boardY = board_size_options_y[i];
            //RadioButton button = new RadioButton(this);
            AppCompatRadioButton button = new AppCompatRadioButton(this);
            button.setText(getBoardSizeString(boardX, boardY));
            button.setTextColor(getResources().getColor(android.R.color.white));
            button.setSupportButtonTintList(colorStateList);
            //button.setButtonTintList(colorStateList);
            button.setTypeface(ResourcesCompat.getFont(this, R.font.font_laconic_bold));
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

        group = findViewById(R.id.radio_group_number_of_submarines);
        // Create the radio buttons
        for (int i = 0; i < number_of_submarines_options.length; i++) {
            final int numSubmarines = number_of_submarines_options[i];
            AppCompatRadioButton button = new AppCompatRadioButton(this);
            button.setText(getNumberOfSubmarinesString(numSubmarines));
            button.setTextColor(getResources().getColor(android.R.color.white));
            button.setSupportButtonTintList(colorStateList);
            button.setTypeface(ResourcesCompat.getFont(this, R.font.font_laconic_bold));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNumberOfSubmarines(numSubmarines);
                }
            });
            group.addView(button);

            if (numSubmarines == getNumberOfSubmarines(this)) {
                button.setChecked(true);
            }
        }
    }

    private String getBoardSizeString(int boardX, int boardY) {
        return String.format("%dx%d", boardX, boardY);
    }
    private String getNumberOfSubmarinesString(int numSubmarines) {
        return String.format("%d subs", numSubmarines);
    }

    private void saveBoardSize(int boardX, int boardY) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("board_x", boardX);
        editor.putInt("board_y", boardY);
        editor.apply();
    }
    private void saveNumberOfSubmarines(int numSubmarines) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("num_submarines", numSubmarines);
        editor.apply();
    }

    static public int getNumberOfSubmarines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        int defaultNumSubmarines = context.getResources().getInteger(R.integer.default_number_of_submarines);
        return prefs.getInt("num_submarines", defaultNumSubmarines);
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

    public void clickBackArrow(View view) {
        finish();
    }

    private void setupBackArrow() {
        ImageButton backArrow = findViewById(R.id.back_arrow);
        backArrow.setImageResource(R.drawable.arrow_animation);
        AnimationDrawable moveAnimation = (AnimationDrawable)backArrow.getDrawable();
        moveAnimation.start();
    }
}