package com.kania.vocamemorizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Seonghan Kim on 2016-11-05.
 */

public class UpdateVocaTask extends AbstractVocaQueryTask {

    private Context mContext;
    private VocaData mVoca;

    public UpdateVocaTask(Context context, int queryType, VocaData voca,
                          QueryEndCallback callback) {
        super(callback);
        mContext = context;
        mVoca = voca;
        setQueryType(queryType);
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mQueryType == QUERY_TYPE_INSERT) {
            insertVoca();
        } else if (mQueryType == QUERY_TYPE_UPDATE) {
            updateVoca();
        } else if (mQueryType == QUERY_TYPE_DELETE) {
            removeVoca();
        }
        return null;
    }

    private void insertVoca() {
        VocaDbHelper dbHelper = new VocaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VocaDbHelper.VocaEntry.VOCA_WORD, mVoca.getWord());
        cv.put(VocaDbHelper.VocaEntry.VOCA_MEANING, mVoca.getMeaningString());
        cv.put(VocaDbHelper.VocaEntry.LAST_UPDATED, mVoca.getLastUpdated());
        cv.put(VocaDbHelper.VocaEntry.CORRECT_COUNT, mVoca.getCorrectCount());
        cv.put(VocaDbHelper.VocaEntry.IS_INCORRECT_PREV, mVoca.isIncorrectPrev());
        db.insert(VocaDbHelper.VocaEntry.TABLE_NAME, null, cv);
    }

    private void updateVoca() {
        VocaDbHelper dbHelper = new VocaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VocaDbHelper.VocaEntry.LAST_UPDATED, mVoca.getLastUpdated());
        cv.put(VocaDbHelper.VocaEntry.CORRECT_COUNT, mVoca.getCorrectCount());
        cv.put(VocaDbHelper.VocaEntry.IS_INCORRECT_PREV, mVoca.isIncorrectPrev());
        String selectionUpdateVoca = VocaDbHelper.VocaEntry._ID + " LIKE " + mVoca.getId();
        db.update(VocaDbHelper.VocaEntry.TABLE_NAME, cv, selectionUpdateVoca, null);
    }

    private void removeVoca() {
        VocaDbHelper dbHelper = new VocaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String selectionUpdateVoca = VocaDbHelper.VocaEntry._ID + " LIKE " + mVoca.getId();
        db.delete(VocaDbHelper.VocaEntry.TABLE_NAME, selectionUpdateVoca, null);
    }
}
