package com.kania.vocamemorizer.presenter;

import android.content.Context;

import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.data.VocaProvider;
import com.kania.vocamemorizer.view.IAddVocaView;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class AddVocaViewPresenter implements IAddVocaViewPresenter {

    private Context mContext;
    private IAddVocaView mView;

    public AddVocaViewPresenter(Context context, IAddVocaView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void addVoca(String word, String meaningString) {
        VocaData voca = new VocaData(VocaData.ID_NOT_NEEDED, word.trim(), meaningString);
        voca.touch();
        VocaProvider.getInstance().insertVoca(mContext, voca,
                new VocaProvider.RequestEndCallback() {
                    @Override
                    public void onEndRequest() {
                        mView.addFinished();
                    }
                });
    }
}
