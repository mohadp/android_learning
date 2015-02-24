package com.mohas.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Moha on 2/8/15.
 */
public class CrimePagerActivity extends ActionBarActivity {

    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = this.getFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){

            @Override
            public int getCount(){
                return CrimePagerActivity.this.mCrimes.size();
            }

            @Override
            public Fragment getItem(int pos){
                Crime crime = CrimePagerActivity.this.mCrimes.get(pos);
                return CrimeFragment.newInstance(crime.getId());
            }
        });

        //Read intent extras, in this case the crimeId, to set ViewPager's current item to be the crime
        UUID crimeId = (UUID)this.getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        // Set listener to the ViewPager
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state){ }
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){ }
            public void onPageSelected(int pos){
                Crime c = mCrimes.get(pos);
                if(c.getTitle() != null){
                    CrimePagerActivity.this.setTitle(c.getTitle());
                }
            }
        });
    }
}
