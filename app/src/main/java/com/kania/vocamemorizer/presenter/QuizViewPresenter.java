package com.kania.vocamemorizer.presenter;

import com.kania.vocamemorizer.view.IQuizView;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class QuizViewPresenter implements IQuizViewPresenter {

    private IQuizView mView;

    public QuizViewPresenter(IQuizView view) {
        mView = view;
    }

    @Override
    public void verify() {

    }

    @Override
    public void startQuiz() {

    }
}
