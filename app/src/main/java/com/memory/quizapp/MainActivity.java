package com.memory.quizapp;

import android.database.Cursor;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    byte[] jsonByte;
    InputStream jsonInputStream;
    JSONObject jsonObject;
    JSONArray jsonQuizArray;
    String jsonFileString,settingsCategoryProperty = "Category",
            settingsVersionProperty = "Version";
    QuizSQLiteDBHelper quizSQLiteDBHelper;
    ArrayList<String> categoryList = new ArrayList<>();
    Cursor settingsCursor;
    RecyclerView categoryrecyclerview;
    QuizRecyclerViewAdapter quizRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryrecyclerview = findViewById(R.id.categoryrecyclerview);

        quizSQLiteDBHelper = new QuizSQLiteDBHelper(MainActivity.this);
        if (quizSQLiteDBHelper.readAllQuestionsFromDB().getCount()==0)
            readJSONFile();

        categoryList.clear();
        settingsCursor = quizSQLiteDBHelper.readSettingsValueFromDB(settingsCategoryProperty);
        if (settingsCursor!=null){
            settingsCursor.moveToPosition(-1);
            while (settingsCursor.moveToNext())
                if (!categoryList.contains(settingsCursor.getString(0)))
                    categoryList.add(settingsCursor.getString(0));
        }

        categoryrecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        quizRecyclerViewAdapter = new QuizRecyclerViewAdapter(MainActivity.this, categoryList);
        categoryrecyclerview.setAdapter(quizRecyclerViewAdapter);
    }

    public void readJSONFile(){
        try {
            quizSQLiteDBHelper.deleteDBTableData();
            jsonInputStream = this.getAssets().open("QuestionsJSON.json");
            jsonByte = new byte[jsonInputStream.available()];
            jsonInputStream.read(jsonByte);
            jsonFileString = new String(jsonByte);
            jsonObject = new JSONObject(jsonFileString);
            quizSQLiteDBHelper.addSettingsToDB(settingsVersionProperty,jsonObject.getString("version"));
            jsonQuizArray = jsonObject.getJSONArray("quiz");
            for(int i=0;i<jsonQuizArray.length();i++){
                jsonObject = jsonQuizArray.getJSONObject(i);
                quizSQLiteDBHelper.addQuestionsToDB(jsonObject.getString("category"),jsonObject.getString("question"),
                        jsonObject.getString("option_1"),jsonObject.getString("option_2"),jsonObject.getString("option_3"),
                        jsonObject.getString("option_4"),jsonObject.getString("answer"));
                categoryList.add(jsonObject.getString("category"));
            }
            categoryList.add("Random");
            for (String category: categoryList){
                quizSQLiteDBHelper.addSettingsToDB(settingsCategoryProperty,category);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}