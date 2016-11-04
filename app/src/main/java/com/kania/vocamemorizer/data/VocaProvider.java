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
    }

    public static VocaProvider getInstance() {
        if (mInstance == null) {
            mInstance = new VocaProvider();
        }
        return mInstance;
    }

    public void initVocaList(Context context, final RequestEndCallback callback) {
        mVocaList = new LinkedList<>();
        new InitVocaListTask(context, mVocaList, new AbstractVocaQueryTask.QueryEndCallback() {
            @Override
            public void onEndQuery(int type) {
                callback.onEndRequest();
            }
        }).execute();
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

    private int getVocaCount() {
        return mVocaList.size() - 1; //sub IP's count
    }

    public VocaData poll() {
        //1. check vocalist is empty
        if (getVocaCount() == 0) { //if only has IP
            return null;
        }
        //2. getFront and sent at last if it is IP
        VocaData voca = mVocaList.remove();
        if (voca.isInputPoint()) {
            mVocaList.addLast(voca);
            voca = mVocaList.remove();
        }
        return voca;
    }
}
