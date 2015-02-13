package com.mohas.criminalintent;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moha on 2/12/15.
 */
public class TimeDatePickerFragment extends DialogFragment {

    //Arguments to initialize this Fragment.
    public static final String EXTRA_DATE = "com.mohas.criminalintent.datetime";

    //attributes in this fragments
    private Date mDate;

    public static TimeDatePickerFragment newInstance(Date date){
        TimeDatePickerFragment fragment = new TimeDatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_date_and_time, container, false);

        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_datePicker);
        datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_timePicker);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));


        return v;
    }

}
