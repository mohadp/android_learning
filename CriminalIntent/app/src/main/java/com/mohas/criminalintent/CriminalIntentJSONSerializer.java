package com.mohas.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Moha on 3/21/15.
 */
public class CriminalIntentJSONSerializer {
    private static final String TAG = "CriminalIntentJSONSerializer";

    private Context mContext;
    private String mFilename;
    private boolean mTrySD;

    public CriminalIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
        mTrySD = false;
    }

    public CriminalIntentJSONSerializer(Context c, String f, boolean sd){
        mContext = c;
        mFilename = f;
        mTrySD = sd;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();

        for(Crime c : crimes){
            array.put(c.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        OutputStream out = null;
        try{
            if(mTrySD && isExternalStorageAvailable()){
                File file = getFileHandle();
                out = new BufferedOutputStream(new FileOutputStream(file));
                Log.d(TAG, "saveCrimes() External Storage");
            }else {
                out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                Log.d(TAG, "saveCrimes() Only Private Storage");

            }
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());

        }finally{
            if(writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        InputStream in = null;

        try{
            if(mTrySD && isExternalStorageAvailable()) {
                File file = getFileHandle();
                in = new BufferedInputStream(new FileInputStream(file));
                Log.d(TAG, "LoadCrimes() External Storage");
            }else {
                //Open and read the file in StringBuilder
                in = mContext.openFileInput(mFilename);
                Log.d(TAG, "LoadCrimes() Only Private Storage");
            }

            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            //Line breaks are omitted and irrelevant
            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }

            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            //Build the array of crimes from JSONObjects
            for(int i=0; i < array.length(); i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch(FileNotFoundException e){
            //Ignore this one; it happens when starting fresh
        }finally{
            if(reader != null) {
                reader.close();
            }
        }

        return crimes;
    }

    private File getFileHandle(){
        return new File(mContext.getExternalFilesDir(null), mFilename);
    }

    private boolean isExternalStorageAvailable(){
        Log.d(TAG, "isExternalStorageAvailable: StorageState = " + Environment.getExternalStorageState());
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ return true; }

        return false;
    }
}
