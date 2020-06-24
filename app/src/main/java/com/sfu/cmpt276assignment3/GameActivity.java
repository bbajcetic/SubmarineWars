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

import java.util.Random;

import static com.sfu.cmpt276assignment3.MusicManager.GAME_MUSIC;

public class GameActivity extends AppCompatActivity {
    GameData gameData;
    MusicManager musicManager;
    boolean keepPlaying = false;

    FrameLayout gameOverBox;
    FrameLayout gamePauseBox;
    Button[][] buttons;

    /* Data Structures for game computation */
    // isClicked[][] indicates whether that specific spot has been clicked
    // isDoubleClicked[][] indicates whether that specific spot has been clicked twice (for submarines, since the first click doesn't scan for nearby submarines)
    // isSubmarines[x][y] indicates whether that spot in the grid has a submarine
    // submarinesInCol[x] holds the number of submarines in that column
    // submarinesInRow[x] holds the number of submarines in that row
    boolean[][] isClicked;
    boolean[][] isDoubleClicked;
    boolean[][] isSubmarine;
    int[] submarinesInCol;
    int[] submarinesInRow;

    int numCols;
    int numRows;
    int numSubmarines;

    int missiles_fired = 0;
    int submarines_destroyed = 0;

    boolean isPaused = false;

    TextView missileInfo;
    TextView subInfo;
    LinearLayout grid;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        random = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameData = GameData.getInstance(this);
        numSubmarines = gameData.getNumSubmarines();
        numRows = gameData.getNumRows();
        numCols = gameData.getNumCols();
        gameOverBox = findViewById(R.id.game_over_box);
        gamePauseBox = findViewById(R.id.game_pause_box);

        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(GAME_MUSIC);
        setupSubmarines();
        setupInfoBox();
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

    private void setupSubmarines() {
        isClicked = new boolean[numRows][numCols];
        isDoubleClicked = new boolean[numRows][numCols];
        isSubmarine = new boolean[numRows][numCols];
        submarinesInCol = new int[numCols];
        submarinesInRow = new int[numRows];
        // initialize all spots to false
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                isSubmarine[row][col] = false;
                isClicked[row][col] = false;
                isDoubleClicked[row][col] = false;
            }
        }

        // generate numSubmarines randomly in the boolean multi-array isSubmarine
        int submarinesLeft = numSubmarines;
        while (submarinesLeft > 0) {
            int randomNum = random.nextInt(numCols * numRows);
            int row = randomNum / numCols;
            int col = randomNum - (row * numCols);
            if (!isSubmarine[row][col]) {
                isSubmarine[row][col] = true;
                submarinesLeft--;
                submarinesInCol[col]++;
                submarinesInRow[row]++;
            }
        }
    }

    private void setupInfoBox() {
        missileInfo = findViewById(R.id.text_missiles_information);
        subInfo = findViewById(R.id.text_submarines_information);
        updateInfoBox();
    }
    private void updateInfoBox() {
        String missileInfoString = getResources().getString(R.string.game_missile_info);
        String subInfoString = getResources().getString(R.string.game_submarines_info);
        missileInfo.setText(missileInfoString + Integer.toString(missiles_fired));
        subInfo.setText(subInfoString + String.format("%d/%d", submarines_destroyed, numSubmarines));
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
                /*if (isSubmarine[row][col]) {
                    button.setBackground(getResources().getDrawable(R.drawable.grid_element_with_submarine));
                } else {
                    button.setBackground(getResources().getDrawable(R.drawable.grid_element));
                }*/
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
        //Toast.makeText(this, String.format("Button at %d, %d clicked", col, row), Toast.LENGTH_SHORT).show();

        if (isGameOver() || isPaused) {
            return;
        }
        if (isClicked[row][col] && !isDoubleClicked[row][col] && isSubmarine[row][col]) {
            isDoubleClicked[row][col] = true;
            displayHiddenNumber(row, col);
        }
        if (!isClicked[row][col]) {
            isClicked[row][col] = true;
            missiles_fired++;
            if(isSubmarine[row][col]) {
                submarines_destroyed++;
                setBackground(row, col, R.drawable.grid_element_destroyed_submarine);
                submarinesInRow[row]--;
                submarinesInCol[col]--;
                updateHiddenNumbers();
            } else {
                displayHiddenNumber(row, col);
            }
        }

        updateInfoBox();
        if (isGameOver()) {
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
        if (missiles_fired < high_score || high_score == -1) {
            gameData.setHighScore(missiles_fired);
            high_score = missiles_fired;
        }

        TextView textScore = findViewById(R.id.text_score);
        TextView textHighScore = findViewById(R.id.text_high_score);
        TextView textHighScoreInfo = findViewById(R.id.text_high_score_info);
        textScore.setText(getResources().getString(R.string.game_over_score, missiles_fired));
        textHighScore.setText(getResources().getString(R.string.game_over_high_score, high_score));
        textHighScoreInfo.setText(getResources().getString(R.string.game_over_high_score_info,
                numRows, numCols, numSubmarines));

        gameData.setNumGamesPlayed(gameData.getNumGamesPlayed() + 1);
    }

    private boolean isGameOver() {
        return submarines_destroyed == numSubmarines;
    }
    private void displayHiddenNumber(int row, int col) {
        Button button = buttons[row][col];
        int num_hidden = submarinesInRow[row] + submarinesInCol[col];
        button.setText(Integer.toString(num_hidden));
        button.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        button.setTextSize(48);
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_motion_control_bold), Typeface.BOLD);
    }
    private void updateHiddenNumbers() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if((isClicked[row][col] && !isSubmarine[row][col])
                        || isDoubleClicked[row][col]) {
                    displayHiddenNumber(row, col);
                }
            }
        }
    }

    private void setBackground(int row, int col, int drawable_resource) {
        Button button = buttons[row][col];
        //Log.d("TAG", "" + row + " " + col);

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
        int numCols = gameData.getNumCols();
        int numRows = gameData.getNumRows();

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
        if (!isGameOver() && !isPaused) {
            pauseScreen();
        }
    }
    private void pauseScreen() {
        isPaused = true;
        gamePauseBox.setVisibility(View.VISIBLE);
        View scrim = findViewById(R.id.scrim);
        scrim.setVisibility(View.VISIBLE);
    }
    public void continueGame(View view) {
        isPaused = false;
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
