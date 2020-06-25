package com.sfu.cmpt276assignment3.Model;

import com.sfu.cmpt276assignment3.R;

import java.util.Random;

/**
 * This class is used for storing and updating the state of the Game. The Game Activity
 * interfaces with this class to update the UI
 */
public class GameModel {
    /* Data Structures for game computation */
    // isClicked[][] indicates whether that specific spot has been clicked
    // isDoubleClicked[][] indicates whether that specific spot has been clicked twice (for submarines, since the first click doesn't scan for nearby submarines)
    // isSubmarines[x][y] indicates whether that spot in the grid has a submarine
    // submarinesInCol[x] holds the number of submarines in that column
    // submarinesInRow[x] holds the number of submarines in that row
    private boolean[][] isClicked;
    private boolean[][] isDoubleClicked;
    private boolean[][] isSubmarine;
    private int[] submarinesInCol;
    private int[] submarinesInRow;

    private int numSubmarines;

    private int missiles_wasted;
    private int submarines_destroyed;

    private boolean isPaused ;

    public int getMissiles_wasted() { return missiles_wasted; }
    public int getSubmarines_destroyed() { return submarines_destroyed; }
    public int getNumSubmarines() { return numSubmarines; }

    private Random random;

    public GameModel(int numRows, int numCols, int numSubmarines) {
        random = new Random();

        isPaused = false;

        missiles_wasted = 0;
        submarines_destroyed = 0;

        this.numSubmarines = numSubmarines;

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

    public boolean isGameOver() {
        return submarines_destroyed == numSubmarines;
    }
    public boolean isPaused() { return isPaused; }

    public boolean fireMissile(int row, int col) {
        // if it's a destroyed submarine without a number
        if (isClicked[row][col] && !isDoubleClicked[row][col] && isSubmarine[row][col]) {
            isDoubleClicked[row][col] = true;
            missiles_wasted++;
        }
        // unclicked square
        if (!isClicked[row][col]) {
            isClicked[row][col] = true;
            if(isSubmarine[row][col]) {         // undiscovered submarine
                submarines_destroyed++;
                submarinesInRow[row]--;
                submarinesInCol[col]--;
                return true;
            }
            else {                              // no submarine
                missiles_wasted++;
            }
        }
        return false;
    }

    public int getHiddenCount(int row, int col) {
        return submarinesInRow[row] + submarinesInCol[col];
    }

    public boolean isNumberDisplayed(int row, int col) {
        if((isClicked[row][col] && !isSubmarine[row][col])
                || isDoubleClicked[row][col]) {
            return true;
        }
        return false;
    }
    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }
}
