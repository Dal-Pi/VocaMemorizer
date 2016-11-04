package com.kania.vocamemorizer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;

/**
 * Created by Seonghan Kim on 2016-11-02.
 */

public class InitVocaListTask extends AbstractVocaQueryTask {

    private Context mContext;
    private LinkedList<VocaData> mVocaList;

    public InitVocaListTask(Context context, LinkedList<VocaData> vocaList,
                            QueryEndCallback callback) {
        super(callback);
        mContext = context;
        mVocaList = vocaList;
        setQueryType(QUERY_TYPE_INIT);
    }

    @Override
    protected Void doInBackground(Void... params) {
        VocaDbHelper dbHelper = new VocaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String[] projection = {
                VocaDbHelper.VocaEntry._ID,
                VocaDbHelper.VocaEntry.VOCA_WORD,
                VocaDbHelper.VocaEntry.VOCA_MEANING,
                VocaDbHelper.VocaEntry.LAST_UPDATED,
                VocaDbHelper.VocaEntry.CORRECT_COUNT,
                VocaDbHelper.VocaEntry.IS_INCORRECT_PREV
        };
        final String sortOrder = VocaDbHelper.VocaEntry.LAST_UPDATED + " ASC";
        Cursor vocaListCursor = db.query(VocaDbHelper.VocaEntry.TABLE_NAME,
                projection, null, null, null, null, sortOrder);
        initVocaLinkedList(vocaListCursor, mVocaList);
        return null;
    }

    private void initVocaLinkedList(Cursor cursor, LinkedList<VocaData> linkedList) {
        VocaData inputPoint = getInputPointData();
        linkedList.add(inputPoint);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry._ID));
            String word = cursor.getString(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry.VOCA_WORD));
            String meaningString = cursor.getString(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry.VOCA_MEANING));
            long lastUpdated = cursor.getInt(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry.LAST_UPDATED));
            int correctCount = cursor.getInt(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry.CORRECT_COUNT));
            boolean isIncorrectPrev = (1 == cursor.getInt(cursor
                    .getColumnIndexOrThrow(VocaDbHelper.VocaEntry.IS_INCORRECT_PREV)));

            VocaData voca = new VocaData(id, word, meaningString, lastUpdated, correctCount,
                    isIncorrectPrev);

            if (voca.isIncorrectPrev) {
                linkedList.add(linkedList.indexOf(inputPoint), voca);
            } else {
                linkedList.addLast(voca);
            }
        }
    }

    private VocaData getInputPointData() {
        return new VocaData(VocaData.ID_INPUT_POINT, VocaData.INPUT_POINT_DEFINE,
                VocaData.INPUT_POINT_DEFINE, 0, 0, false);
    }
}
