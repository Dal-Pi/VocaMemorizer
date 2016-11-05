package com.kania.vocamemorizer.view;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kania.vocamemorizer.R;

public class MainActivity extends AppCompatActivity implements AddVocaFragment.EndAddVocaCallback {

    private FragmentManager mFragmentManager;
    private Fragment mQuizView;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeStatusbarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.act_main_toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();

        mFab = (FloatingActionButton) findViewById(R.id.act_main_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddView();
            }
        });
        setQuizView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEndAddVoca() {
        mFab.setVisibility(View.VISIBLE);
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
            mFab.setVisibility(View.GONE);
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.act_main_container, AddVocaFragment.newInstance(this),
                    AddVocaFragment.class.getCanonicalName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
