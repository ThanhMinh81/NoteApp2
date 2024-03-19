package com.example.notepad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private DatabaseHandler dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHandler(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<Note> search(String keyWord) {

        ArrayList<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID + ","
                + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_TITLE + "," +
                dbHelper.TABLE_NOTE + "." + dbHelper.KEY_CONTENT + " FROM " + dbHelper.TABLE_NOTE +
                " WHERE " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_TITLE + " LIKE ? OR " +
                dbHelper.TABLE_NOTE + "." + dbHelper.KEY_CONTENT + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyWord + "%", "%" + keyWord + "%"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setIdNote(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTimeEdit(cursor.getString(3));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return noteList;

    }

    // add note

    public void addNote(Note note) {

        // neu id category bang null
        // thi khong them no vao table category
        // cai nay khong can thiet phai them vaoo category

        if(note.getIdCategory().equals("null"))
        {
            // them du lieu vao note
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(dbHelper.KEY_TITLE, note.getTitle());
            values.put(dbHelper.KEY_CONTENT, note.getContent());
            values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());

            // cai nay la background cho tat ca layout update ,add gi do

            values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());

            // tra ve -1 neu insert khong thanh cong
            // neu thành công trả về last id

            long result = db.insert(dbHelper.TABLE_NOTE, null, values);

            Log.d("insert",result + " ");

            if (result == -1) {

            } else {
                // them thanh cong va add vao bang style

                try {

                    SQLiteDatabase insertNoteStyle = dbHelper.getWritableDatabase();
                    ContentValues noteStyle = new ContentValues();
                    noteStyle.put(dbHelper.KEY_ID_NOTE, Integer.parseInt(String.valueOf(result)));
                    noteStyle.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
                    noteStyle.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
                    noteStyle.put(dbHelper.KEY_BOLD, note.getStyleBold());
                    noteStyle.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
                    noteStyle.put(dbHelper.KEY_BACKGROUND_TEXT,note.getBackgroundColorText());
                    noteStyle.put(dbHelper.KEY_STRIKER,note.getStrike());
                    long result2 = insertNoteStyle.insert(dbHelper.TABLE_STYLE, null, noteStyle);

                    Log.d("insertStyle",result2 + " ");


                } catch (Exception e) {}

            }
            db.close();
        }else {



            // them du lieu vao note
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(dbHelper.KEY_TITLE, note.getTitle());
            values.put(dbHelper.KEY_CONTENT, note.getContent());
            values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());

            // cai nay la background cho tat ca layout update ,add gi do

            values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());
            values.put(dbHelper.KEY_ID_CATEGORY,note.getIdCategory());

            // tra ve -1 neu insert khong thanh cong
            // neu thành công trả về last id

            long result = db.insert(dbHelper.TABLE_NOTE, null, values);


            if (result == -1) {

            } else {
                // them thanh cong va add vao bang style

                try {

                    SQLiteDatabase insertNoteStyle = dbHelper.getWritableDatabase();
                    ContentValues noteStyle = new ContentValues();
                    noteStyle.put(dbHelper.KEY_ID_NOTE, Integer.parseInt(String.valueOf(result)));
                    noteStyle.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
                    noteStyle.put(dbHelper.KEY_BOLD, note.getStyleBold());
                    noteStyle.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
                    noteStyle.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
                    long result2 = insertNoteStyle.insert(dbHelper.TABLE_STYLE, null, noteStyle);

                } catch (Exception e) {}

            }
            db.close();


        }


    }

    public void addNote2(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());

        long result = db.insert(dbHelper.TABLE_NOTE, null, values);

        if (result != -1) {
            int idNote = (int) result; // Lấy idNote vừa thêm

            // Thêm các bản ghi vào bảng join cho mỗi danh mục của ghi chú
            for (Category category : note.getCategories()) {
                ContentValues joinValues = new ContentValues();
                joinValues.put(dbHelper.KEY_ID_NOTE, idNote);
                joinValues.put(dbHelper.KEY_ID_CATEGORY, Integer.parseInt(category.getIdCategory()));
                long joinResult = db.insert(dbHelper.TABLE_JOIN, null, joinValues);
                Log.d("insertJoin", joinResult + " ");
            }

            // Thêm bản ghi vào bảng style
            ContentValues noteStyle = new ContentValues();
            noteStyle.put(dbHelper.KEY_ID_NOTE, idNote);
            noteStyle.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
            noteStyle.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
            noteStyle.put(dbHelper.KEY_BOLD, note.getStyleBold());
            noteStyle.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
            noteStyle.put(dbHelper.KEY_BACKGROUND_TEXT, note.getBackgroundColorText());
            noteStyle.put(dbHelper.KEY_STRIKER, note.getStrike());
            long styleResult = db.insert(dbHelper.TABLE_STYLE, null, noteStyle);
            Log.d("insertStyle", styleResult + " ");
        }

        db.close();
    }


    public int updateNote(@NonNull Note note) {
        int update = -1;

        if (note.getIdCategory().equals("null")) {
            // update khi nguoi dung khong co id category
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(dbHelper.KEY_TITLE, note.getTitle());
            values.put(dbHelper.KEY_CONTENT, note.getContent());
            values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
            values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());

            int resultUpdateNote = db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?",
                    new String[]{String.valueOf(note.getIdNote())});

            // update vao database no da la true roi

