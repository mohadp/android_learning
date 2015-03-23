package com.mohas.hellomoon;

import android.app.Fragment;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Moha on 3/1/15.
 */
public class HelloMoonFragmentVideo extends HelloMoonFragmentBase implements SurfaceHolder.Callback {

    //private SurfaceView mVideoSurface;


    @Override
    protected void getViewsAndSetListeners(View v){
        super.getViewsAndSetListeners(v);
        //mVideoSurface = (SurfaceView) v.findViewById(R.id.hellomoon_surface);
        //mVideoSurface.setVisibility(SurfaceView.VISIBLE);
        //mVideoSurface.setZOrderOnTop(true);

    }

    @Override
    protected void initializeAudioVideoPlayer(View v){
        //getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surface = (SurfaceView) v.findViewById(R.id.hellomoon_surface);
        surface.setZOrderOnTop(true);


        //sh.setFormat(PixelFormat.UNKNOWN);
        //sh.setSizeFromLayout();
        if(mPlayer == null) {
            mPlayer = new AudioVideoPlayer(getActivity(), surface.getHolder(), R.raw.apollo_17_stroll);
        }

        surface.getHolder().addCallback(this);
    }

    @Override
    protected View inflatePlayStopView(LayoutInflater inflater, ViewGroup parent){
        return inflater.inflate(R.layout.fragment_hello_moon_video, parent, false);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //mPlayer.setSurfaceReady(true);
        mPlayer.setVideoHolder(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPlayer.setVideoHolder(null);
    }
}
