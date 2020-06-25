package com.sfu.cmpt276assignment3.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.sfu.cmpt276assignment3.R;

/**
 * This class is used as an interface to the permanent data for the other classes to use:
 * - Options configurations (board size, number of submarines)
 * - Saved game state
 */
public class GameData {
    /*private List<Lens> lenses = new ArrayList<>();
    private void saveLenses() {
        Gson gson = new Gson();
        String json_lenses = gson.toJson(lenses);
        Log.d("DEBUG", "Lenses = " + json_lenses);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("SAVED_LENSES", json_lenses);
        preferencesEditor.apply();
    }

    private void loadLenses(String json_lenses) {
        Gson gson = new Gson();
        Type lensesListType = new TypeToken<ArrayList<Lens>>(){}.getType();
        lenses = gson.fromJson(json_lenses, lensesListType);
    }*/

    public static final String SETTINGS_FILE = "settings_file";
    private int numCols;
    private int numRows;
    private int numSubmarines;
    private int numGamesPlayed;
    private String numRowsKey = "numRows";
    private String numColsKey = "numCols";
    private String numSubmarinesKey = "numSubmarines";
    private String numGamesPlayedKey = "numGamesPlayed";
    private String highScoreKeyFormat = "high_score_%d_%d_%d";
    private SharedPreferences prefs;

    // Singleton pattern
    private static GameData instance;
    public static GameData getInstance(Context context) {
        if (instance == null) {
            instance = new GameData(context);
        }
        return instance;
    }

    private GameData(Context context) {
        prefs = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        int defaultNumSubmarines = context.getResources().getInteger(R.integer.default_num_submarines);
        int defaultNumCols = context.getResources().getInteger(R.integer.default_num_cols);
        int defaultNumRows = context.getResources().getInteger(R.integer.default_num_rows);
        numCols = prefs.getInt(numColsKey, defaultNumCols);
        numRows = prefs.getInt(numRowsKey, defaultNumRows);
        numSubmarines = prefs.getInt(numSubmarinesKey, defaultNumSubmarines);
        numGamesPlayed = prefs.getInt(numGamesPlayedKey, 0);
    }

    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }
    public int getNumSubmarines() { return numSubmarines; }
    public int getNumGamesPlayed() { return numGamesPlayed; }

    public void setNumCols(int numCols) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(numColsKey, numCols);
        editor.apply();
        this.numCols = numCols;
    }
    public void setNumRows(int numRows) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(numRowsKey, numRows);
        editor.apply();
        this.numRows = numRows;
    }
    public void setNumSubmarines(int numSubmarines) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(numSubmarinesKey, numSubmarines);
        editor.apply();
        this.numSubmarines = numSubmarines;
    }
    public void setNumGamesPlayed(int numGamesPlayed) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(numGamesPlayedKey, numGamesPlayed);
        editor.apply();
        this.numGamesPlayed = numGamesPlayed;
    }
    public void setBoardSize(int numRows, int numCols) {
        setNumRows(numRows);
        setNumCols(numCols);
    }
    public void setHighScore(int score) {
        SharedPreferences.Editor editor = prefs.edit();
        String keyString = String.format(highScoreKeyFormat, numSubmarines, numRows, numCols);
        editor.putInt(keyString, score);
        editor.apply();
    }
    // returns -1 if no high score has been set
    public int getHighScore() {
        String keyString = String.format(highScoreKeyFormat, numSubmarines, numRows, numCols);
        return prefs.getInt(keyString, -1);
    }

    public void resetData(Context context) {
        int[] numSubmarinesOptions = context.getResources().getIntArray(R.array.num_submarines_options);
        int[] numRowsOptions = context.getResources().getIntArray(R.array.num_rows_options);
        int[] numColsOptions = context.getResources().getIntArray(R.array.num_cols_options);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < numSubmarinesOptions.length; i++) {
            for (int j = 0; j < numRowsOptions.length; j++) {
                String keyString = String.format(highScoreKeyFormat, numSubmarinesOptions[i], numRowsOptions[j], numColsOptions[j]);
                editor.remove(keyString);
            }
        }
        editor.remove(numGamesPlayedKey);
        editor.apply();
    }
}
