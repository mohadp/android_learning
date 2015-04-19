package com.mohas.criminalintent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Moha on 3/30/15.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        //HIde window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //used action bar methods to hide the title bar; needs to be called after "super.onCreate(...)"
        getSupportActionBar().hide();
    }

    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }
}
