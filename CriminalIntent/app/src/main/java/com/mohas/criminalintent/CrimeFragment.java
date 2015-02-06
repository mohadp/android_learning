package com.mohas.criminalintent;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimeFragment} interface
 * to handle interaction events.
 * Use the {@link CrimeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;

    //References to the widgets in the layout
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
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

        //Other initialization of widgets

        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()));
        mDateButton.setEnabled(false);

        return v;
    }
}
