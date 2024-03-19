package com.example.notepad.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edTitle, edContent;
    TextView tvSave;
    DBManager databaseHandler;
    ImageView back;

    Boolean checkUpdateNote = true;

    SharedPreferences sharedPreferences ;
    String themeStyle ;



    ConstraintLayout constraintLayout ;

    Toolbar toolbarAdd ;

    TextView tvTitle , tvUndo ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        tvSave = this.<TextView>findViewById(R.id.saveNote);
        back = this.<ImageView>findViewById(R.id.backAddNote);

        tvTitle = this.<TextView>findViewById(R.id.tvTitle);
        tvUndo = this.<TextView>findViewById(R.id.tvUndo);

        toolbarAdd = findViewById(R.id.toolbarAdd);


        constraintLayout = this.<ConstraintLayout>findViewById(R.id.addlayout);

        databaseHandler = new DBManager(this);
        databaseHandler.open();

        checkUpdateNote = getIntent().getExtras().getBoolean("checkUpdate", true);

        sharedPreferences = AddNoteActivity.this.getSharedPreferences("MyTheme", Context.MODE_PRIVATE);


        themeStyle = sharedPreferences.getString("theme_system", "Default");

        if(themeStyle.equals("Solarized"))
        {

            constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));
            toolbarAdd.setBackgroundColor(getResources().getColor(R.color.themeSolari));
            tvUndo.setTextColor(getResources().getColor(R.color.colorTextSolari));
            tvTitle.setTextColor(getResources().getColor(R.color.colorTextSolari));
            tvSave.setTextColor(getResources().getColor(R.color.colorTextSolari));

        }else  {
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari2));
            tvUndo.setTextColor(getResources().getColor(R.color.white));
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            tvSave.setTextColor(getResources().getColor(R.color.white));

        }


        if (!checkUpdateNote) {
            eventClickAdd();
        }

    }

    private void eventClickAdd() {
        back.setOnClickListener(v -> {
            finish();
        });

        tvSave.setOnClickListener(view -> {

            Intent resultIntent = new Intent();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();

            note.setTitle(edTitle.getText().toString());
            note.setContent(edContent.getText().toString());
            note.setTimeEdit(time.toString());
//            note.setBgColors("#836E4C");
            note.setBgColors("#FCFACA");
            note.setStyleTextColor("#1B1A18");
            note.setStyleUnderline("false");
            note.setStyleBold("false");
            note.setStyleItalic("false");
            databaseHandler.addNote(note);
            resultIntent.putExtra("note", note);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        });
    }
}
