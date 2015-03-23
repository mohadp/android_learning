package com.mohas.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

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
    public final static int STATE_STOPPED = 0;
    public final static int STATE_PLAYING = 1;
    public final static int STATE_PAUSED = 2;

    public AudioVideoPlayer(Context c, SurfaceHolder video, int resource){
        this(c, resource);
        mVideoHolder = video;
    }

    public AudioVideoPlayer(Context c, int resource){
        mContext = c;
        mResource = resource;
        //surfaceReady = true;
        initMediaPlayer();
    }

    public void stop(){
        if(mPlayer != null){
            stopMediaPlayer();
        }
    }

    public void play(){
        switch(mState){
            case STATE_STOPPED: //start playing
                //initMediaPlayer();
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
        stop();
        //mPlayer = new MediaPlayer();
        Uri resourceId = Uri.parse("android.resource://" +
                mContext.getResources().getResourcePackageName(mResource) + "/" +
                mContext.getResources().getResourceTypeName(mResource) + "/" +
                mContext.getResources().getResourceEntryName(mResource));

        Log.d("AUDIO", "URI = " + resourceId.toString());

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mContext, resourceId);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    AudioVideoPlayer.this.stop();
                }
            });
        }catch(IOException e){
            Log.d("AUDIO", e.toString());
        }

        //mPlayer = MediaPlayer.create(mContext, mResource);
        /*if(getVideoHolder() != null) {
            mPlayer.setDisplay(getVideoHolder());
            //mPlayer.seekTo(500);
        }*/
        mState = STATE_STOPPED;
    }

    public void startMediaPlayer(){
        //if(surfaceReady) {
        if(mPlayer == null){
            initMediaPlayer();
            mPlayer.setDisplay(mVideoHolder);
        }
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
        mPlayer.setDisplay(null);
        mPlayer.release();
        mPlayer = null;
    }

    public void setOnPlaying(OnPlaying op){
        mOnPlaying = op;
    }

    public void setOnStopped(OnStopped os){
        mOnStopped = os;
    }

    public SurfaceHolder getVideoHolder() {
        return mVideoHolder;
    }

    public void setVideoHolder(SurfaceHolder videoHolder) {
        this.mVideoHolder = videoHolder;
        if(mPlayer != null) { mPlayer.setDisplay(mVideoHolder); }
    }

    public int getState() {
        return mState;
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
