package com.memory.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    TextView selectedcategorytextview,questiontextview,option1textview,option2textview,
            option3textview,option4textview,selectedOptionTextView,quizscoretextview;
    String selectedCategory,correctAnswer;
    QuizSQLiteDBHelper quizSQLiteDBHelper;
    Cursor questionCursor,optionCursor;
    ArrayList<String> questionsList = new ArrayList<>();
    ArrayList<String> optionsList = new ArrayList<>();
    int questionPosition = 0,quizScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        selectedcategorytextview = findViewById(R.id.selectedcategorytextview);
        questiontextview = findViewById(R.id.questiontextview);
        option1textview = findViewById(R.id.option1textview);
        option2textview = findViewById(R.id.option2textview);
        option3textview = findViewById(R.id.option3textview);
        option4textview = findViewById(R.id.option4textview);
        quizscoretextview = findViewById(R.id.quizscoretextview);

        quizSQLiteDBHelper = new QuizSQLiteDBHelper(QuizActivity.this);

        selectedCategory = getIntent().getExtras().get("selectedcategory").toString();
        selectedcategorytextview.setText(selectedCategory);

        if (selectedCategory.equals("Random"))
            questionCursor = quizSQLiteDBHelper.readAllQuestionsFromDB();
        else
            questionCursor = quizSQLiteDBHelper.readCategoryQuestionsFromDB(selectedCategory);
        if (questionCursor!=null){
            questionCursor.moveToPosition(-1);
            while (questionCursor.moveToNext()){
                questionsList.add(questionCursor.getString(1));
            }
        }
        Collections.shuffle(questionsList);
        setQuestionDetails(questionPosition);
    }

    public void optionClicked(View view){
        selectedOptionTextView = (TextView) view;
        if (selectedOptionTextView.getText().equals(correctAnswer)){
            selectedOptionTextView.setBackgroundColor(Color.GREEN);
            quizScore+=10;
            quizscoretextview.setText(""+quizScore);
        }else {
            selectedOptionTextView.setBackgroundColor(Color.RED);
            if (quizScore>=5){
                quizScore-=5;
                quizscoretextview.setText(""+quizScore);
            }
        }
        questionPosition++;
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (questionsList.size()==questionPosition){
                Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                intent.putExtra("finalScore",quizScore);
                startActivity(intent);
                QuizActivity.this.finish();
            }else {
                setQuestionDetails(questionPosition);
            }
        },2000);
    }

    public void setQuestionDetails(int position){
        optionsList.clear();
        setOptionBackground();
        questiontextview.setText(questionsList.get(position));
        optionCursor = quizSQLiteDBHelper.readOptionsFromDB(questionsList.get(position));
        optionCursor.moveToPosition(0);
        for (int i=2;i<=5;i++){
            optionsList.add(optionCursor.getString(i));
        }
        Collections.shuffle(optionsList);
        option1textview.setText(optionsList.get(0));
        option2textview.setText(optionsList.get(1));
        option3textview.setText(optionsList.get(2));
        option4textview.setText(optionsList.get(3));
        correctAnswer = optionCursor.getString(6);
    }

    public void setOptionBackground(){
        option1textview.setBackgroundColor(Color.WHITE);
        option2textview.setBackgroundColor(Color.WHITE);
        option3textview.setBackgroundColor(Color.WHITE);
        option4textview.setBackgroundColor(Color.WHITE);
    }
}