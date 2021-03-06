package com.mohas.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimeFragment} interface
 * to handle interaction events.
 * Use the {@link CrimeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeFragment extends Fragment {

    //Debugging help
    public static final String TAG = "CrimeFragment";

    //Intent extras used by calling fragments/activites
    public static final String EXTRA_CRIME_ID = "com.mohas.criminalintent.crime_id";

    //Fragments managed by the FragmentManager
    public static final String DIALOG_DATE = "date";

    //Request codes to other fragments
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    private Crime mCrime;

    //References to the widgets in the layout
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;


    /**
     * No initialize the CrimeFragment
     * @param crimeId
     * @return
     */
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        if(crimeId != null){
            mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
        }else{
            mCrime = new Crime();
        }

    }

    @SuppressWarnings("depreciation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        if(NavUtils.getParentActivityName(getActivity()) != null) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Add reference to widgets
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);

        //Add listeners to the widgets
        mTitleField.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence c, int start, int before, int count){
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after){
                //Nothing now
            }

            public void afterTextChanged(Editable c){
                //Nothing now
            }
        });

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                //set the crime's solved property
                CrimeFragment.this.mCrime.setSolved(isChecked);
            }
        });

        mDateButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fm = CrimeFragment.this.getActivity().getFragmentManager();

                TimeDatePickerFragment dialog = TimeDatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mPhotoButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        //Verify if the device has a camera...
        boolean hasACamera = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
        if(!hasACamera){
            mPhotoButton.setEnabled(false);
        }

        //Other initialization of widgets
        mTitleField.setText(mCrime.getTitle());
        updateDateButtonText();
        //mDateButton.setEnabled(false);
        mSolvedCheckBox.setChecked(mCrime.isSolved());

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK) return;

        switch(requestCode){
            case REQUEST_DATE:
                Date date = (Date)data.getSerializableExtra(TimeDatePickerFragment.EXTRA_DATE);
                mCrime.setDate(date);
                updateDateButtonText();
                return;
            case REQUEST_PHOTO:
                String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
                if(filename != null){
                    mCrime.setPhoto(new Photo(filename));
                    showPhoto();
                    //Log.i(TAG, "Crime " + mCrime.getTitle() + " has a photo");
                }
                return;
        }
    }

    private void updateDateButtonText(){
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy hh:mm:ss", mCrime.getDate()));
    }

    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onStart(){
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(this.getActivity()) != null){
                    Log.d("CRIME", NavUtils.getParentActivityName(this.getActivity()));
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPhoto(){
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if(p != null){
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(this.getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }
}
