package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    Button[][] buttons;
    int[][] values;
    int numCols;
    int numRows;
    int numSubmarines;

    int missiles_fired = 0;
    int submarines_destroyed = 0;

    TextView missileInfo;
    TextView subInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        numSubmarines = OptionsActivity.getNumberOfSubmarines(this);
        numCols = OptionsActivity.getBoardSizeX(this);
        numRows = OptionsActivity.getBoardSizeY(this);
        buttons = new Button[numCols][numRows];
        values = new int[numCols][numRows];

        setupInfoBox();
        setupGrid();
        setupBackArrow();
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
        LinearLayout grid = findViewById(R.id.game_grid);
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
                button.setBackground(getResources().getDrawable(R.drawable.grid_element));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });
                buttons[col][row] = button;
                tableRow.addView(button);
            }
        }
    }

    private void gridButtonClicked(int col, int row) {
        missiles_fired++;
        submarines_destroyed++;
        Toast.makeText(this, String.format("Button at %d, %d clicked", col, row), Toast.LENGTH_SHORT).show();
        Button button = buttons[col][row];
        setWaterBackground(col, row);
        values[col][row]++;
        button.setText(Integer.toString(values[col][row]));
        button.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        button.setTextSize(48);
        button.setTypeface(ResourcesCompat.getFont(this, R.font.font_motion_control_bold), Typeface.BOLD);
        updateInfoBox();
    }

    private void setWaterBackground(int col, int row) {
        Button button = buttons[col][row];
        Log.d("TAG", "" + col + " " + row);

        // Lock buttons
        lockButtonSizes();
        int width = button.getWidth();
        int height = button.getHeight();

        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.grid_element_destroyed_submarine);
        layerDrawable.setBounds(0, 0, width, height);
        // already to scale because you create a bitmap with this size
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        layerDrawable.draw(new Canvas(bitmap));
        button.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void lockButtonSizes() {
        int x = OptionsActivity.getBoardSizeX(this);
        int y = OptionsActivity.getBoardSizeY(this);

        for (int row = 0; row < y; row++) {
            for (int col = 0; col < x; col++) {
                Button button = buttons[col][row];
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

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    public void clickBackArrow(View view) {
        finish();
    }
}
