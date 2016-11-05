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
    public void selectInfo() {
        mView.showInfoDialog(mNowSetVoca.isIncorrectPrev());
    }

    @Override
    public void selectVerify(String candidate) {
        if (isCorrect(candidate.trim())) {
            mNowSetVoca.correct();
        } else {
            mNowSetVoca.incorrect();
        }
        mView.showResultDialog(mNowSetVoca.getWord(), !mNowSetVoca.isIncorrectPrev());
    }

    @Override
    public void selectRemove() {
        VocaProvider provider = VocaProvider.getInstance();
        provider.removeVoca(mContext, mNowSetVoca, new VocaProvider.RequestEndCallback() {
            @Override
            public void onEndRequest() {
                nextVoca();
            }
        });
    }

    @Override
    public void selectRemain() {
        VocaProvider provider = VocaProvider.getInstance();
        mNowSetVoca.touch();
        provider.updateVoca(mContext, mNowSetVoca, new VocaProvider.RequestEndCallback() {
            @Override
            public void onEndRequest() {
                nextVoca();
            }
        });
    }

    private boolean isCorrect(String candidate) {
        return mNowSetVoca.getWord().equalsIgnoreCase(candidate);
    }

    private void nextVoca() {
        VocaProvider provider = VocaProvider.getInstance();
        //TODO test start
        provider.printVocaListDataLog();
        //TODO test end

        mNowSetVoca = provider.poll();

        if (mNowSetVoca == null) {
            mView.setEmptyView();
            return;
        } else {
            //TODO test start
            mNowSetVoca.printDataLog();
            //TODO test end
            mView.setVoca(mNowSetVoca);
        }
    }

}
