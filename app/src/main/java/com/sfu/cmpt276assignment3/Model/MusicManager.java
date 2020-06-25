package com.sfu.cmpt276assignment3.Model;

import android.content.Context;
import android.media.MediaPlayer;

import com.sfu.cmpt276assignment3.R;

/**
 * This class is responsible for playing background game music for all the activities
 */
public class MusicManager {
    public static int MENU_MUSIC = R.raw.menu_music;
    public static int GAME_MUSIC = R.raw.game_music;

    MediaPlayer mediaPlayer;
    int currentTrack;
    Context context;
    boolean isPlaying;

    // Singleton Sound Manager
    private static MusicManager instance;
    public static MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    // regular class code
    private MusicManager(Context context) {
        this.context = context;
        isPlaying = false;
    }
    public void startMusic(int music_track) {
        if (isPlaying && currentTrack != music_track) {
            stopMusic();
            isPlaying = false;
        }
        if (!isPlaying) {
            mediaPlayer = MediaPlayer.create(context, music_track);
            currentTrack = music_track;
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
        isPlaying = true;
    }
    public void pauseMusic(boolean keepPlaying) {
        if (!keepPlaying) {
            //mediaPlayer.pause();
            stopMusic();
        }
    }
    public void resumeMusic(int music_track) {
        startMusic(music_track);
    }
    public void stopMusic() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
    }
}
