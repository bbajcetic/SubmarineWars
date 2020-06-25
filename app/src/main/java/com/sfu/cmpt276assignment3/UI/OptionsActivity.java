package com.sfu.cmpt276assignment3.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sfu.cmpt276assignment3.Model.GameData;
import com.sfu.cmpt276assignment3.Model.MusicManager;
import com.sfu.cmpt276assignment3.R;

import static com.sfu.cmpt276assignment3.Model.MusicManager.MENU_MUSIC;

/**
 * This class is the Options Activity UI: responsible for displaying the options to the player and
 * allowing them to adjust them. Uses the GameData class to change and retrieve the options
 */
public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    GameData gameData;
    MusicManager musicManager;
    boolean keepPlaying = false;

    boolean isBoxOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        gameData = GameData.getInstance(getApplicationContext());
        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(MENU_MUSIC);
        setupBackArrow();
        createRadioButtons();
        findViewById(R.id.reset_high_scores).setOnClickListener(this);
        findViewById(R.id.confirm_yes).setOnClickListener(this);
        findViewById(R.id.confirm_no).setOnClickListener(this);
        findViewById(R.id.show_high_scores).setOnClickListener(this);
        findViewById(R.id.hide_high_scores).setOnClickListener(this);
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

    public void resetData() {
        closeConfirmBox();
        gameData.resetData(this);
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), R.string.options_reset_message, BaseTransientBottomBar.LENGTH_SHORT);
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
                    if (!isBoxOpen) {
                        gameData.setBoardSize(numRows, numCols);
                    }
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
                    if (!isBoxOpen) {
                        gameData.setNumSubmarines(numSubmarines);
                    }
                }
            });
            group.addView(button);

            if (numSubmarines == gameData.getNumSubmarines()) {
                button.setChecked(true);
            }
        }
    }
    private void openConfirmBox() {
        if (isBoxOpen) { return; }
        isBoxOpen = true;
        disableRadioButtons();
        Log.d("TAG", "openConfirmBox");
        FrameLayout confirmBox = findViewById(R.id.options_confirm_reset);
        confirmBox.setVisibility(View.VISIBLE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.VISIBLE);
    }
    public void closeConfirmBox() {
        isBoxOpen = false;
        enableRadioButtons();
        Log.d("TAG", "closeConfirmBox");
        FrameLayout confirmBox = findViewById(R.id.options_confirm_reset);
        confirmBox.setVisibility(View.GONE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.GONE);
    }

    public void setRadioButtons(boolean enable) {
        RadioGroup boardSizeGroup = findViewById(R.id.radio_group_board_size);
        RadioGroup numSubmarinesGroup = findViewById(R.id.radio_group_number_of_submarines);
        for(int i = 0; i < boardSizeGroup.getChildCount(); i++){
            boardSizeGroup.getChildAt(i).setEnabled(enable);
        }
        for(int i = 0; i < numSubmarinesGroup.getChildCount(); i++){
            numSubmarinesGroup.getChildAt(i).setEnabled(enable);
        }
    }
    public void enableRadioButtons() {
        setRadioButtons(true);
    }
    public void disableRadioButtons() {
        setRadioButtons(false);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.reset_high_scores:
                openConfirmBox();
                break;
            case R.id.confirm_yes:
                resetData();
                break;
            case R.id.confirm_no:
                closeConfirmBox();
                break;
            case R.id.show_high_scores:
                openHighScoresBox();
                break;
            case R.id.hide_high_scores:
                closeHighScoresBox();
                break;
        }
    }
    private void openHighScoresBox() {
        if (isBoxOpen) { return; }
        isBoxOpen = true;
        disableRadioButtons();
        Log.d("TAG", "openHighScoresBox");

        FrameLayout highScoreBox = findViewById(R.id.options_show_high_scores);
        TextView highScoreContent = findViewById(R.id.high_score_content);
        String highScores = getHighScores();
        highScoreContent.setText(highScores);
        highScoreBox.setVisibility(View.VISIBLE);

        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.VISIBLE);
    }

    private String getHighScores() {
        String highScores = "";

        int[] numSubmarinesOptions = this.getResources().getIntArray(R.array.num_submarines_options);
        int[] numRowsOptions = this.getResources().getIntArray(R.array.num_rows_options);
        int[] numColsOptions = this.getResources().getIntArray(R.array.num_cols_options);
        for (int i = 0; i < numRowsOptions.length; i++) {
            for (int j = 0; j < numSubmarinesOptions.length; j++) {
                int highScore = gameData.getHighScore(numSubmarinesOptions[j], numRowsOptions[i], numColsOptions[i]);
                if (highScore == -1) {
                    highScores += getResources().getString(R.string.options_no_score_format,
                            numRowsOptions[i], numColsOptions[i], numSubmarinesOptions[j]);
                } else {
                    highScores += getResources().getString(R.string.options_high_score_format,
                            highScore, numRowsOptions[i], numColsOptions[i], numSubmarinesOptions[j]);
                }
                highScores += (i == numRowsOptions.length-1 && j == numSubmarinesOptions.length-1) ? "" : "\n";
            }
        }
        return highScores;
    }

    public void closeHighScoresBox() {
        isBoxOpen = false;
        enableRadioButtons();
        Log.d("TAG", "closeHighScoresBox");
        FrameLayout highScoreBox = findViewById(R.id.options_show_high_scores);
        highScoreBox.setVisibility(View.GONE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.GONE);
    }
}
