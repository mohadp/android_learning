package com.mohas.criminalintent;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moha on 2/4/15.
 */
public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //Configure the adapter that will control the ListView in the fragment
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        this.setListAdapter(adapter);

    }

    /**
     * Override a class to catch clicks on a particular list item
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Crime c = ((CrimeAdapter)l.getAdapter()).getItem(position);
        Log.d(this.getClass().getName(), c.getTitle());
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        this.startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    /**
     * Adapter for current this ListFragment's ListView so the adapter handles Crime objects.
     */
    private class CrimeAdapter extends ArrayAdapter<Crime>{

        public CrimeAdapter(ArrayList<Crime> crimes){

            super(getActivity(),0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = CrimeListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, parent, false);
            }

            //Fill in the view with the crime information
            Crime c = getItem(position);
            ((TextView)convertView.findViewById(R.id.crime_list_item_titleTextView)).setText(c.getTitle());
            ((TextView)convertView.findViewById(R.id.crime_list_item_dateTextView)).setText(DateFormat.format("EEEE, MMM d, yyyy", c.getDate()));
            ((Switch)convertView.findViewById(R.id.crime_list_item_solvedSwitch)).setChecked(c.isSolved());

            return convertView;
        }
    }

}
