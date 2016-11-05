package com.kania.vocamemorizer.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Seonghan Kim on 2016-10-31.
 * not using setter/getter for performance
 */

public class VocaData {
    public static final String MEANING_DELIMITER = "/";
    public static final int ID_NOT_NEEDED = -2;
    public static final int ID_INPUT_POINT = -1;
    public static final String INPUT_POINT_DEFINE = "__IP__";

    private int mId;
    private String mWord;
    private ArrayList<String> mMeanings;
    private long mLastUpdated;
    private int mCorrectCount; //continually corrected count
    private boolean mIsIncorrectPrev;

    public VocaData (int id, String word, String meanings) {
        this.mId = id;
        this.mWord = word;
        this.mMeanings = getMeaningList(meanings);
        this.mLastUpdated = 0;
        this.mCorrectCount = 0;
        this.mIsIncorrectPrev = false;
    }

    public VocaData (int id, String word, String meanings, long lastUpdated, int correctCount,
                     boolean isIncorrectPrev) {
        this.mId = id;
        this.mWord = word;
        this.mMeanings = getMeaningList(meanings);
        this.mLastUpdated = lastUpdated;
        this.mCorrectCount = correctCount;
        this.mIsIncorrectPrev = isIncorrectPrev;
    }

    public boolean isInputPoint() {
        return (mId == ID_INPUT_POINT) && INPUT_POINT_DEFINE.equals(mWord);
    }

    public int getId() {
        return this.mId;
    }

    public String getWord() {
        return this.mWord;
    }

    public ArrayList<String> getMeanings() {
        return this.mMeanings;
    }

    public String getMeaningString() {
        StringBuilder sb = new StringBuilder();
        if (mMeanings != null) {
            for (int i = 0; i < mMeanings.size(); ++i) {
                sb.append(mMeanings.get(i));
                if (i < mMeanings.size() - 1) {
                    sb.append(MEANING_DELIMITER);
                }
            }
        }
        return sb.toString();
    }

    public long getLastUpdated() {
        return this.mLastUpdated;
    }

    public void touch() {
        Calendar calendar = Calendar.getInstance();
        this.mLastUpdated = calendar.getTimeInMillis();
    }

    public int getCorrectCount() {
        return this.mCorrectCount;
    }

    public void correct() {
        this.mCorrectCount++;
        this.mIsIncorrectPrev = false;
    }

    public void incorrect() {
        this.mCorrectCount = 0;
        this.mIsIncorrectPrev = true;
    }

    public boolean isIncorrectPrev() {
        return this.mIsIncorrectPrev;
    }

    private ArrayList<String> getMeaningList(String meaningString) {
        ArrayList<String> ret = new ArrayList<>();
        String[] arrMeanings = meaningString.split(MEANING_DELIMITER);
        for (String meaning : arrMeanings) {
            ret.add(meaning);
        }
        return ret;
    }

    //for test
    public void printDataLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("VocaData : ");
        sb.append(mWord);
        sb.append("(");
        sb.append(getMeaningString());
        sb.append(")_");
        sb.append("correct=");
        sb.append(mCorrectCount);
        sb.append("_prev=");
        sb.append(mIsIncorrectPrev);
        Log.d("VocaMemorizer", sb.toString());
    }
}
