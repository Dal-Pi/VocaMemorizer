package com.kania.vocamemorizer.presenter;

import android.content.Context;

import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.data.VocaProvider;
import com.kania.vocamemorizer.view.IQuizView;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class QuizViewPresenter implements IQuizViewPresenter {

    private Context mContext;
    private IQuizView mView;

    private VocaData mNowSetVoca;

    public QuizViewPresenter(Context context, IQuizView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void startQuiz() {
        VocaProvider.getInstance().initVocaList(mContext, new VocaProvider.RequestEndCallback() {
            @Override
            public void onEndRequest() {
                nextVoca();
            }
        });
    }

    @Override
    public void verify() {

    }

    private void nextVoca() {
        VocaProvider provider = VocaProvider.getInstance();
        mNowSetVoca = VocaProvider.getInstance().poll();
        if (mNowSetVoca == null) {
            mView.setEmptyView();
            return;
        } else {
            mView.setVoca(mNowSetVoca);
        }




    }


}
