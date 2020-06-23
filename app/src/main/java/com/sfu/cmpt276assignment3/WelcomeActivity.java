package com.sfu.cmpt276assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private Button buttonSkip;
    private ImageView imageSubmarine;
    private ImageView imageBomb;
    private ImageView imageTitle;
    private TextView textDeveloper;

    AnimatorSet move_set;
    AnimatorSet slide_set;
    AnimatorSet title_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "onCreate: WelcomeActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupViews();
        setupAnimations();
    }

    private void setupViews() {
        buttonSkip = findViewById(R.id.button_skip);
        imageSubmarine = findViewById(R.id.image_submarine);
        imageBomb = findViewById(R.id.image_bomb);
        imageTitle = findViewById(R.id.image_title);
        textDeveloper = findViewById(R.id.text_developer);
    }

    private void setupAnimations() {
        textDeveloper.setAlpha(0.0f);
        imageSubmarine.setAlpha(0.0f);
        imageBomb.setAlpha(0.0f);
        buttonSkip.setAlpha(0.0f);

        animateTitle();

        // bomb, developer text, skip button
        slide_set = new AnimatorSet();
        //slide_set.setDuration(900);
        slide_set.setStartDelay(200);
        ObjectAnimator bomb_slide_in = ObjectAnimator.ofFloat(imageBomb, View.TRANSLATION_X, -2000, 0).setDuration(700);
        ObjectAnimator bomb_fade_in = ObjectAnimator.ofFloat(imageBomb, View.ALPHA, 0.8f);
        ObjectAnimator developer_slide_in = ObjectAnimator.ofFloat(textDeveloper, View.TRANSLATION_X, -2000, 0).setDuration(1000);
        ObjectAnimator developer_fade_in = ObjectAnimator.ofFloat(textDeveloper, View.ALPHA, 1.0f);
        ObjectAnimator skip_slide_in = ObjectAnimator.ofFloat(buttonSkip, View.TRANSLATION_X, 2000, 0).setDuration(1000);
        ObjectAnimator skip_fade_in = ObjectAnimator.ofFloat(buttonSkip, View.ALPHA, 1.0f);
        // delays
        developer_fade_in.setStartDelay(200);
        developer_slide_in.setStartDelay(200);
        skip_fade_in.setStartDelay(400);
        skip_slide_in.setStartDelay(400);
        // play animation
        slide_set.play(bomb_slide_in).with(bomb_fade_in);
        slide_set.play(developer_slide_in).with(developer_fade_in);
        slide_set.play(skip_slide_in).with(skip_fade_in);
        slide_set.start();

        move_set = new AnimatorSet();
        move_set.setStartDelay(1500);
        //imageSubmarine.setAlpha(1.0f);
        ObjectAnimator submarine_fade_in = ObjectAnimator.ofFloat(imageSubmarine, View.ALPHA, 1.0f);
        ObjectAnimator submarine_move1 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_X, -1500, 1500);
        ObjectAnimator submarine_move2 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_X, 1500, -1500);
        ObjectAnimator submarine_move3 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_Y, 1000);
        ObjectAnimator submarine_move4 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_X, 0);
        ObjectAnimator submarine_rotate1 = ObjectAnimator.ofFloat(imageSubmarine, View.ROTATION, 90);
        ObjectAnimator submarine_move5 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_Y, 750, -750);
        ObjectAnimator submarine_move6 = ObjectAnimator.ofFloat(imageSubmarine, View.TRANSLATION_Y, -750, 750);
        submarine_move1.setDuration(2500);
        submarine_move2.setDuration(2500);
        submarine_move5.setDuration(1500);
        submarine_move6.setDuration(1500);
        submarine_fade_in.setDuration(0);
        submarine_move4.setDuration(0);
        submarine_move3.setDuration(0);
        submarine_rotate1.setDuration(0);
        //move_set.play(submarine_rotate1).before(submarine_move1);
        move_set.play(submarine_move1).with(submarine_fade_in);
        move_set.play(submarine_move2).after(submarine_move1);
        move_set.play(submarine_move3).after(submarine_move2);
        move_set.play(submarine_move4).with(submarine_rotate1).after(submarine_move3);
        //move_set.play(submarine_rotate1).after(submarine_move4);
        move_set.play(submarine_move5).after(submarine_move4);
        move_set.play(submarine_move6).after(submarine_move5);
        // 4 second delay before going to the Main Menu
        // (chose alpha animation because it's easy to create a delay this way, without needing
        //  another thread)
        move_set.play(ObjectAnimator.ofFloat(imageSubmarine, View.ALPHA, 0.0f).setDuration(4000)).after(submarine_move6);
        move_set.start();

        move_set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                openMainMenu();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }
            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }

    private void animateTitle() {
        ObjectAnimator.ofFloat(imageTitle, View.TRANSLATION_X, -2000, 0)
                .setDuration(900).start();

        title_set = new AnimatorSet();
        title_set.setStartDelay(900);
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
                title_set.setStartDelay(0);
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

    public void openMainMenu() {
        Intent mainMenuIntent = MainMenuActivity.makeIntent(this);
        startActivity(mainMenuIntent);
        finish();
    }
    public void skipAnimation(View view) {
        openMainMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title_set.cancel();
        slide_set.cancel();
        move_set.cancel();
    }

    @Override
    protected void onDestroy() {
        Log.d("TAG", "onDestroy: WelcomeActivity");
        super.onDestroy();
    }
}
