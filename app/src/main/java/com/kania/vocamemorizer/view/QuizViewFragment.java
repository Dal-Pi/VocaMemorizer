package com.kania.vocamemorizer.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.presenter.IQuizViewPresenter;
import com.kania.vocamemorizer.presenter.QuizViewPresenter;
import com.kania.vocamemorizer.util.ViewUtil;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class QuizViewFragment extends Fragment implements IQuizView, View.OnClickListener {
    private IQuizViewPresenter mPresenter;

    private ViewGroup mEmptyView;
    private ViewGroup mQuizView;
    private EditText mEditWord;
    private ImageButton mImgBtnInfo;
    private ImageButton mImgBtnVerify;
    private ListView mListMeanings;
    private ArrayAdapter<String> mAdapter;

    private boolean mEmptyViewEnabled;

    public static QuizViewFragment newInstance() {
        return new QuizViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new QuizViewPresenter(getActivity(), this);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        mEmptyView = (ViewGroup) view.findViewById(R.id.frag_quiz_layout_empty);
        mQuizView = (ViewGroup) view.findViewById(R.id.frag_quiz_layout_quiz);
        mQuizView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyViewEnabled = true;
        mEditWord = (EditText) view.findViewById(R.id.frag_quiz_edit_word);
        mEditWord.setSingleLine();
        ViewUtil.setEditColor(mEditWord, getResources().getColor(R.color.pastel_blue));
        mImgBtnInfo = (ImageButton) view.findViewById(R.id.frag_quiz_imgbtn_info);
        mImgBtnInfo.setOnClickListener(this);
        ViewUtil.setImageButtonColor(mImgBtnInfo, getResources().getColor(R.color.pastel_blue));
        mImgBtnVerify = (ImageButton) view.findViewById(R.id.frag_quiz_imgbtn_verify);
        mImgBtnVerify.setOnClickListener(this);
        ViewUtil.setImageButtonColor(mImgBtnVerify, getResources().getColor(R.color.pastel_blue));
        mListMeanings = (ListView) view.findViewById(R.id.frag_quiz_list_meanings);
        mListMeanings.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.startQuiz();
    }

    @Override
    public void setEmptyView() {
        enableEmptyView(true);
    }

    @Override
    public void setVoca(VocaData voca) {
        enableEmptyView(false);
        hideInputMethod(mEditWord);
        if (voca.isIncorrectPrev()) {
            mImgBtnInfo.setVisibility(View.VISIBLE);
            setColorTheme(getResources().getColor(R.color.color_incorrected));
        } else {
            if (voca.getCorrectCount() != 0) {
                mImgBtnInfo.setVisibility(View.VISIBLE);
                setColorTheme(getResources().getColor(R.color.color_corrected));
            } else { //first time
                mImgBtnInfo.setVisibility(View.GONE);
                setColorTheme(getResources().getColor(R.color.color_first_time));
            }
        }
        mEditWord.setText("");
        mAdapter.clear();
        for (String meaning : voca.getMeanings()) {
            mAdapter.add(meaning);
        }
        mAdapter.notifyDataSetChanged();
        mEditWord.requestFocus();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frag_quiz_imgbtn_verify) {
            String candidate = mEditWord.getText().toString();
            mPresenter.selectVerify(candidate);
        } else if (id == R.id.frag_quiz_imgbtn_info) {
            mPresenter.selectInfo();
        }
    }

    @Override
    public void showResultDialog(String word, boolean isCorrected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int color;
        if (isCorrected) {
            builder.setTitle(R.string.frag_quiz_dlg_title_correct)
                    .setMessage(word)
                    .setIcon(R.drawable.ic_sentiment_very_satisfied_black_48dp);
            color = getResources().getColor(R.color.color_corrected);
        } else {
            builder.setTitle(R.string.frag_quiz_dlg_title_incorrect)
                    .setMessage(getString(R.string.frag_quiz_dlg_msg_incorrect_prefix) + " " + word)
                    .setIcon(R.drawable.ic_sentiment_dissatisfied_black_48dp);
            color = getResources().getColor(R.color.color_incorrected);
        }
        builder.setPositiveButton(R.string.frag_quiz_dlg_btn_remain,
                new ResultDialogRemainClickListener())
                .setNegativeButton(R.string.frag_quiz_dlg_btn_remove,
                        new ResultDialogRemoveClickListener());
        AlertDialog dialog = builder.create();
        ViewUtil.setDialogButtonColor(dialog, color, color, color);
        dialog.show();
    }

    @Override
    public void showInfoDialog(boolean isIncorrectPrevTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int color;
        builder.setTitle(R.string.frag_quiz_dlg_title_info);
        if (isIncorrectPrevTime) {
            builder.setMessage(R.string.frag_quiz_dlg_msg_info_incorrect_prev);
            color = getResources().getColor(R.color.color_incorrected);
        } else {
            builder.setMessage(R.string.frag_quiz_dlg_msg_info_correct_prev);
            color = getResources().getColor(R.color.color_corrected);
        }
        builder.setPositiveButton(R.string.frag_quiz_dlg_btn_ok, null);
        AlertDialog dialog = builder.create();
        ViewUtil.setDialogButtonColor(dialog, color, color, color);
        dialog.show();
    }

    private void enableEmptyView(boolean enable) {
        if (mEmptyViewEnabled == enable) {
            return;
        }
        mEmptyViewEnabled = enable;
        if (mEmptyViewEnabled) {
            hideInputMethod(mEditWord);
            mQuizView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mQuizView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void hideInputMethod(EditText edit) {
        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void setColorTheme(int color) {
        ViewUtil.setImageButtonColor(mImgBtnInfo, color);
        ViewUtil.setEditColor(mEditWord, color);
        ViewUtil.setImageButtonColor(mImgBtnVerify, color);
    }

    class ResultDialogRemainClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.selectRemain();
        }
    }

    class ResultDialogRemoveClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.selectRemove();
        }
    }
}
