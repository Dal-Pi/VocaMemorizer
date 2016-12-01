package com.kania.vocamemorizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.util.ViewUtil;

public class AddVocaActivity extends AppCompatActivity {

    public static void actionStartAddVoca(Context fromContext) {
        Intent intent = new Intent(fromContext, AddVocaActivity.class);
        fromContext.startActivity(intent);
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

    private void setAddView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.act_add_container, AddVocaFragment.newInstance(),
                AddVocaFragment.class.getCanonicalName());
        ft.commit();
    }
}
