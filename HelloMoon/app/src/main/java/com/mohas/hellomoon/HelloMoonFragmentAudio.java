package com.mohas.hellomoon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Moha on 2/24/15.
 */
public class HelloMoonFragmentAudio extends HelloMoonFragmentBase {

    @Override
    protected void initializeAudioVideoPlayer(View v){
        mPlayer = (mPlayer == null)? new AudioVideoPlayer(this.getActivity(), R.raw.one_small_step) : mPlayer;
    }

    @Override
    protected View inflatePlayStopView(LayoutInflater inflater, ViewGroup parent){
        return inflater.inflate(R.layout.fragment_hello_moon_audio,parent,false);
    }

}
