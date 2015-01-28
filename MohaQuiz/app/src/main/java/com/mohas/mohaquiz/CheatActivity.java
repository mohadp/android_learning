package com.mohas.mohaquiz;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.mohas.mohaquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.mohas.mohaquiz.answer_shown";
    public static final String EXTRA_HAS_CHEATED = "com.mohas.mohaquiz.has_cheated";
    public static final String IS_CHEATER = "cheater";

    private boolean mIsCheater;
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private TextView mBuildNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
        //Get calling Activity information

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mIsCheater = getIntent().getBooleanExtra(EXTRA_HAS_CHEATED, false);

        if(savedInstanceState != null){
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER);
        }

        //Get references to widgets
        mAnswerTextView = (TextView) this.findViewById(R.id.answerTextView);
        mShowAnswer = (Button) this.findViewById(R.id.showAnswerButton);
        mBuildNumber = (TextView)this.findViewById(R.id.build_number);

        //Restore app according to state
        setAnswerTextAndResult();
        mBuildNumber.setText("Current Version: " + Build.VERSION.RELEASE);

        //Set listeners
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mIsCheater = true;
                setAnswerTextAndResult();
            }
        });
	}

    public void setAnswerTextAndResult(){
        if(mIsCheater) {
            mAnswerTextView.setText(mAnswerIsTrue ? R.string.true_button : R.string.false_button);
        }
        setActivityResult();

    }

    public void setActivityResult(){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsCheater);
        this.setResult(this.RESULT_OK,data);  //sets an intent with information for the parent/calling Activity to know
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(IS_CHEATER, this.mIsCheater);
    }

}
