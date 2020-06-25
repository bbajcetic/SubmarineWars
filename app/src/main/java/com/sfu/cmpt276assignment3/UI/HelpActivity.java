package com.sfu.cmpt276assignment3.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sfu.cmpt276assignment3.Model.MusicManager;
import com.sfu.cmpt276assignment3.R;

import static com.sfu.cmpt276assignment3.Model.MusicManager.MENU_MUSIC;

/**
 * This class is the Help Activity UI: responsible for displaying information to the player
 * about how to play the game, the developer of the game, and citations to free resources used in
 * the game
 */
public class HelpActivity extends AppCompatActivity {

    MusicManager musicManager;
    boolean keepPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        musicManager = MusicManager.getInstance(getApplicationContext());
        musicManager.startMusic(MENU_MUSIC);

        setupWebpageLink();
        setupBackArrow();
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

    private void setupBackArrow() {
        ImageButton backArrow = findViewById(R.id.back_arrow);
        backArrow.setImageResource(R.drawable.arrow_animation);
        AnimationDrawable moveAnimation = (AnimationDrawable)backArrow.getDrawable();
        moveAnimation.start();
    }

    private void setupWebpageLink() {
        TextView aboutContent = findViewById(R.id.text_help_about_content);
        String text = this.getResources().getString(R.string.help_about_content);
        int startIndex = text.indexOf("Webpage: ") + "Webpage: ".length();
        int endIndex = text.length();
        final String webpage = text.substring(startIndex, endIndex);

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpage));
                startActivity(browserIntent);
            }
        };
        ss.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        aboutContent.setText(ss);
        aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        return intent;
    }

    public void clickBackArrow(View view) {
        keepPlaying = true;
        finish();
    }
}
