package com.example.quizappmvc.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quizappmvc.model.ProgressCounter;

import static android.content.Context.MODE_PRIVATE;

public class SaveGame {
    private static final String MESSAGE_ID = "previous game";
    private SharedPreferences preferences;

    public SaveGame(Context context) {
        this.preferences = context.getSharedPreferences(MESSAGE_ID ,MODE_PRIVATE);
    }

    public void saveGame(ProgressCounter progressCounter){
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("progressCounter", progressCounter.getScore());
        editor.putInt("current question", progressCounter.getCurrentQuestion());
        editor.putInt("questions taken", progressCounter.getQuestionsTaken());
        editor.putInt("highScore", progressCounter.getHighScore());
        editor.putInt("questions on highScore", progressCounter.getHighScoreQuestionsTaken());
        editor.apply();
    }

}
