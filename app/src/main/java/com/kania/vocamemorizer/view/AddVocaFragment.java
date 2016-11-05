package com.kania.vocamemorizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class AddVocaFragment extends Fragment implements IAddVocaView, View.OnClickListener {

    public interface EndAddVocaCallback {
        void onEndAddVoca();
    }

    private IAddVocaViewPresenter mPresenter;

    private EditText mEditWord;
    private LinearLayout mLayoutMeanings;
    private ImageButton mBtnAdd;
    private ImageButton mBtnCancel;

    private ArrayList<EditText> mMeaningEdits;

    private EndAddVocaCallback mCallback;

    public static AddVocaFragment newInstance(EndAddVocaCallback callback) {
        AddVocaFragment fragment = new AddVocaFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddVocaViewPresenter(getActivity(), this);
        mMeaningEdits = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        if (view != null) {
            initView(view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback.onEndAddVoca();
    }

    @Override
    public void finish() {
        finishAddView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frag_add_imgbtn_add) {
            add();
        } else if (id == R.id.frag_add_imgbtn_cancle) {
            finishAddView();
        }
    }

    private void initView(View rootView) {
        mEditWord = (EditText)rootView.findViewById(R.id.frag_add_edit_word);
        mEditWord.setSingleLine();
        mEditWord.requestFocus();
        ViewUtil.setEditColor(mEditWord, getResources().getColor(R.color.colorAccent));
        mLayoutMeanings = (LinearLayout)rootView.findViewById(R.id.frag_add_layout_meanings);
        addMeaningEdit();
        mBtnAdd = (ImageButton)rootView.findViewById(R.id.frag_add_imgbtn_add);
        if (mBtnAdd != null) {
            mBtnAdd.setOnClickListener(this);
        }
        ViewUtil.setImageButtonColor(mBtnAdd, getResources().getColor(R.color.color_add_confirm));
        mBtnCancel = (ImageButton)rootView.findViewById(R.id.frag_add_imgbtn_cancle);
        if (mBtnCancel != null) {
            mBtnCancel.setOnClickListener(this);
        }
        ViewUtil.setImageButtonColor(mBtnCancel, getResources().getColor(R.color.color_add_cancel));
    }

    private void setCallback(EndAddVocaCallback callback) {
        mCallback = callback;
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

    private void finishAddView() {
        getActivity().onBackPressed();
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
