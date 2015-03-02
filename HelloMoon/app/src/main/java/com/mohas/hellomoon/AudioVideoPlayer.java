package com.mohas.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Moha on 2/25/15.
 */
public class AudioVideoPlayer {

    private MediaPlayer mPlayer;
    private OnPlaying mOnPlaying;
    private OnStopped mOnStopped;
    private int mState = 0;

    private SurfaceHolder mVideoHolder;
    private int mResource;
    private Context mContext;
    //private boolean surfaceReady;

    //States
    private final int STATE_STOPPED = 0;
    private final int STATE_PLAYING = 1;
    private final int STATE_PAUSED = 2;

    public AudioVideoPlayer(Context c, SurfaceHolder video, int resource){
        mContext = c;
        mVideoHolder = video;
        mResource = resource;
    }

    public AudioVideoPlayer(Context c, int resource){
        mContext = c;
        mResource = resource;
        //surfaceReady = true;
    }

    public AudioVideoPlayer(){}

    public void stop(){
        if(mPlayer != null){
            stopMediaPlayer();
        }
    }

    public void play(){
        switch(mState){
            case STATE_STOPPED: //start playing
                stop();
                initMediaPlayer();
                startMediaPlayer();
                break;

            case STATE_PLAYING: // Pause
                if(mPlayer.isPlaying()){
                    pauseMediaPlayer();
                }
                break;
            case STATE_PAUSED:  // Resume play
                startMediaPlayer();
                break;

        }
    }

    public void initMediaPlayer(){
        mPlayer = MediaPlayer.create(mContext, mResource);
        if(mVideoHolder != null) {
            mPlayer.setDisplay(mVideoHolder);
            //mPlayer.seekTo(500);
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                AudioVideoPlayer.this.stop();
            }
        });
        mState = STATE_STOPPED;
    }

    public void startMediaPlayer(){
        //if(surfaceReady) {
            Log.d("AUDIO", "Start");
            mState = STATE_PLAYING;
            mOnPlaying.onPlaying();
            mPlayer.start();
        //}
    }

    public void pauseMediaPlayer(){
        Log.d("AUDIO", "Pause" );
        mState = STATE_PAUSED;
        mOnStopped.onStopped();
        mPlayer.pause();

    }

    public void stopMediaPlayer(){
        Log.d("AUDIO", "Stop" );
        mState = STATE_STOPPED;
        mOnStopped.onStopped();
        mPlayer.release();
        mPlayer = null;
    }

    public void setOnPlaying(OnPlaying op){
        mOnPlaying = op;
    }

    public void setOnStopped(OnStopped os){
        mOnStopped = os;
    }

    /*public boolean isSurfaceReady() {
        return surfaceReady;
    }

    public void setSurfaceReady(boolean surfaceReady) {
        this.surfaceReady = surfaceReady;
    }*/

    public interface OnStopped{
        public void onStopped();
    }

    public interface OnPlaying{
        public void onPlaying();
    }

}
