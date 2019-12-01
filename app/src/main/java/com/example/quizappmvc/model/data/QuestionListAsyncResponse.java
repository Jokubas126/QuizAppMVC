package com.example.quizappmvc.model.data;

import com.example.quizappmvc.model.Question;

import java.util.ArrayList;

public interface QuestionListAsyncResponse{
    void processFinished(ArrayList<Question> questionArrayList);
}
