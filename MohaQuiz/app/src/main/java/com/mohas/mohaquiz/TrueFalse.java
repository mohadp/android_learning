package com.mohas.mohaquiz;


public class TrueFalse {

	private int mQuestion; //question holds a resource ID of a text/string
	private boolean mTrueQuestion;
	
	public TrueFalse(int question, boolean trueQuestion){
		mQuestion = question;
		mTrueQuestion = trueQuestion;
	}

	public int getQuestion() {
		return mQuestion;
	}

	public void setQuestion(int mQuestion) {
		this.mQuestion = mQuestion;
	}

	public boolean isTrueQuestion() {
		return mTrueQuestion;
	}

	public void setTrueQuestion(boolean mTrueQuestion) {
		this.mTrueQuestion = mTrueQuestion;
	}
	
}
