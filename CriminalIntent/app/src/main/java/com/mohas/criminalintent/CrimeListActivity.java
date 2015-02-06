package com.mohas.criminalintent;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Moha on 2/4/15.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
