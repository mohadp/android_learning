package com.mohas.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Moha on 3/29/15.
 */

public class CrimeCameraFragment extends Fragment {

    //Debugging tag
    private static final String TAG = "CrimeCameraFragment";

    //Constants for inter-activity
    public static final String EXTRA_PHOTO_FILENAME = "com.mohas.criminalintent.photo_filename";

    //Private attributes
    @SuppressWarnings("deprecation")
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(mCamera != null){
                    //Take picture using the callbacks defined below; ShutterCallback adds a progress bar. PictureTake saves picture in app directory
                    Log.d(TAG, "Taking picture from Button; mCamera is not null");
                    mCamera.takePicture(new ShutterCallbackTakePic(), null, new PictureCallbackTakePic());
                }else {
                    Log.d(TAG, "mCamera is null");
                }
            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceCallBackForCamera());

        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        return v;
    }



    @SuppressWarnings("deprecation")
    @Override
    public void onResume(){
        super.onResume();
        mCamera = Camera.open(0);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    @SuppressWarnings("deprecation")
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        //See CameraPreview.java in the ApiDemos for sample app from Android.
        //Log.d(TAG, "Surface Width = " + width + ", Height = " + height);
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.height * bestSize.width;

        for(Camera.Size s : sizes){
            int area = s.height * s.width;
            //Log.d(TAG, "Supported Sizes: Width = " + s.width + ", Height = " + s.height);
            //if(s.width <= width && s.height <= height){
                if(largestArea < area) {
                    bestSize = s;
                    largestArea = area;
                }
            //}
        }

        return bestSize;
    }

    private class SurfaceCallBackForCamera implements SurfaceHolder.Callback{

        //when surface gets its client, the camera
        public void surfaceCreated(SurfaceHolder holder){
            try {
                if(mCamera != null) mCamera.setPreviewDisplay(holder);
            }catch(IOException e){
                Log.e(TAG, "Could not set camera as Surface's client", e);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
            if(mCamera == null) return;

            //The surface has changed size; update the camera preview size
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height); //To be reset in the next section
            parameters.setPreviewSize(s.width, s.height);
            s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
            parameters.setPictureSize(s.width, s.height);
            mCamera.setParameters(parameters);

            try {
                mCamera.startPreview();
            }catch(Exception e){
                Log.e(TAG,"Could not start preview", e);
                mCamera.release();
                mCamera = null;
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder){
            //Cannot display camera preview in surface because it is detroyed
            if(mCamera != null) {
                mCamera.stopPreview();
            }
        }
    }

    //Taking picture...
    @SuppressWarnings("deprecation")
    private class ShutterCallbackTakePic implements Camera.ShutterCallback{

        public void onShutter(){
            //Display progress bar
            mProgressContainer.setVisibility(View.VISIBLE);
        }

    }

    //Processing picture...
    @SuppressWarnings("deprecation")
    private class PictureCallbackTakePic implements Camera.PictureCallback{

        public void onPictureTaken(byte[] data, Camera camera){
            //create a file
            String filename = UUID.randomUUID().toString() + ".jpg";
            Log.d(TAG, "OnPictureTaken: " + filename);
            FileOutputStream os = null;

            boolean success = false;

            try {
                if (isExternalStorageAvailable()) { //First try to save to external storage:
                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
                    os = new FileOutputStream(file);
                } else { // save in internal app storage
                    os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                }
                os.write(data);
                Log.i(TAG, "JPEG saved: "+ filename);
                success = true;
            }catch(Exception e){
                Log.e(TAG, "Failed to save picture " + filename, e);
                success = false;
            }finally{
                try{
                    if(os != null){
                        os.close();
                    }
                }catch(Exception e){
                    Log.e(TAG, "Error closing file " + filename, e);
                }
            }

            if(success){
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK, i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            CrimeCameraFragment.this.getActivity().finish();
        }

    }

    private boolean isExternalStorageAvailable(){
        //Log.d(TAG, "isExternalStorageAvailable: StorageState = " + Environment.getExternalStorageState());
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ return true; }

        return false;
    }

}
