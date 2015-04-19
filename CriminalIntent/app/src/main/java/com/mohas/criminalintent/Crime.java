package com.mohas.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Photo mPhoto;

    //JSON properties
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";

    public Crime(){

        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(String title, boolean isSolved){
        this();
        mTitle = title;
        mSolved = isSolved;
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_PHOTO)){
               mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        if(mPhoto != null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }
        return json;
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

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }
}
