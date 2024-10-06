package com.example.myapplication.question;

public class Question {
    private int questionId;
    private boolean trueAnswer;

    public Question(int questionId, boolean trueAnswer){
        this.questionId = questionId;
        this.trueAnswer = trueAnswer;
    }

    public boolean getTrueAnswer() {
        return trueAnswer;
    }

    public int getQuestionId() {
        return questionId;
    }
}
