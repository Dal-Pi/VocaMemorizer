package com.kania.vocamemorizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class AddVocaTask extends AbstractVocaQueryTask {

    private Context mContext;
    private VocaData mVoca;

    public AddVocaTask(Context context, VocaData voca, QueryEndCallback callback) {
        super(callback);
        mContext = context;
        mVoca = voca;
        setQueryType(QUERY_TYPE_INSERT);
    }

    @Override
    protected Void doInBackground(Void... params) {
        VocaDbHelper dbHelper = new VocaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VocaDbHelper.VocaEntry.VOCA_WORD, mVoca.word);
        cv.put(VocaDbHelper.VocaEntry.VOCA_MEANING, mVoca.getMeaningString());
        cv.put(VocaDbHelper.VocaEntry.CORRECT_COUNT, mVoca.correctCount);
        cv.put(VocaDbHelper.VocaEntry.IS_INCORRECT_PREV, mVoca.isIncorrectPrev);
        db.insert(VocaDbHelper.VocaEntry.TABLE_NAME, null, cv);
        return null;
    }
}
