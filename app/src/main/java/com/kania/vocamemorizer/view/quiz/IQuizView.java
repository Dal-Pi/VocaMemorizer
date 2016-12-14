package com.kania.vocamemorizer.view.quiz;

import com.kania.vocamemorizer.data.VocaData;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public interface IQuizView {
    void setEmptyView();
    void setVoca(VocaData voca);
    void showResultDialog(String word, boolean isCorrected);
    void showInfoDialog(boolean isIncorrectPrevTime);
}
