package com.mohas.hellomoon;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Moha on 2/24/15.
 */
public abstract class HelloMoonFragmentBase extends Fragment {

    private Button mPlayButton;
    private Button mStopButton;
    protected AudioVideoPlayer mPlayer;


    //Abstract methods
    protected abstract View inflatePlayStopView(LayoutInflater inflater, ViewGroup parent); //Create a vew based on a particular layout
    protected abstract void initializeAudioVideoPlayer(View v); //creates instance of mPlayer


    //Normal Methods
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflatePlayStopView(inflater, parent);

        getViewsAndSetListeners(v);
        initializeAudioVideoPlayer(v);
        setAudioPlayerListeners();

        return v;
    }

    protected void getViewsAndSetListeners(View v){
        mPlayButton = (Button)v.findViewById(R.id.hellomoon_playButton);
        mStopButton = (Button)v.findViewById(R.id.hellomoon_stopButton);

        mPlayButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                HelloMoonFragmentBase.this.mPlayer.play();
            }
        });

        mStopButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                HelloMoonFragmentBase.this.mPlayer.stop();
            }
        });

        if(mPlayer != null){
            if(mPlayer.getState() == AudioVideoPlayer.STATE_PLAYING){ setPauseButton(); }
        }
    }


    protected void setAudioPlayerListeners(){
        mPlayer.setOnPlaying(new AudioVideoPlayer.OnPlaying(){
            @Override
            public void onPlaying(){
                setPauseButton();
            }
        });

        mPlayer.setOnStopped(new AudioVideoPlayer.OnStopped(){
            @Override
            public void onStopped(){
                setPlayButton();
            }
        });
    }




    @Override
    public void onDestroy(){
        super.onDestroy();
        mPlayer.stop();
    }

    public void setPauseButton(){
        mPlayButton.setText(R.string.hellomoon_pause);
    }

    public void setPlayButton(){
        mPlayButton.setText(R.string.hellomoon_play);
    }

}