//           Log.d("update", note.getStyleBold() + "");
            if (resultUpdateNote != -1) {

//             if(note.getIdNoteStyle() != 0)
//             {
                // update style

                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                ContentValues values1 = new ContentValues();

                // truyen du cac truong du leu
                // bo qua truong autoincrement tu dong tang

                values1.put(dbHelper.KEY_ID_NOTE, note.getIdNote());
                values1.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
                values1.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
                values1.put(dbHelper.KEY_BOLD, note.getStyleBold());
                values1.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
                values1.put(dbHelper.KEY_BACKGROUND_TEXT, note.getBackgroundColorText());
                values1.put(dbHelper.KEY_STRIKER, note.getStrike());

                update = db2.update(dbHelper.TABLE_STYLE, values1, dbHelper.KEY_ID_NOTE + " = ?",
                        new String[]{String.valueOf(note.getIdNote())});

                Log.d("dfasfas", update + "");


            }

        }
        else if (!note.getIdCategory().equals("null")) {
            // update khi nguoi dung khong co id category
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(dbHelper.KEY_TITLE, note.getTitle());
            values.put(dbHelper.KEY_CONTENT, note.getContent());
            values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
            values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());
            values.put(dbHelper.KEY_IDCATEGORY, Integer.parseInt(note.getIdCategory()));

            int resultUpdateNote = db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?",
                    new String[]{String.valueOf(note.getIdNote())});

            // update vao database no da la true roi

