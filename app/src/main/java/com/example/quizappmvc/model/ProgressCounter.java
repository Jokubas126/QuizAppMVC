package com.example.quizappmvc.model;

public class ProgressCounter {

    private int currentQuestion; //question counter
    private int score; //score of the player
    private int questionsTaken; // questions that were already asked
    private String scoreText; //score text
    private int highScore;
    private int highScoreQuestionsTaken;

    public ProgressCounter(){}

    public ProgressCounter(int currentQuestion, int score, int questionsTaken, String scoreText, int highScore, int highScoreQuestionsTaken) {
        this.currentQuestion = currentQuestion;
        this.score = score;
        this.questionsTaken = questionsTaken;
        this.scoreText = scoreText;
        this.highScore = highScore;
        this.highScoreQuestionsTaken = highScoreQuestionsTaken;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getHighScoreQuestionsTaken() {
        return highScoreQuestionsTaken;
    }

    public int getQuestionsTaken() {
        return questionsTaken;
    }

    public int getScore() {
        return score;
    }

    public String getScoreText() {
        return scoreText;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setHighScoreQuestionsTaken(int highScoreQuestionsTaken) {
        this.highScoreQuestionsTaken = highScoreQuestionsTaken;
    }

    public void setQuestionsTaken(int questionsTaken) {
        this.questionsTaken = questionsTaken;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }
}
