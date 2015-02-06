package com.mohas.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class CriminalActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeFragment();
    }

}
