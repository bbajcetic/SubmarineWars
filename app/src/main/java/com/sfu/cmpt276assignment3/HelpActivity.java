package com.sfu.cmpt276assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.sfu.cmpt276assignment3.SoundManager.MENU_MUSIC;

public class HelpActivity extends AppCompatActivity {

    SoundManager soundManager;
    boolean keepPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        soundManager = SoundManager.getInstance(getApplicationContext());
        soundManager.startMusic(MENU_MUSIC);

        setupWebpageLink();
        setupBackArrow();
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

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.pauseMusic(keepPlaying);
        keepPlaying = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resumeMusic(MENU_MUSIC);
    }
}
