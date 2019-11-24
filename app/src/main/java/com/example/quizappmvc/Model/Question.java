package com.example.quizappmvc.Model;

public class Question {
    private int answerResourceId;
    private boolean answer;

    public Question(int answerResourceId, boolean answer){
        this.answerResourceId = answerResourceId;
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public int getAnswerResourceId() {
        return answerResourceId;
    }
}
