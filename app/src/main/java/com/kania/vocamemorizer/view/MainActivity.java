package com.kania.vocamemorizer.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.util.ViewUtil;

public class MainActivity extends AppCompatActivity implements QuizViewFragment.AddVocaCallback {

    private FragmentManager mFragmentManager;
    private Fragment mQuizView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.act_main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
        }


        ViewUtil.changeStatusbarColor(this);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            setQuizView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        ViewUtil.setMenuItemColor(menu.findItem(R.id.menu_activity_main_settings),
                getResources().getColor(R.color.purple));
        ViewUtil.setMenuItemColor(menu.findItem(R.id.menu_activity_main_slideshow),
                getResources().getColor(R.color.red));
        return true;
    }

    @Override
    public void onRequestAdd() {
        AddVocaActivity.actionStartAddVoca(this);
    }

    private void setQuizView() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mQuizView = QuizViewFragment.newInstance();
        ft.add(R.id.act_main_container, mQuizView,
                QuizViewFragment.class.getCanonicalName());
        ft.commit();
    }
}
