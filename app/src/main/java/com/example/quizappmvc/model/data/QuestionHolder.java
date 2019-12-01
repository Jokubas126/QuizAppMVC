package com.example.quizappmvc.model.data;

import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.quizappmvc.controller.ApplicationController;
import com.example.quizappmvc.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionHolder {
    private String URL = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    private ArrayList<Question> questions = new ArrayList<>();
    private int currentQuestion = 0;

    public List<Question> getQuestions(final QuestionListAsyncResponse callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                //Log.d("JSON: ", "onResponse: " + string);
                                Question question = new Question();
                                question.setQuestionText(response.getJSONArray(i).getString(0));
                                question.setCorrectAnswer(response.getJSONArray(i).getBoolean(1));
                                questions.add(question);
                                //Log.d("QUESTION", "onResponse: " + question.toString());
                            } catch (JSONException e) {
                                Log.d("ERROR: ", "onResponse: could not get a question");
                            }
                        }
                        if (callback != null) callback.processFinished(questions);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR RESPONSE", "onErrorResponse: jsonArrayRequest onResponse failed");
            }
        });
        ApplicationController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questions;
    }

    //-------------------------------------------------------------------//
        //-----------------------GETTERS---------------------------//
    //-------------------------------------------------------------------//


    public Question getNextQuestion(){
        currentQuestion++;
        return questions.get(currentQuestion);
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestion);
    }

    public int getQuestionListSize(){
        return questions.size();
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
