package com.mohas.criminalintent;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moha on 2/4/15.
 */
public class CrimeListFragment extends ListFragment {

    private static final String TAG = "CrimeListFragment";
    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        mSubtitleVisible = false;

        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //Configure the adapter that will control the ListView in the fragment
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        this.setListAdapter(adapter);

    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            //Use floating text menus on Froyo and Gingerbread
            this.registerForContextMenu(listView);
        }else{
            //Use contextual action bar on Honeycomb and higher. *_MODAL means an actionbar appears...
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }

        if(mSubtitleVisible){
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }


        //Create the callbacks to the ActionBar contextual bar
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
            public void onItemCheckedStateChanged(ActionMode mode, int postion, long id, boolean checked){}

            //ActionMode.Callback methods
            public boolean onCreateActionMode(ActionMode mode, Menu menu){
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu){ return false; }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item){
                switch(item.getItemId()){
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                        CrimeLab crimeLab = CrimeLab.get(getActivity());

                        for(int i = adapter.getCount()-1; i >= 0; i--){
                            if(getListView().isItemChecked(i)){
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }

            public void onDestroyActionMode(ActionMode mode){ }

        });


        return v;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && menuItem != null){
            menuItem.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);

                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if(((ActionBarActivity)getActivity()).getSupportActionBar().getSubtitle() == null) {
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mSubtitleVisible = true;
                }else{
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mSubtitleVisible = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo){
        //Only create contextual menu for listview, on which the context menu is registered
        if(v.getId() == android.R.id.list) {
            getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);

        switch(item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
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
