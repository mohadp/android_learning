package com.mohas.mohaquiz;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class QuizActivity extends Activity {
	
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mCheatButton;
	private TextView mQuestionText;
	private ImageButton mPrevButton;
	private ImageButton mNextButton;
	
	private TrueFalse[] mQuestionBank = new TrueFalse[]{
			new TrueFalse(R.string.question_africa, false),
			new TrueFalse(R.string.question_asia, true),
			new TrueFalse(R.string.question_europe, true),
			new TrueFalse(R.string.question_oceans, true)
	};


	private int mCurrentIndex = 0;
	private static final String KEY_INDEX = "index";
    private static final String IS_CHEATER = "isCheater";

    private boolean mIsCheater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_quiz);
		ActionBar actionBar = getActionBar();
        actionBar.setSubtitle("Mohas' Quiz");



		//get buttons
		mTrueButton = (Button)findViewById(R.id.true_button);
		mFalseButton = (Button)findViewById(R.id.false_button);
		mPrevButton = (ImageButton)findViewById(R.id.prev_button);
		mNextButton = (ImageButton)findViewById(R.id.next_button);
		mCheatButton = (Button)findViewById(R.id.cheat_button);
		mQuestionText = (TextView)findViewById(R.id.question_text_view);

		//read saved state
		if(savedInstanceState != null){
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER);
		}

		//Default state: display first question
		updateQuestion();
		
		
		//Add listeners
		mTrueButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast
				checkAnswer(true);
			}
		});

        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast
                checkAnswer(false);
            }
        });

		mNextButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mCurrentIndex = (++mCurrentIndex) % mQuestionBank.length;
				updateQuestion();
                clearCheating();
			}
		});
		
		mPrevButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mCurrentIndex = ((--mCurrentIndex) + mQuestionBank.length) % mQuestionBank.length;
				updateQuestion();
                clearCheating();
			}
		});
		
		mQuestionText.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mCurrentIndex = (++mCurrentIndex) % mQuestionBank.length;
				updateQuestion();
                clearCheating();
			}
		});
		
		

		mCheatButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent loadCheatIntent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                loadCheatIntent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                loadCheatIntent.putExtra(CheatActivity.EXTRA_HAS_CHEATED, mIsCheater);

                //Use ActivityForResult to get a result from child activity
                QuizActivity.this.startActivityForResult(loadCheatIntent,0);
			}
		});
		
		///////////// Chapter 3 - Lifecycle and Logging ////////////
		Log.d(this.getClass().getName(), "onCreate(bundle) called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onCreate(bundle) called", Toast.LENGTH_SHORT).show();

	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		Log.d(this.getClass().getName(), "onStart() called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onStart() called", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Log.d(this.getClass().getName(), "onPause() called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onPause() called", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d(this.getClass().getName(), "onResume() called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onResume() called", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Log.d(this.getClass().getName(), "onStop() called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onStop() called", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(this.getClass().getName(), "onDestroy() called");
		//Toast.makeText(QuizActivity.this, this.getClass().getName() + "onStop() called", Toast.LENGTH_SHORT).show();
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		Log.d(this.getClass().getName(), "onSavedInstanceState");
		savedInstanceState.putInt(KEY_INDEX, this.mCurrentIndex);
        savedInstanceState.putBoolean(IS_CHEATER, this.mIsCheater);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data!= null){
            mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,false);
        }
    }
	
	private void updateQuestion(){
		mQuestionText.setText(mQuestionBank[mCurrentIndex].getQuestion());
	}
	
	private void checkAnswer(boolean answer){

		int toastMessage = mIsCheater? R.string.cheat_message : (mQuestionBank[mCurrentIndex].isTrueQuestion() == answer)? R.string.correct_toast : R.string.incorrect_toast;
		Toast.makeText(QuizActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
	}

    private void clearCheating(){
        mIsCheater = false;
    }
	
}
