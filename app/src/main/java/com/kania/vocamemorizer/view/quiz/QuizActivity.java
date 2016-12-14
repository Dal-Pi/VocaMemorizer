package com.kania.vocamemorizer.view.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.util.ViewUtil;
import com.kania.vocamemorizer.view.add.AddVocaActivity;
import com.kania.vocamemorizer.view.slideshow.SlideShowActivity;

public class QuizActivity extends AppCompatActivity implements QuizViewFragment.RequestAddVocaCallback {

    public static final int REQ_CODE_ADD_VOCA = 100;

    private FragmentManager mFragmentManager;

    private QuizViewFragment mQuizViewFragment;

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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof QuizViewFragment) {
            mQuizViewFragment = (QuizViewFragment)fragment;
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
    public void onBackPressed() {
        if (mQuizViewFragment != null) {
            if (mQuizViewFragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mQuizViewFragment != null) {
            if (mQuizViewFragment.onKeyUp(keyCode, event)) {
                return true;
            }
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_F8:
                onRequestAdd();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_ADD_VOCA) {
            if (resultCode == RESULT_OK) {
                if (mQuizViewFragment != null) {
                    mQuizViewFragment.refresh();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.menu_activity_main_slideshow:
                SlideShowActivity.actionStartSlideShow(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestAdd() {
        AddVocaActivity.actionStartAddVoca(this, REQ_CODE_ADD_VOCA);
    }

    private void setQuizView() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.act_main_container, QuizViewFragment.newInstance(),
                QuizViewFragment.class.getCanonicalName());
        ft.commit();
    }
}
