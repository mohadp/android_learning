package com.mohas.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Moha on 2/12/15.
 */
public class TimeDatePickerFragment extends DialogFragment {

    //Arguments to initialize this Fragment.
    public static final String EXTRA_DATE = "com.mohas.criminalintent.datetime";

    //Tabhost's tabs
    public static final String TAB_DATE = "date";
    public static final String TAB_TIME = "time";

    //attributes in this fragments
    private Date mDate;

    public static TimeDatePickerFragment newInstance(Date date){
        TimeDatePickerFragment fragment = new TimeDatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date_and_time, null);

        Resources res = getResources();
        TabHost mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec(TAB_DATE).setIndicator(res.getString(R.string.date_tab)).setContent(R.id.date_tab));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TIME).setIndicator(res.getString(R.string.time_tab)).setContent(R.id.time_tab));


        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.setTime(mDate);
                c.set(year, monthOfYear, dayOfMonth, c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
                mDate = c.getTime();

                //Update arguments to preserve selected value on rotation
                TimeDatePickerFragment.this.getArguments().putSerializable(EXTRA_DATE, mDate);
            }

        });

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute){
                Calendar c = Calendar.getInstance();
                c.setTime(mDate);
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                mDate = c.getTime();

                //Update arguments to preserve selected value on rotation
                TimeDatePickerFragment.this.getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(this.getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TimeDatePickerFragment.this.sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();


    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        this.getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, i);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_date_and_time, container, false);

        Resources res = getResources();
        TabHost mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec(TAB_DATE).setIndicator(res.getString(R.string.date_tab)).setContent(R.id.date_tab));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TIME).setIndicator(res.getString(R.string.time_tab)).setContent(R.id.time_tab));

        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

        return v;
    }*/

}
