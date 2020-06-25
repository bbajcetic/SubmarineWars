package com.sfu.cmpt276assignment3.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.sfu.cmpt276assignment3.R;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
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
    private int[] submarinesInRow;
    private int[] submarinesInCol;

    private int numSubmarines;

    private int numRows;
    private int numCols;

    private int missiles_wasted;
    private int submarines_destroyed;

    private boolean isPaused ;

    public int getMissiles_wasted() { return missiles_wasted; }
    public int getSubmarines_destroyed() { return submarines_destroyed; }
    public int getNumSubmarines() { return numSubmarines; }
    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }


    private Random random;

    public GameModel(int numRows, int numCols, int numSubmarines) {
        random = new Random();

        isPaused = false;

        missiles_wasted = 0;
        submarines_destroyed = 0;

        this.numSubmarines = numSubmarines;
        this.numRows = numRows;
        this.numCols = numCols;

        isClicked = new boolean[numRows][numCols];
        isDoubleClicked = new boolean[numRows][numCols];
        isSubmarine = new boolean[numRows][numCols];
        submarinesInRow = new int[numRows];
        submarinesInCol = new int[numCols];
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
                submarinesInRow[row]++;
                submarinesInCol[col]++;
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

    public boolean isDestroyedSubmarine(int row, int col) {
        return (isClicked[row][col] && isSubmarine[row][col]);
    }
    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public String getGame() throws IOException {
        Gson gson = new Gson();
        String[] variables = new String[10];
        variables[0] = Integer.toString(numSubmarines);
        variables[1] = Integer.toString(numRows);
        variables[2] = Integer.toString(numCols);
        variables[3] = Integer.toString(missiles_wasted);
        variables[4] = Integer.toString(submarines_destroyed);

        variables[5] = gson.toJson(isClicked);
        variables[6] = gson.toJson(isDoubleClicked);
        variables[7] = gson.toJson(isSubmarine);
        variables[8] = gson.toJson(submarinesInRow);
        variables[9] = gson.toJson(submarinesInCol);

        String gameString = serialize(variables);
        //Log.d("TAG", "get: Submarines destroyed: " + submarines_destroyed);
        return gameString;
    }

    // These 2 functions are from: https://stackoverflow.com/questions/13271503/converting-array-string-to-string-and-back-in-java
    private String serialize(String[] strings) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(strings);
        String serializedString = new String(Hex.encodeHex(out.toByteArray()));
        return serializedString;
    }
    private String[] deserialize(String serializedString) throws DecoderException, IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(Hex.decodeHex(serializedString.toCharArray()));
        String[] deserializedString = (String[]) new ObjectInputStream(in).readObject();
        return deserializedString;
    }

    public void loadGame(String game) throws ClassNotFoundException, IOException, DecoderException {
        Gson gson = new Gson();
        String gameState[] = deserialize(game);
        numSubmarines = Integer.parseInt(gameState[0]);
        numRows = Integer.parseInt(gameState[1]);
        numCols = Integer.parseInt(gameState[2]);
        missiles_wasted = Integer.parseInt(gameState[3]);
        submarines_destroyed = Integer.parseInt(gameState[4]);

        isClicked = gson.fromJson(gameState[5], boolean[][].class);
        isDoubleClicked = gson.fromJson(gameState[6], boolean[][].class);
        isSubmarine = gson.fromJson(gameState[7], boolean[][].class);
        submarinesInRow = gson.fromJson(gameState[8], int[].class);
        submarinesInCol = gson.fromJson(gameState[9], int[].class);
        //Log.d("TAG", "load: Submarines destroyed: " + submarines_destroyed);
    }
}
