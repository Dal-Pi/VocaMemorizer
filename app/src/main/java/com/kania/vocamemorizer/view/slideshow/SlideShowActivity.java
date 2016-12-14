package com.kania.vocamemorizer.view.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.data.VocaProvider;
import com.kania.vocamemorizer.util.ViewUtil;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private ViewGroup mLayoutWord;
    private TextView mTextWord;
    private ViewGroup mLayoutMeaning;
    private TextView mTextMeaning;

    private PostponedSwitch mPostponedSwitch;

    private SlideShowTask mSlideShowTask;

    public static void actionStartSlideShow(Context fromContext) {
        Intent intent = new Intent(fromContext, SlideShowActivity.class);
        fromContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        //TODO set immersive mode
        Toolbar toolbar = (Toolbar)findViewById(R.id.act_slide_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowTitleEnabled(false);
        }

        ViewUtil.changeStatusbarColor(this);

        //TODO make action to reverse
        mPostponedSwitch = new PostponedSwitch();

        initView();

        mSlideShowTask = new SlideShowTask();
        mSlideShowTask.execute();
    }

    @Override
    protected void onDestroy() {
        if (mSlideShowTask != null) {
            mSlideShowTask.cancel(false);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        mLayoutWord = (ViewGroup)findViewById(R.id.act_slide_layout_word);
        mTextWord = (TextView)findViewById(R.id.act_slide_text_word);
        mLayoutMeaning = (ViewGroup)findViewById(R.id.act_slide_layout_meaning);
        mTextMeaning = (TextView)findViewById(R.id.act_slide_text_meaning);
    }

    class PostponedSwitch {
        private boolean enabled;

        public PostponedSwitch() {
            enabled = false;
        }

        public void on() {
            enabled = true;
        }

        public boolean get() {
            if (enabled) {
                enabled = false;
                return true;
            }
            return false;
        }
    }

    class SlideShowTask extends AsyncTask<Void, Void, Void> {
        private static final int MAX_SEQUENCE = 2;
        private static final int SEQUENCE_FIRST = 0;
        private static final int SEQUENCE_SECOND = 1;

        private ArrayList<VocaData> mVocaList;
        private VocaData mVoca;
        private int mCount;
        private boolean mWordFirst;

        private int mDelay;

        public SlideShowTask() {
            mCount = SEQUENCE_FIRST;
            mWordFirst = true;
            //TODO change dynamically
            mDelay = 1500;
            initVocaList();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //TODO
            if (mWordFirst) {
                if (mCount == SEQUENCE_FIRST) {
                    setSequenceFirst(mTextMeaning, mLayoutMeaning, mTextWord, mVoca.getWord());
                } else if (mCount == SEQUENCE_SECOND) {
                    setSequenceSecond(mLayoutMeaning, mTextMeaning,
                            getEnteredMeaningString(mVoca.getMeanings()));
                }
            } else {
                if (mCount == SEQUENCE_FIRST) {
                    setSequenceFirst(mTextWord, mLayoutWord, mTextMeaning,
                            getEnteredMeaningString(mVoca.getMeanings()));
                } else if (mCount == SEQUENCE_SECOND) {
                    setSequenceSecond(mLayoutWord, mTextWord, mVoca.getWord());
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {
                mCount = mCount % MAX_SEQUENCE;
                if (mCount == SEQUENCE_FIRST) {
                    if (mVocaList == null || mVocaList.size() == 0) {
                        initVocaList();
                    }
                    mVoca = mVocaList.remove(0);
                    if (mPostponedSwitch != null && mPostponedSwitch.get()) {
                        mWordFirst = !mWordFirst;
                    }
                }
                publishProgress();

                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mCount++;
            }
            Log.d("VocaMemorizer", "task finish condition. task = " + hashCode());
            return null;
        }

        private void initVocaList() {
            mVocaList = VocaProvider.getInstance().getAllList();
        }

        private void setSequenceFirst(TextView textForHide, ViewGroup layoutForHide,
                                      TextView textForShow, String stringForShow) {
            textForHide.setText("");
            layoutForHide.setVisibility(View.GONE);
            if (mVoca != null) {
                textForShow.setText(stringForShow);
            }
        }

        private void setSequenceSecond(ViewGroup layoutForShow, TextView textForShow,
                                       String stringForShow) {
            layoutForShow.setVisibility(View.VISIBLE);
            textForShow.setText(stringForShow);
        }

        private String getEnteredMeaningString(ArrayList<String> meaningList) {
            StringBuilder ret = new StringBuilder();
            for (String meaning : meaningList) {
                ret.append(meaning).append("\n");
            }
            return ret.toString();
        }
    }
}
