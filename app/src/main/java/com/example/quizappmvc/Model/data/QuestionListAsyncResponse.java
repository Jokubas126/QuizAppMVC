package com.example.quizappmvc.Model.data;

import com.example.quizappmvc.Model.Question;

import java.util.ArrayList;

public interface QuestionListAsyncResponse{
    void processFinished(ArrayList<Question> questionArrayList);
}
