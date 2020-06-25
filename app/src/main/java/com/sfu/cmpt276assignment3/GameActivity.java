package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sfu.cmpt276assignment3.Model.GameData;
import com.sfu.cmpt276assignment3.Model.GameModel;
import com.sfu.cmpt276assignment3.Model.MusicManager;

import java.util.Random;

import static com.sfu.cmpt276assignment3.Model.MusicManager.GAME_MUSIC;

public class GameActivity extends AppCompatActivity {
    GameData gameData;
    MusicManager musicManager;
    boolean keepPlaying = false;

    int numCols;
    int numRows;
    int numSubmarines;

    TextView missileInfo;
    TextView subInfo;
    LinearLayout grid;
    GameModel game;
    FrameLayout gameOverBox;
    FrameLayout gamePauseBox;
    Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // get game configuration info
        gameData = GameData.getInstance(this);
        numSubmarines = gameData.getNumSubmarines();
        numRows = gameData.getNumRows();
        numCols = gameData.getNumCols();
        // create a game model
        game = new GameModel(numRows, numCols, numSubmarines);

        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(GAME_MUSIC);
        setupViews();
        setupGrid();
        setupBackArrow();
        displayGamesPlayed();
    }

    private void displayGamesPlayed() {
        TextView textGamesPlayed = findViewById(R.id.text_games_played);
        int gamesPlayed = gameData.getNumGamesPlayed();
        String games_played_string = getResources().getString(R.string.info_games_played, gamesPlayed);
        textGamesPlayed.setText(games_played_string);
    }

    private void setupViews() {
        gameOverBox = findViewById(R.id.game_over_box);
        gamePauseBox = findViewById(R.id.game_pause_box);
        missileInfo = findViewById(R.id.text_missiles_information);
        subInfo = findViewById(R.id.text_submarines_information);
        updateInfoBox();
    }
    private void updateInfoBox() {
        String missileInfoString = getResources().getString(R.string.game_missile_info, game.getMissiles_wasted());
        String subInfoString = getResources().getString(R.string.game_submarines_info,
                game.getSubmarines_destroyed(), numSubmarines);
        missileInfo.setText(missileInfoString);
        subInfo.setText(subInfoString);
    }

    private void setupBackArrow() {
        ImageButton backArrow = findViewById(R.id.back_arrow);
        backArrow.setImageResource(R.drawable.arrow_animation);
        AnimationDrawable moveAnimation = (AnimationDrawable)backArrow.getDrawable();
        moveAnimation.start();
    }
    private void setupGrid() {
        buttons = new Button[numRows][numCols];
        grid = findViewById(R.id.game_grid);
        for (int row = 0; row < numRows; row++) {
            LinearLayout tableRow = new LinearLayout(this);
            tableRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            grid.addView(tableRow);
            for (int col = 0; col < numCols; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                // debugging
                /*if (isSubmarine[row][col]) { button.setBackground(getResources().getDrawable(R.drawable.grid_element_with_submarine)); }
                else { button.setBackground(getResources().getDrawable(R.drawable.grid_element)); }*/
                // non-debugging
                button.setBackground(getResources().getDrawable(R.drawable.grid_element));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });
                buttons[row][col] = button;
                tableRow.addView(button);
            }
        }
    }

    private void gridButtonClicked(int row, int col) {
        if (game.isGameOver() || game.isPaused()) {
            return;
        }
        boolean isSubmarine = game.fireMissile(row, col);
        if (isSubmarine) {
            setBackground(row, col, R.drawable.grid_element_destroyed_submarine);
        }
        updateHiddenNumbers();

        updateInfoBox();
        if (game.isGameOver()) {
            showGameOverBox();
        }
    }

    private void showGameOverBox() {
        gameOverBox.setVisibility(View.VISIBLE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.VISIBLE);

        ObjectAnimator.ofFloat(gameOverBox, View.TRANSLATION_X, -2000, 0).setDuration(2000).start();
        // get high score
        int high_score = gameData.getHighScore();
        int score = game.getMissiles_wasted();
        if (score < high_score || high_score == -1) {
            gameData.setHighScore(score);
            high_score = score;
        }

        TextView textScore = findViewById(R.id.text_score);
        TextView textHighScore = findViewById(R.id.text_high_score);
        TextView textHighScoreInfo = findViewById(R.id.text_high_score_info);
        textScore.setText(getResources().getString(R.string.game_over_score, score));
        textHighScore.setText(getResources().getString(R.string.game_over_high_score, high_score));
        textHighScoreInfo.setText(getResources().getString(R.string.game_over_high_score_info,
                numRows, numCols, numSubmarines));

        gameData.setNumGamesPlayed(gameData.getNumGamesPlayed() + 1);
    }

    private void displayHiddenNumber(int row, int col) {
        Button button = buttons[row][col];
        int num_hidden = game.getHiddenCount(row, col);
        button.setText(Integer.toString(num_hidden));
        button.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        button.setTextSize(48);
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_motion_control_bold), Typeface.BOLD);
    }
    private void updateHiddenNumbers() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (game.isNumberDisplayed(row,col)) {
                    displayHiddenNumber(row, col);
                }
            }
        }
    }

    private void setBackground(int row, int col, int drawable_resource) {
        Button button = buttons[row][col];

        // Lock buttons
        lockButtonSizes();
        int width = button.getWidth();
        int height = button.getHeight();

        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(drawable_resource);
        layerDrawable.setBounds(0, 0, width, height);
        // already to scale because you create a bitmap with this size
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        layerDrawable.draw(new Canvas(bitmap));
        button.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void lockButtonSizes() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Button button = buttons[row][col];
                button.setPadding(0, 0, 0, 0);
                int width = button.getWidth();
                int height = button.getHeight();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    public void saveGame(View view) {
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    public void clickBackArrow(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        if (!game.isGameOver() && !game.isPaused()) {
            game.pause();
            pauseScreen();
        }
    }
    private void pauseScreen() {
        gamePauseBox.setVisibility(View.VISIBLE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.VISIBLE);
    }
    public void continueGame(View view) {
        game.resume();
        gamePauseBox.setVisibility(View.GONE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.GONE);
    }

    public void goHome(View view) {
        leaveScreen();
    }
    public void leaveScreen() {
        //keepPlaying = true;
        finish();
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
        musicManager.startMusic(GAME_MUSIC);
    }

}
