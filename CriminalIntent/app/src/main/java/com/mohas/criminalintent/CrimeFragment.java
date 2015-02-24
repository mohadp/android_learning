package com.mohas.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;

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

    //Intent extras used by calling fragments/activites
    public static final String EXTRA_CRIME_ID = "com.mohas.criminalintent.crime_id";

    //Fragments managed by the FragmentManager
    public static final String DIALOG_DATE = "date";

    //Request codes to other fragments
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;

    //References to the widgets in the layout
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;


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

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        if(crimeId != null){
            mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
        }else{
            mCrime = new Crime();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        //Add reference to widgets
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);

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
        }

    }

    private void updateDateButtonText(){
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy hh:mm:ss", mCrime.getDate()));
    }

}
