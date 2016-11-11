package com.kania.vocamemorizer.data;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
/**
 * Created by Seonghan Kim on 2016-10-31.
 */

public class VocaProvider {

    public static VocaProvider mInstance;
    private LinkedList<VocaData> mVocaList;
    private VocaData mInputPoint;

    public interface RequestEndCallback {
        void onEndRequest(); //TODO need to selectVerify succeed
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
                setInputPoint();
                callback.onEndRequest();
            }
        }).execute();
    }

    public void insertVoca(Context context, final VocaData voca,
                           final RequestEndCallback callback) {
        new UpdateVocaTask(context, AbstractVocaQueryTask.QUERY_TYPE_INSERT, voca,
                new AbstractVocaQueryTask.QueryEndCallback() {
                    @Override
                    public void onEndQuery(int type) {
                        mVocaList.addLast(voca);
                        callback.onEndRequest();
                    }
                }).execute();
    }

    public void updateVoca(Context context, final VocaData voca,
                           final RequestEndCallback callback) {
        new UpdateVocaTask(context, AbstractVocaQueryTask.QUERY_TYPE_UPDATE, voca,
                new AbstractVocaQueryTask.QueryEndCallback() {
                    @Override
                    public void onEndQuery(int type) {
                        VocaData target = mVocaList.remove(mVocaList.indexOf(voca));
                        if (target != null) {
                            if (voca.isIncorrectPrev()) {
                                mVocaList.add(mVocaList.indexOf(mInputPoint), voca);
                            } else {
                                mVocaList.addLast(voca);
                            }
                        }
                        callback.onEndRequest();
                    }
                }).execute();
    }

    public void removeVoca(Context context, final VocaData voca,
                           final RequestEndCallback callback) {
        new UpdateVocaTask(context, AbstractVocaQueryTask.QUERY_TYPE_DELETE, voca,
                new AbstractVocaQueryTask.QueryEndCallback() {
                    @Override
                    public void onEndQuery(int type) {
                        mVocaList.remove(mVocaList.indexOf(voca));
                        callback.onEndRequest();
                    }
                }).execute();
    }

    public VocaData poll() {
        //1. check vocalist is empty
        if (getVocaCount() == 0) { //if only has IP
            return null;
        }
        //2. getFront and sent at last if it is IP
        if (mVocaList.getFirst().isInputPoint()) {
            mVocaList.addLast(mVocaList.remove());
        }
        //3. get word
        return mVocaList.getFirst();
    }

    private int getVocaCount() {
        return mVocaList.size() - 1; //sub IP's count
    }

    private void setInputPoint() {
        Iterator<VocaData> it = mVocaList.iterator();
        while (it.hasNext()) {
            VocaData candidate = it.next();
            if (candidate.isInputPoint()) {
                mInputPoint = candidate;
                break;
            }
        }
    }


    //for test
    public void printVocaListDataLog() {
        if (mVocaList == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("VocaListData : ");
        for (VocaData voca : mVocaList) {
            sb.append(voca.getWord());
            sb.append("->");
        }
        Log.d("VocaMemorizer", sb.toString());
    }
}
