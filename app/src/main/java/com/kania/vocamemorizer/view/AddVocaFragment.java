package com.kania.vocamemorizer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.presenter.AddVocaViewPresenter;
import com.kania.vocamemorizer.presenter.IAddVocaViewPresenter;
import com.kania.vocamemorizer.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class AddVocaFragment extends Fragment implements IAddVocaView {

    private IAddVocaViewPresenter mPresenter;

    private EditText mEditWord;
    private LinearLayout mLayoutMeanings;

    private ArrayList<EditText> mMeaningEdits;

    private AddFinishedCallback mCallback;

    public static AddVocaFragment newInstance() {
        AddVocaFragment fragment = new AddVocaFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddFinishedCallback) {
            mCallback = (AddFinishedCallback)context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new AddVocaViewPresenter(getActivity(), this);
        mMeaningEdits = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        if (view != null) {
            initView(view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void addFinished() {
        finishAddView(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        MenuItem itemAdd = menu.findItem(R.id.menu_fragment_add_add);
        ViewUtil.setMenuItemColor(itemAdd, getResources().getColor(R.color.color_add_confirm));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finishAddView(false);
                return true;
            case R.id.menu_fragment_add_add:
                add();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onBackPressed() {
        finishAddView(false);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F12:
                add();
                return true;
        }
        return false;
    }

    private void initView(View rootView) {
        mEditWord = (EditText)rootView.findViewById(R.id.frag_add_edit_word);
        mEditWord.setSingleLine();
        mEditWord.requestFocus();
        ViewUtil.setEditColor(mEditWord, getResources().getColor(R.color.colorAccent));
        mLayoutMeanings = (LinearLayout)rootView.findViewById(R.id.frag_add_layout_meanings);
        addMeaningEdit();
    }

    private void addMeaningEdit() {
        EditText edit = new EditText(getActivity());
        edit.setSingleLine();
        edit.setHint(getString(R.string.frag_add_edit_meaning_hint) + " " +
                (mMeaningEdits.size() + 1));
        edit.addTextChangedListener(new NonEmptyTextWatcher());
        mLayoutMeanings.addView(edit);
        mMeaningEdits.add(edit);
    }

    private void add() {
        String word = mEditWord.getText().toString();
        if (word.trim().length() == 0) {
            Toast.makeText(getActivity(), R.string.frag_add_toast_empty_text,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mMeaningEdits.size(); ++i) {
            String candidate = mMeaningEdits.get(i).getText().toString();
            if (candidate.trim().length() > 0) {
                if (sb.length() > 0) {
                    sb.append(VocaData.MEANING_DELIMITER);
                }
                sb.append(candidate);
            }
        }
        if (sb.length() == 0) {
            Toast.makeText(getActivity(), R.string.frag_add_toast_empty_meaning,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.addVoca(word, sb.toString());
    }

    private void finishAddView(boolean isAdded) {
        if (mCallback != null) {
            if (isAdded) {
                mCallback.addFinished(AddFinishedCallback.ADDED);
            } else {
                mCallback.addFinished(AddFinishedCallback.CANCELED);
            }
            return;
        }
        getActivity().onBackPressed();
    }

    interface AddFinishedCallback {
        int CANCELED = 0;
        int ADDED = 1;
        void addFinished(int isAdded);
    }

    class NonEmptyTextWatcher implements TextWatcher {
        private boolean mAdded = false;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mAdded && s.length() > 0) {
                addMeaningEdit();
                mAdded = true;
            }
        }
    }
}
