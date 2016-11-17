package com.kania.vocamemorizer.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kania.vocamemorizer.R;

public class MainActivity extends AppCompatActivity implements QuizViewFragment.AddVocaCallback {

    private FragmentManager mFragmentManager;
    private Fragment mQuizView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusbarColor();
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            setQuizView();
        }
    }

    @Override
    public void onRequestAdd() {
        setAddView();
    }

    private void changeStatusbarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int flag = decorView.getSystemUiVisibility();
            flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flag);
        }
    }
    private void setQuizView() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mQuizView = QuizViewFragment.newInstance();
        ft.add(R.id.act_main_container, mQuizView,
                QuizViewFragment.class.getCanonicalName());
        ft.commit();
    }

    private void setAddView() {
        if (mQuizView != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.act_main_container, AddVocaFragment.newInstance(),
                    AddVocaFragment.class.getCanonicalName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
