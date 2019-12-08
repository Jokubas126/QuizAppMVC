package com.example.quizappmvc.controller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_ID = "previous progressCounter";

    // buttons in the app
    private Button falseButton;
    private Button trueButton;
    private Button skipButton;
    private Button saveButton;
    private ImageButton restartButton;

    //text views used
    private TextView questionTextView;
    private TextView allAnswerScore;
    private TextView highScoreTextView;

    private ProgressCounter progressCounter = new ProgressCounter();

    private QuestionHolder questionHolder; //all the questions from the database
    private Question question; //current question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new StyleSetup(this, getSupportActionBar());

        questionHolder = new QuestionHolder();
        questionHolder.getQuestions(new QuestionListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                loadGame();
            }
        });
        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        skipButton = findViewById(R.id.skip_button);
        restartButton = findViewById(R.id.restart_button);
        saveButton = findViewById(R.id.save_button);
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
                saveGame();
                break;
        }
    }

    void checkAnswer(boolean choice){
        boolean correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == choice){
            fadeAnimation();
            progressCounter.setScore(progressCounter.getScore() + 1);
            progressCounter.setScoreText(progressCounter.getScore() + "/" + progressCounter.getQuestionsTaken());
            allAnswerScore.setText(progressCounter.getScoreText());
            nextQuestion();
            checkForHighScore();
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
        progressCounter.setScore(0);
        progressCounter.setQuestionsTaken(0);
        progressCounter.setCurrentQuestion(0);
        updateInformation();
        saveGame();
    }

    void updateInformation(){
        progressCounter.setScoreText(progressCounter.getScore() + "/" + progressCounter.getQuestionsTaken());
        allAnswerScore.setText(progressCounter.getScoreText());
        questionHolder.setCurrentQuestion(progressCounter.getCurrentQuestion());
        question = questionHolder.getCurrentQuestion();
        questionTextView.setText(question.getQuestionText());
        checkForHighScore();
        String textForHighScore = progressCounter.getHighScore() + "/" + progressCounter.getHighScoreQuestionsTaken();
        highScoreTextView.setText(textForHighScore);
    }

    private void checkForHighScore(){
        if (progressCounter.getScore() > progressCounter.getHighScore()){
            progressCounter.setHighScore(progressCounter.getScore());
            progressCounter.setHighScoreQuestionsTaken(progressCounter.getQuestionsTaken());
        }

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

    private void loadGame(){
        if (getSharedPreferences(MESSAGE_ID, MODE_PRIVATE) != null){
            SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID , MODE_PRIVATE);
            progressCounter.setScore(getSharedData.getInt("progressCounter", 0));
            progressCounter.setQuestionsTaken(getSharedData.getInt("questions taken", 0));
            progressCounter.setCurrentQuestion(getSharedData.getInt("current question", 0));
            progressCounter.setHighScore(getSharedData.getInt("highScore", 0));
            progressCounter.setHighScoreQuestionsTaken(getSharedData.getInt("questions on highScore", 0));
            Toast.makeText(MainActivity.this, "Game loaded", Toast.LENGTH_SHORT).show();
        }
        updateInformation();
    }

    private void saveGame(){
        SharedPreferences preferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("progressCounter", progressCounter.getScore());
        editor.putInt("current question", progressCounter.getCurrentQuestion());
        editor.putInt("questions taken", progressCounter.getQuestionsTaken());
        editor.putInt("highScore", progressCounter.getHighScore());
        editor.putInt("questions on highScore", progressCounter.getHighScoreQuestionsTaken());
        editor.apply();

        Toast.makeText(MainActivity.this, "Game saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveGame();
    }
}
