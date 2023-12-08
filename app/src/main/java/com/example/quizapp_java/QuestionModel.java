package com.example.quizapp_java;

import java.io.Serializable;

public class QuestionModel implements Serializable {
    protected String questionText;
    protected String answer;
    protected int color;

    public QuestionModel(String questionText, String answer, int color) {
        this.questionText = questionText;
        this.answer = answer;
        this.color = color;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public int getColor() {
        return color;
    }
}
