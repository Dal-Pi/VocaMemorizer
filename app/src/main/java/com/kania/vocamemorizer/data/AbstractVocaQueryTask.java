package com.kania.vocamemorizer.data;

import android.os.AsyncTask;

/**
 * Created by Seonghan Kim on 2016-11-02.
 */

public abstract class AbstractVocaQueryTask extends AsyncTask<Void, Void, Void> {

    //TODO change as Enum
    public static final int QUERY_TYPE_NOT_DEFINED = 0;
    public static final int QUERY_TYPE_INIT = 1;
    public static final int QUERY_TYPE_INSERT = 2;
    public static final int QUERY_TYPE_DELETE = 3;
    public static final int QUERY_TYPE_UPDATE = 4;

    public interface QueryEndCallback {
        void onEndQuery(int type);
    }

    private QueryEndCallback mCallback;
    private int mQueryType = QUERY_TYPE_NOT_DEFINED;

    public AbstractVocaQueryTask(QueryEndCallback callback) {
        mCallback = callback;
    }

    public void setQueryType(int queryType) {
        mQueryType = queryType;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mCallback != null) {
            mCallback.onEndQuery(mQueryType);
        }
    }
}
