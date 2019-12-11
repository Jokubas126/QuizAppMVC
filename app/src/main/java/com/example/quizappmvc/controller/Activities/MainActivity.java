package com.example.quizappmvc.controller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizappmvc.controller.StyleSetup;
import com.example.quizappmvc.model.ProgressCounter;
import com.example.quizappmvc.model.Question;
import com.example.quizappmvc.model.data.*;
import com.example.quizappmvc.R;
import com.example.quizappmvc.model.preferences.LoadGame;
import com.example.quizappmvc.model.preferences.SaveGame;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //text views used
    private TextView questionTextView;
    private TextView allAnswerScore;
    private TextView highScoreTextView;

    private ProgressCounter progressCounter;
    private SaveGame saveGameState;
    private LoadGame loadGame;

    private QuestionHolder questionHolder; //all the questions from the database
    private Question question; //current question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new StyleSetup(this, getSupportActionBar());

        progressCounter = new ProgressCounter();
        saveGameState = new SaveGame(this.getApplicationContext());
        loadGame = new LoadGame(this.getApplicationContext());

        questionHolder = new QuestionHolder();
        questionHolder.getQuestions(new QuestionListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                progressCounter = loadGame.loadGame(progressCounter);
                updateInformation();
                Toast.makeText(MainActivity.this, "Game loaded", Toast.LENGTH_SHORT).show();
            }
        });

        // buttons in the app
        Button falseButton = findViewById(R.id.false_button);
        Button trueButton = findViewById(R.id.true_button);
        Button skipButton = findViewById(R.id.skip_button);
        ImageButton restartButton = findViewById(R.id.restart_button);
        Button saveButton = findViewById(R.id.save_button);

        //text views found
        questionTextView = findViewById(R.id.question_text_view);
        allAnswerScore = findViewById(R.id.score_text_view);
        highScoreTextView = findViewById(R.id.high_score_text_view);

        //registering button to listen for a click
        falseButton.setOnClickListener(MainActivity.this);
        trueButton.setOnClickListener(MainActivity.this);
        skipButton.setOnClickListener(MainActivity.this);
        restartButton.setOnClickListener(MainActivity.this);
        saveButton.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.false_button:
                checkAnswer(false); //check an answer with the given
                break;

            case R.id.true_button:
                checkAnswer(true); //check an answer with the given
                break;

            case R.id.skip_button:
                //go to the next question
                nextQuestion();
                break;

            case R.id.restart_button:
                Toast.makeText(MainActivity.this, "Game restarted", Toast.LENGTH_SHORT).show();
                restartQuiz(); //restarts the results
                break;

            case R.id.save_button:
                saveGameState.saveGame(progressCounter);
                Toast.makeText(MainActivity.this, "Game saved", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    void checkAnswer(boolean choice){
        if(question.getCorrectAnswer() == choice){
            fadeAnimation();
            progressCounter.onCorrect();
            allAnswerScore.setText(progressCounter.getScoreText());
            nextQuestion();
        } else {
            shakeAnimation();
            nextQuestion();
        }
    }

    private void nextQuestion(){
        progressCounter.setCurrentQuestion(progressCounter.getCurrentQuestion() + 1);
        progressCounter.setQuestionsTaken(progressCounter.getQuestionsTaken() + 1);
        if (progressCounter.getCurrentQuestion() < questionHolder.getQuestionListSize()){
            question = questionHolder.getNextQuestion();
            updateInformation();
        } else{
            Toast.makeText(MainActivity.this, "This was the last question", Toast.LENGTH_SHORT).show();
        }
    }

    // needs to redo it later to change values using setters in QuestionHolder class
    void restartQuiz(){
        progressCounter.onRestart();
        updateInformation();
    }

    void updateInformation(){
        progressCounter.setScoreText(progressCounter.getScore() + "/" + progressCounter.getQuestionsTaken());
        allAnswerScore.setText(progressCounter.getScoreText());
        questionHolder.setCurrentQuestion(progressCounter.getCurrentQuestion());
        question = questionHolder.getCurrentQuestion();
        questionTextView.setText(question.getQuestionText());
        progressCounter.checkForHighScore();
        String textForHighScore = progressCounter.getHighScore() + "/" + progressCounter.getHighScoreQuestionsTaken();
        highScoreTextView.setText(textForHighScore);
    }

    @Override
    protected void onPause() {
        saveGameState.saveGame(progressCounter);
        super.onPause();
    }

    private void fadeAnimation(){
        final CardView cardView = findViewById(R.id.card_view);
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.3f);
        animation.setDuration(250);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE); // to repeat animation backwards
        questionTextView.setAnimation(animation);
        cardView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.cardRightAnswer));
            }

            @Override public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.backgroundCardLayoutColor));
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.card_view);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.cardWrongAnswer));
            }

            @Override public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.backgroundCardLayoutColor));
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });
    }
}