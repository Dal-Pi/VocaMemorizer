package com.kania.vocamemorizer.presenter;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public interface IQuizViewPresenter {
    void startQuiz();
    void selectInfo();
    void selectVerify(String candidate);
    void selectRemain();
    void selectRemove();
}
