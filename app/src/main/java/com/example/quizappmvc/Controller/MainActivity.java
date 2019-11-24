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

    private Button falseButton;
    private Button trueButton;
    private Button nextButton;
    private TextView answerTextView;

    private Question[] question = new Question[]{
            new Question(R.string.question_1, false),
            new Question(R.string.question_2, false),
            new Question(R.string.question_3, true),
            new Question(R.string.question_4, true),
            new Question(R.string.question_5, false),
            new Question(R.string.question_6, true)
    };
    private int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        nextButton = findViewById(R.id.next_button);
        answerTextView = findViewById(R.id.answer_text_view);



        //registering button to listen for a click
        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.false_button:
                // when answer is wrong
                Toast.makeText(MainActivity.this, "False", Toast.LENGTH_SHORT).show();
                break;

            case R.id.true_button:
                //when answer is correct
                Toast.makeText(MainActivity.this, "True", Toast.LENGTH_SHORT).show();
                break;

            case R.id.next_button:
                //go to the next question
                Toast.makeText(MainActivity.this, "Next", Toast.LENGTH_SHORT).show();
                currentQuestion++;
                //question[currentQuestion].setAnswerResourceId(currentQuestion);
                answerTextView.setText(question[currentQuestion].getAnswerResourceId());
                break;
        }
    }
}
