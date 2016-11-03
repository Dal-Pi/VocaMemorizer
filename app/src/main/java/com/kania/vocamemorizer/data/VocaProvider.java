package com.kania.vocamemorizer.data;

import android.content.Context;

import java.util.LinkedList;
/**
 * Created by Seonghan Kim on 2016-10-31.
 */

public class VocaProvider {

    public static VocaProvider mInstance;
    private LinkedList<VocaData> mVocaList;

    public interface RequestEndCallback {
        void onEndRequest(); //TODO need to verify succeed
    }

    private VocaProvider() {
        mVocaList = new LinkedList<>();
    }

    public static VocaProvider getInstance() {
        if (mInstance == null) {
            mInstance = new VocaProvider();
        }
        return mInstance;
    }

    public void initVocaMap(Context context, final RequestEndCallback callback) {
        new InitVocaListTask(context, mVocaList, new AbstractVocaQueryTask.QueryEndCallback() {
            @Override
            public void onEndQuery(int type) {
                callback.onEndRequest();
            }
        });
    }

    public void insertVoca(Context context, final VocaData voca,
                           final RequestEndCallback callback) {
        new AddVocaTask(context, voca, new AbstractVocaQueryTask.QueryEndCallback() {
            @Override
            public void onEndQuery(int type) {
                mVocaList.addLast(voca);
                callback.onEndRequest();
            }
        }).execute();
    }
}
