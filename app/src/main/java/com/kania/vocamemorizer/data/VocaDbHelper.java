package com.kania.vocamemorizer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Seonghan Kim on 2016-10-31.
 */

public class VocaDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "voca.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String ORDER = " ORDER BY";
    public static final String NOT_NULL = " NOT NULL";

    private static final String SQL_CREATE_VOCALIST =
            "CREATE TABLE " + VocaEntry.TABLE_NAME + " (" +
                    VocaEntry._ID + " INTEGER PRIMARY KEY," +
                    VocaEntry.VOCA_WORD + TEXT_TYPE + COMMA_SEP +
                    VocaEntry.VOCA_MEANING + TEXT_TYPE + COMMA_SEP +
                    VocaEntry.LAST_UPDATED + INTEGER_TYPE +
                    VocaEntry.CORRECT_COUNT + INTEGER_TYPE +
                    VocaEntry.IS_INCORRECT_PREV + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_VOCALIST =
            "DROP TABLE IF EXISTS " + VocaEntry.TABLE_NAME;

    public VocaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VOCALIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_VOCALIST);
        onCreate(db);
    }

    static abstract class VocaEntry implements BaseColumns {
        static final String TABLE_NAME = "vocalist";
        static final String INPUT_POINT_DEFINE = "__IP__";

        static final String VOCA_WORD = "word";
        static final String VOCA_MEANING = "meaning";
        static final String LAST_UPDATED = "last_updated";
        static final String CORRECT_COUNT = "correct_count";
        static final String IS_INCORRECT_PREV = "is_incorrect_prev";
    }
}
