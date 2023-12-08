package com.example.quizapp_java;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class QuesFragment extends Fragment {
    private TextView textView;
    private QuestionModel question;
    public QuesFragment(QuestionModel question) {
        this.question = question;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ques, container, false);
        textView = view.findViewById(R.id.textViewQuestion);
        setQuestion(question);
        return view;
    }
    public void setQuestion(QuestionModel question) {
        if (textView != null) {
            textView.setText(question.getQuestionText());
            textView.setBackgroundColor(question.getColor());
        }
    }
}