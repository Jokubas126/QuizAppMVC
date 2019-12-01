package com.example.quizappmvc.Model;

import androidx.annotation.NonNull;

public class Question {
    private String questionText;
    private boolean correctAnswer;

    public Question(){}

    public Question(String questionText, boolean correctAnswer){
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" + "questionText="+ questionText + '\'' +
                ", correctAnswer=" + correctAnswer + "}";
    }
}
