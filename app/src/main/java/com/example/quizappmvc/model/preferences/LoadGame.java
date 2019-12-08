package com.example.quizappmvc.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quizappmvc.model.ProgressCounter;

import static android.content.Context.MODE_PRIVATE;

public class LoadGame {

    private static final String MESSAGE_ID = "previous game";

    private SharedPreferences sharedData;

    public LoadGame(Context context) {
        this.sharedData = context.getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
    }

    public ProgressCounter loadGame(ProgressCounter progressCounter){
        if (sharedData != null){
            progressCounter.setScore(sharedData.getInt("progressCounter", 0));
            progressCounter.setQuestionsTaken(sharedData.getInt("questions taken", 0));
            progressCounter.setCurrentQuestion(sharedData.getInt("current question", 0));
            progressCounter.setHighScore(sharedData.getInt("highScore", 0));
            progressCounter.setHighScoreQuestionsTaken(sharedData.getInt("questions on highScore", 0));
        }
        return progressCounter;
    }

}
