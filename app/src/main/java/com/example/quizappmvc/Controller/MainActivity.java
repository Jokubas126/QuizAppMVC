package com.example.quizappmvc.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizappmvc.Model.Question;
import com.example.quizappmvc.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // buttons in the app
    private Button falseButton;
    private Button trueButton;
    private Button nextButton;
    private Button tryAgainButton;

    //text views used
    private TextView answerTextView;
    private TextView allAnswerScore;

    //questions with their correct answers
    private Question[] question = new Question[]{
            new Question(R.string.question_1, false),
            new Question(R.string.question_2, false),
            new Question(R.string.question_3, true),
            new Question(R.string.question_4, true),
            new Question(R.string.question_5, false),
            new Question(R.string.question_6, true)
    };

    private int currentQuestion = 0; //question counter
    private int score = 0; //score of the player
    private String scoreText = "0/" + question.length; //initial score text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        nextButton = findViewById(R.id.next_button);
        tryAgainButton = findViewById(R.id.try_again_button);
        answerTextView = findViewById(R.id.answer_text_view);
        allAnswerScore = findViewById(R.id.score_text_view);

        //registering button to listen for a click
        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        tryAgainButton.setOnClickListener(this);

        scoreText = score + "/" + question.length;
        allAnswerScore.setText(scoreText);
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

    private void nextQuestion(){
        currentQuestion++;
        if (currentQuestion < question.length){
            answerTextView.setText(question[currentQuestion].getAnswerResourceId());
        } else{
            Toast.makeText(MainActivity.this, "This was the last question", Toast.LENGTH_SHORT).show();


        }
        //currentQuestion = (currentQuestion + 1) % question.length; // for looping through the questions over and over

    }

    void checkAnswer(boolean choice){
        boolean correctAnswer = question[currentQuestion].isAnswer();
        if (correctAnswer == choice){
            Toast.makeText(MainActivity.this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
            score++;
            scoreText = score + "/" + question.length;
            allAnswerScore.setText(scoreText);
            nextQuestion();
        } else {
            Toast.makeText(MainActivity.this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
            nextQuestion();
        }
    }

    void restartQuiz(){
        score = 0;
        scoreText = score + "/" + question.length;
        allAnswerScore.setText(scoreText);
        currentQuestion = 0;
        answerTextView.setText(question[currentQuestion].getAnswerResourceId());
    }
}
