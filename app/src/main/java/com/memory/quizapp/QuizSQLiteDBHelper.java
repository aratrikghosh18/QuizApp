package com.memory.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class QuizSQLiteDBHelper extends SQLiteOpenHelper {

    //Database Name & Version
    public static final String QUIZ_DB_Name = "Quiz_Database.sqlite";
    public static final int QUIZ_DB_Version = 1;

    //QUIZ_TABLE Details
    public static final String QUIZ_TABLE_NAME = "Quiz_Table";
    public static final String QUIZ_CATEGORY = "Quiz_Category";
    public static final String QUIZ_QUESTION = "Quiz_Question";
    public static final String QUIZ_OPTION_1 = "Quiz_Option_1";
    public static final String QUIZ_OPTION_2 = "Quiz_Option_2";
    public static final String QUIZ_OPTION_3 = "Quiz_Option_3";
    public static final String QUIZ_OPTION_4 = "Quiz_Option_4";
    public static final String QUIZ_ANSWER = "Quiz_Answer";

    //SETTINGS_TABLE Details
    public static final String SETTINGS_TABLE_NAME = "Settings_Table";
    public static final String SETTINGS_PROPERTY = "Settings_Property";
    public static final String SETTINGS_VALUE = "Settings_Value";

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;

    public QuizSQLiteDBHelper(@Nullable Context context) {
        super(context, QUIZ_DB_Name, null, QUIZ_DB_Version);
        sqLiteDatabase = this.getWritableDatabase();
        contentValues = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + QUIZ_TABLE_NAME + " ("
                + QUIZ_CATEGORY + " TEXT,"
                + QUIZ_QUESTION + " TEXT,"
                + QUIZ_OPTION_1 + " TEXT,"
                + QUIZ_OPTION_2 + " TEXT,"
                + QUIZ_OPTION_3 + " TEXT,"
                + QUIZ_OPTION_4 + " TEXT,"
                + QUIZ_ANSWER + " TEXT)";
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + SETTINGS_TABLE_NAME + " ("
                + SETTINGS_PROPERTY + " TEXT,"
                + SETTINGS_VALUE + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_QUIZ_TABLE);
        sqLiteDatabase.execSQL(CREATE_SETTINGS_TABLE);
    }

    //QUIZ_TABLE Methods
    public void addQuestionsToDB(String quizCategory,String quizQuestion,String quizOption1,String quizOption2,
                                   String quizOption3,String quizOption4,String quizAnswer){
        contentValues.clear();
        contentValues.put(QUIZ_CATEGORY,quizCategory);
        contentValues.put(QUIZ_QUESTION,quizQuestion);
        contentValues.put(QUIZ_OPTION_1,quizOption1);
        contentValues.put(QUIZ_OPTION_2,quizOption2);
        contentValues.put(QUIZ_OPTION_3,quizOption3);
        contentValues.put(QUIZ_OPTION_4,quizOption4);
        contentValues.put(QUIZ_ANSWER,quizAnswer);
        sqLiteDatabase.insert(QUIZ_TABLE_NAME,null,contentValues);
    }

    public Cursor readCategoryQuestionsFromDB(String quizCategory){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + QUIZ_TABLE_NAME + " WHERE " + QUIZ_CATEGORY + " = ?",
                new String[]{quizCategory});
    }

    public Cursor readOptionsFromDB(String question){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + QUIZ_TABLE_NAME + " WHERE " + QUIZ_QUESTION + " = ?",
                new String[]{question});
    }

    public Cursor readAllQuestionsFromDB(){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + QUIZ_TABLE_NAME , null);
    }

    //SETTINGS_TABLE Methods
    public void addSettingsToDB(String settingsProperty,String settingsValue){
        contentValues.clear();
        contentValues.put(SETTINGS_PROPERTY,settingsProperty);
        contentValues.put(SETTINGS_VALUE,settingsValue);
        sqLiteDatabase.insert(SETTINGS_TABLE_NAME,null,contentValues);
    }

    public Cursor readSettingsValueFromDB(String settingsProperty){
        return sqLiteDatabase.rawQuery("SELECT " + SETTINGS_VALUE + " FROM " + SETTINGS_TABLE_NAME + " WHERE "
                        + SETTINGS_PROPERTY + " = ?", new String[]{settingsProperty});
    }

    //CommonMethods
    public void deleteDBTableData(){
        sqLiteDatabase.execSQL("DELETE FROM " + QUIZ_TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + SETTINGS_TABLE_NAME);
        sqLiteDatabase.execSQL("VACUUM");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUIZ_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE_NAME);
        sqLiteDatabase.execSQL("VACUUM");
        onCreate(sqLiteDatabase);
    }
}
