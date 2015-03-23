package com.mohas.criminalintent;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Moha on 2/3/15.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private ArrayList<Crime> mCrimes;
    private Context mAppContext;

    //JSON-specific attributes
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext){
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME, true);
        //addRandomCrimes();
        try{
            mCrimes = mSerializer.loadCrimes();
        }catch(Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes:", e);
        }
    }

    public static CrimeLab get(Context c){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    /*private void addRandomCrimes(){
        for(int i = 0; i < 100; i++){
            Crime c = new Crime("Crime " + i, (i % 2 == 0));
            mCrimes.add(c);
        }
    }*/

    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            Toast.makeText(mAppContext, "Crimes saved :-)", Toast.LENGTH_SHORT);
            return true;
        }catch(Exception e){
            Log.e(TAG, "Error saving crimes: ", e);
            Toast.makeText(mAppContext, "Crime saving failed :-(", Toast.LENGTH_SHORT);
            return false;
        }
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }



    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime c : mCrimes){
            if(c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }
}
