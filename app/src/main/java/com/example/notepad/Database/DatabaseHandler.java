package com.example.notepad.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // table : background color cho tat ca layout
    // style : background color cho text

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "noteManager";
    public static final String TABLE_NOTE = "note";

    public static final String KEY_ID = "idNote";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TIME_EDIT = "timeEdit";
    public static final String KEY_IDCATEGORY = "idCategory";

    public static final String KEY_BACKGROUND_LAYOUT = "bglayout";


    // database version
    static final int DB_VERSION = 1;

    // Database Information
    static final String DB_NAME = "NOTEAPP.DB";

//     CREATE TABLE style
    public static final String TABLE_STYLE = "tbl_style";
    public static final String KEY_ID_STYLE = "idStyle";
    public static final String KEY_ID_NOTE   = "idNote";
    public static final String KEY_ITALIC = "italic";
    public static final String KEY_BOLD = "bold";
    public static final String KEY_UNDERLINE = "underline";

    public static final String KEY_STRIKER = "striker";
    public static final String KEY_BACKGROUND_TEXT = "bgTextStyle";

    public static final String KEY_COLOR = "color";

    public static final String KEY_COLOR_TEXT = "backgroundColorText";

    public static final String KEY_BACKGROUND  = "background" ;

    //CREATE TABLE style

    public static final String TABLE_CATEGORY = "tbl_category";
    public static final String KEY_ID_CATEGORY = "idCategory";
    public static final String NAME_CATEGORY   = "nameCategory";


    // CREATE TABLE JOINTABLE

    public static final String TABLE_JOIN = "tbl_jointable";





    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // tao bang join

        String CREATE_JOIN_TABLE = "CREATE TABLE " + TABLE_JOIN + "("
                + KEY_ID_NOTE + " INTEGER ,"
                + KEY_ID_CATEGORY + " TEXT,"
                + "FOREIGN KEY (" + KEY_ID_NOTE + ") REFERENCES " + TABLE_NOTE + "(" + KEY_ID_NOTE + ") ,"
                + "FOREIGN KEY (" + KEY_ID_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID_CATEGORY + ") "
                + ")";

        sqLiteDatabase.execSQL(CREATE_JOIN_TABLE);



        // tao bang category

        String CREATE_CATEGORY_NOTE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_CATEGORY + " TEXT"
                + ")";


        sqLiteDatabase.execSQL(CREATE_CATEGORY_NOTE);

        // truong hop 1 co category
        // truong hop 2 khong co category

        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_TIME_EDIT + " TEXT,"
                + KEY_BACKGROUND + " TEXT )" ;

        sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);

        // create table format


        // Tạo bảng Style

        String CREATE_TABLE_STYLE = "CREATE TABLE " + TABLE_STYLE + "("
                + KEY_ID_STYLE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_NOTE + " INTEGER,"
                + KEY_COLOR + " TEXT,"
                + KEY_ITALIC + " TEXT,"
                + KEY_BOLD + " TEXT,"
                + KEY_UNDERLINE + " TEXT,"
                + KEY_BACKGROUND_TEXT + " TEXT,"
                + KEY_STRIKER + " TEXT,"
                + "FOREIGN KEY (" + KEY_ID_NOTE + ") REFERENCES " + TABLE_NOTE + "(" + KEY_ID + ")"
                + ")";


        sqLiteDatabase.execSQL(CREATE_TABLE_STYLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(sqLiteDatabase);
    }


}
