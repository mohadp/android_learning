package com.mohas.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Moha on 1/27/15.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime(){

        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(String title, boolean isSolved){
        this();
        mTitle = title;
        mSolved = isSolved;
    }


    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    @Override
    public String toString(){
        return mTitle;
    }
}
