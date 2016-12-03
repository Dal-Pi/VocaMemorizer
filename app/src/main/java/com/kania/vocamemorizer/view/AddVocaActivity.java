package com.kania.vocamemorizer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.util.ViewUtil;

public class AddVocaActivity extends AppCompatActivity
        implements AddVocaFragment.AddFinishedCallback {

    private AddVocaFragment mAddVocaFragment;

    public static void actionStartAddVoca(Context fromContext, int requestCode) {
        Intent intent = new Intent(fromContext, AddVocaActivity.class);
        if (fromContext instanceof Activity) {
            ((Activity) fromContext).startActivityForResult(intent, requestCode);
        } else {
            fromContext.startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voca);
        Toolbar toolbar = (Toolbar)findViewById(R.id.act_add_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowTitleEnabled(false);
        }

        ViewUtil.changeStatusbarColor(this);

        if (savedInstanceState == null) {
            setAddView();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof AddVocaFragment) {
            mAddVocaFragment = (AddVocaFragment)fragment;
        }
        super.onAttachFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (mAddVocaFragment != null) {
            if (mAddVocaFragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAddVocaFragment != null) {
            if (mAddVocaFragment.onKeyUp(keyCode, event)) {
                return true;
            }
        }
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_F8:
//                //TODO
//                return true;
//            default:
//                return super.onKeyUp(keyCode, event);
//        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void addFinished(int isAdded) {
        if (isAdded == AddVocaFragment.AddFinishedCallback.ADDED) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void setAddView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.act_add_container, AddVocaFragment.newInstance(),
                AddVocaFragment.class.getCanonicalName());
        ft.commit();
    }
}
