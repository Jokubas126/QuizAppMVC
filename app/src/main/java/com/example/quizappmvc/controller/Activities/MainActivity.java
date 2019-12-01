package com.example.quizappmvc.controller.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizappmvc.model.Question;
import com.example.quizappmvc.model.data.QuestionHolder;
import com.example.quizappmvc.model.data.QuestionListAsyncResponse;
import com.example.quizappmvc.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // buttons in the app
    private Button falseButton;
    private Button trueButton;
    private Button nextButton;
    private Button tryAgainButton;

    //text views used
    private TextView answerTextView;
    private TextView allAnswerScore;

    private int currentQuestion = 0; //question counter
    private int score = 0; //score of the player
    private String scoreText = ""; //score text

    private QuestionHolder questionHolder; //all the questions from the database
    private Question question; //current question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionHolder = new QuestionHolder();
        questionHolder.getQuestions(new QuestionListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                question = questionHolder.getCurrentQuestion();
                falseButton = findViewById(R.id.false_button);
                trueButton = findViewById(R.id.true_button);
                nextButton = findViewById(R.id.next_button);
                tryAgainButton = findViewById(R.id.try_again_button);
                answerTextView = findViewById(R.id.answer_text_view);
                allAnswerScore = findViewById(R.id.score_text_view);

                //registering button to listen for a click
                falseButton.setOnClickListener(MainActivity.this);
                trueButton.setOnClickListener(MainActivity.this);
                nextButton.setOnClickListener(MainActivity.this);
                tryAgainButton.setOnClickListener(MainActivity.this);

                scoreText = score + "/" + questionHolder.getQuestionListSize();
                allAnswerScore.setText(scoreText);

                answerTextView.setText(question.getQuestionText());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.false_button:
                // when answer is wrong
                Toast.makeText(MainActivity.this, "False", Toast.LENGTH_SHORT).show();
                checkAnswer(false); //check an answer with the given
                break;

            case R.id.true_button:
                //when answer is correct
                Toast.makeText(MainActivity.this, "True", Toast.LENGTH_SHORT).show();
                checkAnswer(true); //check an answer with the given
                break;

            case R.id.next_button:
                //go to the next question
                nextQuestion();
                break;

            case R.id.try_again_button:
                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                restartQuiz(); //restarts the results
                break;
        }
    }

    void checkAnswer(boolean choice){
        boolean correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == choice){
            Toast.makeText(MainActivity.this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
            score++;
            scoreText = score + "/" + questionHolder.getQuestionListSize();
            allAnswerScore.setText(scoreText);
            nextQuestion();
        } else {
            Toast.makeText(MainActivity.this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
            nextQuestion();
        }
    }

    private void nextQuestion(){
        if (currentQuestion < questionHolder.getQuestionListSize()){
            question = questionHolder.getNextQuestion();
            answerTextView.setText(question.getQuestionText());
        } else{
            Toast.makeText(MainActivity.this, "This was the last question", Toast.LENGTH_SHORT).show();
        }
    }

    // needs to redo it later to change values using setters in QuestionHolder class
    void restartQuiz(){
        score = 0;
        scoreText = score + "/" + questionHolder.getQuestionListSize();
        allAnswerScore.setText(scoreText);
        questionHolder.setCurrentQuestion(0);
        question = questionHolder.getCurrentQuestion();
        answerTextView.setText(question.getQuestionText());
    }
}
