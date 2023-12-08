package com.example.quizapp_java;

import java.io.Serializable;

public class ResultModel extends QuestionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isAnsweredCorrect;

    // No-argument constructor
    public ResultModel() {
        // Call the super constructor with default values or leave it empty
        super("", "", 0);
        this.isAnsweredCorrect = false;
    }

    public ResultModel(String questionText, String answer, int color, Boolean isAnsweredCorrect) {
        super(questionText, answer, color);
        this.isAnsweredCorrect = isAnsweredCorrect;
    }
    public Boolean getAnsweredCorrect() {
        return isAnsweredCorrect;
    }
}
