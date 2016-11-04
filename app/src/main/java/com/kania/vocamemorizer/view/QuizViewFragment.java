package com.kania.vocamemorizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.kania.vocamemorizer.R;
import com.kania.vocamemorizer.data.VocaData;
import com.kania.vocamemorizer.presenter.IQuizViewPresenter;
import com.kania.vocamemorizer.presenter.QuizViewPresenter;

/**
 * Created by Seonghan Kim on 2016-11-03.
 */

public class QuizViewFragment extends Fragment implements IQuizView {
    private IQuizViewPresenter mPresenter;

    private ViewGroup mEmptyView;
    private ViewGroup mQuizView;
    private EditText mEditWord;
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
        mEmptyView = (ViewGroup)view.findViewById(R.id.frag_quiz_layout_empty);
        mQuizView = (ViewGroup)view.findViewById(R.id.frag_quiz_layout_quiz);
        enableEmptyView(true);
        mEditWord = (EditText)view.findViewById(R.id.frag_quiz_edit_word);
        mEditWord.setSingleLine();
        mListMeanings = (ListView)view.findViewById(R.id.frag_quiz_list_meanings);
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
        mAdapter.clear();
        for (String meaning : voca.meanings) {
            mAdapter.add(meaning);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void enableEmptyView(boolean enable) {
        if (mEmptyViewEnabled == enable) {
            return;
        }
        mEmptyViewEnabled = enable;
        if (mEmptyViewEnabled) {
            mQuizView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mQuizView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }
}
