package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.sfu.cmpt276assignment3.MusicManager.MENU_MUSIC;

public class MainMenuActivity extends AppCompatActivity {
    MusicManager musicManager;
    boolean keepPlaying = false;

    Button buttonPlay;
    Button buttonOptions;
    Button buttonHelp;
    AnimatorSet title_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "onCreate: MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(MENU_MUSIC);

        displayOptions();
        setupButtons();
        animateTitle();
        animateButtons();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButtons() {
        buttonPlay = findViewById(R.id.button_play);
        buttonOptions = findViewById(R.id.button_options);
        buttonHelp = findViewById(R.id.button_help);
        ButtonTouchListener buttonTouchListener = new ButtonTouchListener();
        buttonPlay.setOnTouchListener(buttonTouchListener);
        buttonOptions.setOnTouchListener(buttonTouchListener);
        buttonHelp.setOnTouchListener(buttonTouchListener);
    }

    public class ButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundResource(R.drawable.menu_button_selected);
                    return false;
                case MotionEvent.ACTION_UP:
                    v.setBackgroundResource(R.drawable.menu_button);
                    return false;
            }
            return false;
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        return intent;
    }

    public void playGame(View view) {
        Intent gameIntent = GameActivity.makeIntent(this);
        startActivity(gameIntent);
        //keepPlaying = true;
    }
    public void openOptions(View view) {
        Intent optionsIntent = OptionsActivity.makeIntent(this);
        startActivity(optionsIntent);
        keepPlaying = true;
    }

    public void openHelp(View view) {
        Intent helpIntent = HelpActivity.makeIntent(this);
        startActivity(helpIntent);
        keepPlaying = true;
    }

    private void displayOptions() {
        GameData gameData = GameData.getInstance(this);
        int numCols = gameData.getNumCols();
        int numRows = gameData.getNumRows();
        int numSubmarines = gameData.getNumSubmarines();
        String toastString = String.format("Board size: %dx%d\nNumber of submarines: %d", numRows, numCols, numSubmarines);
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }

    private void animateButtons() {
        buttonPlay.setAlpha(0.0f);
        buttonOptions.setAlpha(0.0f);
        buttonHelp.setAlpha(0.0f);
        ObjectAnimator playAnimate = ObjectAnimator.ofFloat(buttonPlay, View.TRANSLATION_X, -2000, 0).setDuration(900);
        ObjectAnimator optionsAnimate = ObjectAnimator.ofFloat(buttonOptions, View.TRANSLATION_X, -2000, 0).setDuration(900);
        ObjectAnimator helpAnimate = ObjectAnimator.ofFloat(buttonHelp, View.TRANSLATION_X, -2000, 0).setDuration(900);
        ObjectAnimator playFadeIn = ObjectAnimator.ofFloat(buttonPlay, View.ALPHA, 1.0f);
        ObjectAnimator optionsFadeIn = ObjectAnimator.ofFloat(buttonOptions, View.ALPHA, 1.0f);
        ObjectAnimator helpFadeIn = ObjectAnimator.ofFloat(buttonHelp, View.ALPHA, 1.0f);
        optionsAnimate.setStartDelay(100);
        optionsFadeIn.setStartDelay(100);
        helpAnimate.setStartDelay(200);
        helpFadeIn.setStartDelay(200);
        playAnimate.start();
        playFadeIn.start();
        optionsAnimate.start();
        optionsFadeIn.start();
        helpAnimate.start();
        helpFadeIn.start();
    }
    private void animateTitle() {
        ImageView imageTitle = findViewById(R.id.image_title);
        title_set = new AnimatorSet();
        ObjectAnimator a1 = ObjectAnimator.ofFloat(imageTitle, View.SCALE_X, 1.0f, 0.9f).setDuration(1000);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(imageTitle, View.SCALE_Y, 1.0f, 0.9f).setDuration(1000);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(imageTitle, View.SCALE_X, 0.9f, 1.0f).setDuration(1000);
        ObjectAnimator a4 = ObjectAnimator.ofFloat(imageTitle, View.SCALE_Y, 0.9f, 1.0f).setDuration(1000);
        title_set.play(a1).with(a2);
        title_set.play(a3).with(a4).after(a2);
        title_set.start();
        title_set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                title_set.start();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }
            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        title_set.cancel();
        musicManager.pauseMusic(keepPlaying);
        keepPlaying = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        animateTitle();
        animateButtons();
        musicManager.resumeMusic(MENU_MUSIC);
    }
    @Override
    protected void onDestroy() {
        Log.d("TAG", "onDestroy: MainActivity");
        super.onDestroy();
    }
}