//           Log.d("update", note.getStyleBold() + "");
            if (resultUpdateNote != -1) {

//             if(note.getIdNoteStyle() != 0)
//             {
                // update style

                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                ContentValues values1 = new ContentValues();

                // truyen du cac truong du leu
                // bo qua truong autoincrement tu dong tang

                values1.put(dbHelper.KEY_ID_NOTE, note.getIdNote());
                values1.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
                values1.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
                values1.put(dbHelper.KEY_BOLD, note.getStyleBold());
                values1.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
                values1.put(dbHelper.KEY_BACKGROUND_TEXT, note.getBackgroundColorText());
                values1.put(dbHelper.KEY_STRIKER, note.getStrike());

                update = db2.update(dbHelper.TABLE_STYLE, values1, dbHelper.KEY_ID_NOTE + " = ?",
                        new String[]{String.valueOf(note.getIdNote())});

                Log.d("dfasfas", update + "");
            }

        }
        return update;
    }

    public void updateNote2(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());

        // Cập nhật bảng note
        int result = db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?", new String[]{String.valueOf(note.getIdNote())});
        if (result > 0) {
            // Cập nhật bảng tbl_style
            ContentValues styleValues = new ContentValues();
            styleValues.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
            styleValues.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
            styleValues.put(dbHelper.KEY_BOLD, note.getStyleBold());
            styleValues.put(dbHelper.KEY_UNDERLINE, note.getStyleUnderline());
            styleValues.put(dbHelper.KEY_BACKGROUND_TEXT, note.getBackgroundColorText());
            styleValues.put(dbHelper.KEY_STRIKER, note.getStrike());
            int styleResult = db.update(dbHelper.TABLE_STYLE, styleValues, dbHelper.KEY_ID_NOTE + " = ?", new String[]{String.valueOf(note.getIdNoteStyle())});

            // Cập nhật bảng tbl_join
            // Trước tiên, xóa tất cả các liên kết của note này
            db.delete(dbHelper.TABLE_JOIN, dbHelper.KEY_ID_NOTE + " = ?", new String[]{String.valueOf(note.getIdNote())});
            // Sau đó, thêm các liên kết mới
            for (Category category : note.getCategories()) {
                ContentValues joinValues = new ContentValues();
                joinValues.put(dbHelper.KEY_ID_NOTE, note.getIdNote());
                joinValues.put(dbHelper.KEY_ID_CATEGORY, category.getIdCategory());
                db.insert(dbHelper.TABLE_JOIN, null, joinValues);
            }
        }
        db.close();
    }



    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE + " INNER JOIN " + dbHelper.TABLE_STYLE + " ON "
                + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID + " = " + dbHelper.TABLE_STYLE + "." + dbHelper.KEY_ID_NOTE;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {

                do {
                    Note note = new Note();
                    note.setIdNote(Integer.parseInt(cursor.getString(0)));
                    note.setTitle(cursor.getString(1));
                    note.setContent(cursor.getString(2));
                    note.setTimeEdit(cursor.getString(3));
                    note.setBgColors(cursor.getString(4));

                    note.setIdNoteStyle(Integer.parseInt(cursor.getString(5)));

                    note.setStyleTextColor(cursor.getString(7));

                    note.setStyleItalic(cursor.getString(8));
                    note.setStyleBold(cursor.getString(9));
                    note.setStyleUnderline(cursor.getString(10));
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Log.d("fsfas", e.toString());

        }

        return noteList;
    }

    public ArrayList<Note> getAllNoteCategory() {
        ArrayList<Note> noteList = new ArrayList<>();

        try {
//            String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE + " FULL JOIN " + dbHelper.TABLE_STYLE + " ON "
//                    + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID + " = " + dbHelper.TABLE_STYLE + "." + dbHelper.KEY_ID_NOTE
//                    + " FULL  JOIN  " + dbHelper.TABLE_CATEGORY + " ON " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID_CATEGORY
//                    + " = " + dbHelper.TABLE_CATEGORY + "." + dbHelper.KEY_ID_CATEGORY;
            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NOTE
                    + " LEFT JOIN " + dbHelper.TABLE_STYLE
                    + " ON " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID
                    + " = " + dbHelper.TABLE_STYLE + "." + dbHelper.KEY_ID_NOTE
                    + " LEFT JOIN " + dbHelper.TABLE_CATEGORY
                    + " ON " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID_CATEGORY
                    + " = " + dbHelper.TABLE_CATEGORY + "." + dbHelper.KEY_ID_CATEGORY;


            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {

                do {
                    Note note = new Note();
                    note.setIdNote(Integer.parseInt(cursor.getString(0)));
                    note.setTitle(cursor.getString(1));
                    note.setContent(cursor.getString(2));
                    note.setTimeEdit(cursor.getString(3));
                    note.setBgColors(cursor.getString(4));

                    // lay id category
                    if (cursor.getString(5) == null) {
                        note.setIdCategory("-1");
                    } else {
                        note.setIdCategory(cursor.getString(5));
                    }

                    if(cursor.getString(6) == null)
                    {
                       note.setIdNoteStyle(-1);

                    }
                    else {
                    note.setIdNoteStyle(Integer.parseInt(cursor.getString(6)));
                    }


                    if (cursor.getString(8) == null) {
                        note.setStyleTextColor("#1B1A18");
                    } else {
                        note.setStyleTextColor(cursor.getString(8));

                    }

                    if (cursor.getString(9) == null) {
                        note.setStyleItalic("false");
                    } else {
                        note.setStyleItalic(cursor.getString(9));

                    }


                    if (cursor.getString(10) == null) {
                        note.setStyleBold("false");
                    } else {
                        note.setStyleBold(cursor.getString(10));
                    }


                    if (cursor.getString(11) == null) {
                        note.setStyleUnderline("false");
                    } else {
                        note.setStyleUnderline(cursor.getString(11));
                    }

                    if (cursor.getString(12) == null) {
                        note.setBackgroundColorText("-1");
                    } else {
                        note.setBackgroundColorText(cursor.getString(12));
                    }

                    if (cursor.getString(13) == null) {
                        note.setStrike("false");
                    } else {
                        note.setStrike(cursor.getString(13));
                    }
                    noteList.add(note);

                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            Log.d("looii", e.toString());

        }

        return noteList;


    }


    public ArrayList<Note> getAllNoteCategory2() {
        ArrayList<Note> noteList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NOTE
                    + " LEFT JOIN " + dbHelper.TABLE_STYLE
                    + " ON " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID
                    + " = " + dbHelper.TABLE_STYLE + "." + dbHelper.KEY_ID_NOTE;

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setIdNote(Integer.parseInt(cursor.getString(0)));
                    note.setTitle(cursor.getString(1));
                    note.setContent(cursor.getString(2));
                    note.setTimeEdit(cursor.getString(3));
                    note.setBgColors(cursor.getString(4));

                    // Set style
                    note.setIdNoteStyle(Integer.parseInt(cursor.getString(5)));
                    note.setStyleTextColor(cursor.getString(7));
                    note.setStyleItalic(cursor.getString(8));
                    note.setStyleBold(cursor.getString(9));
                    note.setStyleUnderline(cursor.getString(10));
                    note.setBackgroundColorText(cursor.getString(11));
                    note.setStrike(cursor.getString(12));

                    // Thêm danh mục cho ghi chú
                    ArrayList<Category> categories = new ArrayList<>();
                    String innerQuery = "SELECT * FROM " + dbHelper.TABLE_JOIN
                            + " LEFT JOIN " + dbHelper.TABLE_CATEGORY
                            + " ON " + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_CATEGORY
                            + " = " + dbHelper.TABLE_CATEGORY + "." + dbHelper.KEY_ID_CATEGORY
                            + " WHERE " + dbHelper.KEY_ID_NOTE + " = " + cursor.getInt(0);
                    Cursor innerCursor = db.rawQuery(innerQuery, null);
                    if (innerCursor.moveToFirst()) {
                        do {
                            Category category = new Category();
                            category.setIdCategory(String.valueOf(innerCursor.getString(2)));
                            category.setNameCategory(String.valueOf(innerCursor.getString(3)));
                            categories.add(category);
                        } while (innerCursor.moveToNext());
                    }
                    innerCursor.close();

                    // Gán danh mục cho ghi chú
                    note.setCategories(categories);

                    noteList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("looii", e.toString());
        }

        return noteList;
    }



    public ArrayList<Category> getAllCategory() {
        ArrayList<Category> categories = new ArrayList<>();

        String getCategory = "SELECT  * FROM " + dbHelper.TABLE_CATEGORY;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {

            Cursor cursor = db.rawQuery(getCategory, null);

            if (cursor.moveToFirst()) {

                do {
                    Category category = new Category();
                    category.setIdCategory(String.valueOf(cursor.getString(0)));
                    category.setNameCategory(String.valueOf(cursor.getString(1)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
        }
        Log.d("fosdfasfa",categories.size() + " ") ;
        return categories;
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.NAME_CATEGORY, category.getNameCategory());

        // tra ve -1 neu insert khong thanh cong
        // neu thành công trả về last id

        long result = db.insert(dbHelper.TABLE_CATEGORY, null, values);

        if (result != -1) {
            Log.d("Them thanh cong", "fasfdsad");
        }
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE_CATEGORY, dbHelper.KEY_ID_CATEGORY + " = ?",
                new String[]{String.valueOf(category.getIdCategory())});
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE_NOTE, dbHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(note.getIdNote())});
        db.close();
    }


    public void deleteNode(ArrayList<String> strings) {
        String args = TextUtils.join(", ", strings);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM note WHERE idNote IN (%s);", args));
    }


    public int updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.NAME_CATEGORY, category.getNameCategory());
        int resultUpdateNote = db.update(dbHelper.TABLE_CATEGORY, values, dbHelper.KEY_IDCATEGORY + " = ?",
                new String[]{String.valueOf(category.getIdCategory())});
        return resultUpdateNote;
    }



    // cap nhat category cho 1 note
    public void updateCategoryNote(Note note, String idCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_IDCATEGORY, idCategory);
        String whereClause = dbHelper.KEY_ID_NOTE + " = ?";
        String[] whereArgs = {String.valueOf(note.getIdNote())};
        db.update(dbHelper.TABLE_NOTE, values, whereClause, whereArgs);
    }

    public void deleteCategoryNote(Note note, String idCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_IDCATEGORY, "-1"); // Thay đổi trường cần cập nhật

        // Xác định điều kiện WHERE để chỉ cập nhật cho một hàng cụ thể
        String whereClause = dbHelper.KEY_ID_NOTE + " = ?";
        String[] whereArgs = {String.valueOf(note.getIdNote())}; // Điều kiện WHERE

        // Thực hiện câu lệnh cập nhật
        int rowsAffected = db.update(dbHelper.TABLE_NOTE, values, whereClause, whereArgs);

        // Kiểm tra xem có bao nhiêu hàng đã được cập nhật
        if (rowsAffected > 0) {
            // Cập nhật thành công
            Log.d("ereg", "430t30u");
        } else {
            // Không có hàng nào được cập nhật (ID không tồn tại)
        }

        db.close(); // Đóng kết nối với cơ sở dữ liệu
    }

    public ArrayList<Note> getListNote(String idCategory) {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE
                + " LEFT JOIN " + dbHelper.TABLE_STYLE + " ON "
                + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID + " = " + dbHelper.TABLE_STYLE + "." + dbHelper.KEY_ID_NOTE
                + " LEFT JOIN " + dbHelper.TABLE_JOIN + " ON "
                + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_ID + " = " + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_NOTE
                + " LEFT JOIN " + dbHelper.TABLE_CATEGORY + " ON "
                + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_CATEGORY + " = " + dbHelper.TABLE_CATEGORY + "." + dbHelper.KEY_ID_CATEGORY
                + " WHERE " + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_CATEGORY + " = ?";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCategory});

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setIdNote(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTimeEdit(cursor.getString(3));
                note.setBgColors(cursor.getString(4));
                note.setIdNoteStyle(Integer.parseInt(cursor.getString(5)));
                note.setStyleTextColor(cursor.getString(7));
                note.setStyleItalic(cursor.getString(8));
                note.setStyleBold(cursor.getString(9));
                note.setStyleUnderline(cursor.getString(10));
                note.setIdCategory(cursor.getString(12));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notes;
    }



    public ArrayList<Category> getCategoriesOfNote(String noteId) {
        ArrayList<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CATEGORY
                + " LEFT JOIN " + dbHelper.TABLE_JOIN + " ON "
                + dbHelper.TABLE_CATEGORY + "." + dbHelper.KEY_ID_CATEGORY + " = " + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_CATEGORY
                + " WHERE " + dbHelper.TABLE_JOIN + "." + dbHelper.KEY_ID_NOTE + " = ?";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{noteId});

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setIdCategory(cursor.getString(0));
                category.setNameCategory(cursor.getString(1));
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categories;
    }


    public void deleteMultipleNotes(ArrayList<String> noteIds) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Duyệt qua danh sách các id của note cần xóa
        for (String noteId : noteIds) {
            // Xóa note từ bảng tbl_note
            db.delete(dbHelper.TABLE_NOTE, dbHelper.KEY_ID + " = ?", new String[]{noteId});

            // Xóa các liên kết của note trong bảng tbl_join
            db.delete(dbHelper.TABLE_JOIN, dbHelper.KEY_ID_NOTE + " = ?", new String[]{noteId});

            // Xóa các style của note trong bảng tbl_style
            db.delete(dbHelper.TABLE_STYLE, dbHelper.KEY_ID_NOTE + " = ?", new String[]{noteId});
        }

        db.close();
    }


    public void updateMultipleNotesBackground(ArrayList<Note> notes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Note note : notes) {
            ContentValues values = new ContentValues();
            values.put(dbHelper.KEY_BACKGROUND, note.getBgColors());

            db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?", new String[]{String.valueOf(note.getIdNote())});
        }

        db.close();
    }

    public void updateNotesCategories(List<Note> notes, List<Category> categories) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("94y3", " " + categories.size());

        try {
            db.beginTransaction();

            // Xóa tất cả các liên kết cũ giữa note và category trong bảng tbl_jointable
            db.delete(dbHelper.TABLE_JOIN, null, null);

            // Duyệt qua danh sách các note
            for (Note note : notes) {

                long noteId = note.getIdNote();

                // Duyệt qua danh sách các category của mỗi note
                for (Category category : categories) {
                    Log.d("94y3",category.getIdCategory() + " " + category.getNameCategory());
                    // Kiểm tra xem note này có thuộc category này hay không
//                    if (note.getCategories().contains(category)) {
                        ContentValues values = new ContentValues();
                        values.put(dbHelper.KEY_ID_NOTE, noteId);
                        values.put(dbHelper.KEY_ID_CATEGORY, category.getIdCategory());

                        // Thêm một bản ghi mới vào bảng tbl_jointable
                    try {
                        long  x =    db.insert(dbHelper.TABLE_JOIN, null, values);

                    }catch (Exception e)
                    {
                        Log.d("94y3",e + " ");
                    }

//                    }
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Error", "Error updating notes categories", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }




}
