package com.example.quizappmvc.controller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
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
import com.example.quizappmvc.model.Question;
import com.example.quizappmvc.model.data.*;
import com.example.quizappmvc.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_ID = "previous score";

    // buttons in the app
    private Button falseButton;
    private Button trueButton;
    private Button skipButton;
    private Button previousGameButton;
    private Button saveButton;
    private ImageButton restartButton;

    //text views used
    private TextView questionTextView;
    private TextView allAnswerScore;

    private int currentQuestion = 0; //question counter
    private int score = 0; //score of the player
    private int questionsTaken = 0; // questions that were already asked
    private String scoreText = ""; //score text

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
                question = questionHolder.getCurrentQuestion();
                questionTextView.setText(question.getQuestionText());
            }
        });
        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        skipButton = findViewById(R.id.skip_button);
        restartButton = findViewById(R.id.restart_button);
        previousGameButton = findViewById(R.id.saved_games_button);
        saveButton = findViewById(R.id.save_button);
        questionTextView = findViewById(R.id.question_text_view);
        allAnswerScore = findViewById(R.id.score_text_view);

        //registering button to listen for a click
        falseButton.setOnClickListener(MainActivity.this);
        trueButton.setOnClickListener(MainActivity.this);
        skipButton.setOnClickListener(MainActivity.this);
        restartButton.setOnClickListener(MainActivity.this);
        previousGameButton.setOnClickListener(MainActivity.this);
        saveButton.setOnClickListener(MainActivity.this);

        scoreText = score + "/" + questionsTaken;
        allAnswerScore.setText(scoreText);
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

            case R.id.saved_games_button:
                loadPreviousGame();
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
            score++;
            scoreText = score + "/" + questionsTaken;
            allAnswerScore.setText(scoreText);
            nextQuestion();
        } else {
            shakeAnimation();
            nextQuestion();
        }
    }

    private void nextQuestion(){
        currentQuestion++;
        questionsTaken++;
        if (currentQuestion < questionHolder.getQuestionListSize()){
            question = questionHolder.getNextQuestion();
            updateInformation();
        } else{
            Toast.makeText(MainActivity.this, "This was the last question", Toast.LENGTH_SHORT).show();
        }
    }

    // needs to redo it later to change values using setters in QuestionHolder class
    void restartQuiz(){
        score = 0;
        questionsTaken = 0;
        questionHolder.setCurrentQuestion(0);
        updateInformation();
    }

    void updateInformation(){
        scoreText = score + "/" + questionsTaken;
        allAnswerScore.setText(scoreText);
        questionHolder.setCurrentQuestion(currentQuestion);
        question = questionHolder.getCurrentQuestion();
        questionTextView.setText(question.getQuestionText());
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

    private void loadPreviousGame(){
        SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID , MODE_PRIVATE);
        score = getSharedData.getInt("score", 0);
        questionsTaken = getSharedData.getInt("questions taken", 0);
        currentQuestion = getSharedData.getInt("current question", 0);
        updateInformation();
        Toast.makeText(MainActivity.this, "Game loaded", Toast.LENGTH_SHORT).show();
    }

    private void saveGame(){
        SharedPreferences preferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("score", score);
        editor.putInt("current question", currentQuestion);
        editor.putInt("questions taken", questionsTaken);
        editor.apply();

        Toast.makeText(MainActivity.this, "Game saved", Toast.LENGTH_SHORT).show();
    }
}
