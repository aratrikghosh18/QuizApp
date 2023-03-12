package com.memory.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView scoretextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoretextview =  findViewById(R.id.scoretextview);

        scoretextview.setText("Final Score : "+getIntent().getIntExtra("finalScore",0));
    }
}