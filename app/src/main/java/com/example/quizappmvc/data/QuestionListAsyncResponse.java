package com.example.quizappmvc.data;

import com.example.quizappmvc.Model.Question;

import java.util.ArrayList;

public interface QuestionListAsyncResponse{
    void processFinished(ArrayList<Question> questionArrayList);
}
