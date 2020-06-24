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
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static com.sfu.cmpt276assignment3.MusicManager.MENU_MUSIC;

public class OptionsActivity extends AppCompatActivity {

    GameData gameData;
    MusicManager musicManager;
    boolean keepPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        gameData = GameData.getInstance(getApplicationContext());
        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(MENU_MUSIC);
        setupBackArrow();
        createRadioButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicManager.pauseMusic(keepPlaying);
        keepPlaying = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        musicManager.resumeMusic(MENU_MUSIC);
    }


    private String getBoardSizeString(int numRows, int numCols) {
        return String.format("%dx%d", numRows, numCols); // rows x columns
    }
    private String getNumberOfSubmarinesString(int numSubmarines) {
        return String.format("%d subs", numSubmarines);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, OptionsActivity.class);
        return intent;
    }

    public void clickBackArrow(View view) {
        keepPlaying = true;
        finish();
    }

    private void setupBackArrow() {
        ImageButton backArrow = findViewById(R.id.back_arrow);
        backArrow.setImageResource(R.drawable.arrow_animation);
        AnimationDrawable moveAnimation = (AnimationDrawable)backArrow.getDrawable();
        moveAnimation.start();
    }

    public void resetData(View view) {
        gameData.resetData(this);
        Snackbar snackbar = Snackbar.make(view, "Number of games played and high scores have been reset", BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }

    @SuppressLint("RestrictedApi")
    private void createRadioButtons() {
        int[] numSubmarinesOptions = this.getResources().getIntArray(R.array.num_submarines_options);
        int[] numRowsOptions = this.getResources().getIntArray(R.array.num_rows_options);
        int[] numColsOptions = this.getResources().getIntArray(R.array.num_cols_options);
        // used to change radio button selected color
        ColorStateList colorStateList=  new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_enabled}},
                new int[] {Color.WHITE}
        );

        RadioGroup group = findViewById(R.id.radio_group_board_size);
        // Create the radio buttons
        for (int i = 0; i < numColsOptions.length; i++) {
            final int numRows = numRowsOptions[i];
            final int numCols = numColsOptions[i];
            AppCompatRadioButton button = new AppCompatRadioButton(this);
            button.setText(getBoardSizeString(numRows, numCols));
            button.setTextColor(getResources().getColor(android.R.color.white));
            button.setSupportButtonTintList(colorStateList);
            button.setTypeface(ResourcesCompat.getFont(this, R.font.font_laconic_bold));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameData.setBoardSize(numRows, numCols);
                }
            });
            group.addView(button);

            // Select default button:
            if (numCols == gameData.getNumCols()) {
                button.setChecked(true);
            }
        }

        group = findViewById(R.id.radio_group_number_of_submarines);
        // Create the radio buttons
        for (int i = 0; i < numSubmarinesOptions.length; i++) {
            final int numSubmarines = numSubmarinesOptions[i];
            AppCompatRadioButton button = new AppCompatRadioButton(this);
            button.setText(getNumberOfSubmarinesString(numSubmarines));
            button.setTextColor(getResources().getColor(android.R.color.white));
            button.setSupportButtonTintList(colorStateList);
            button.setTypeface(ResourcesCompat.getFont(this, R.font.font_laconic_bold));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameData.setNumSubmarines(numSubmarines);
                }
            });
            group.addView(button);

            if (numSubmarines == gameData.getNumSubmarines()) {
                button.setChecked(true);
            }
        }
    }
}
