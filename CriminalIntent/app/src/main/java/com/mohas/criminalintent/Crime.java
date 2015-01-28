package com.mohas.criminalintent;

import java.util.UUID;

/**
 * Created by Moha on 1/27/15.
 */
public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime(){
        mId = UUID.randomUUID();
    }


    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }
}
