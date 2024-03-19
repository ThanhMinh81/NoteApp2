package com.example.notepad.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Adapter.AdapterSelect;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.IClickSelect;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class SelectAllActivity extends AppCompatActivity {

      RecyclerView rcvHome ;
      AdapterSelect adapterSelect;

      ArrayList<Note> noteArrayList ;
      IClickSelect iClickSelect ;
      DBManager databaseHandler ;

     ImageView back;

     TextView tvDeleteNote , tvTitle  ;

    Toolbar toolbarAdd ;

    SharedPreferences sharedPreferences ;

    String themeStyle ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_all);

        toolbarAdd = findViewById(R.id.toolbarAdd); //Ignore red line errors



        noteArrayList=new ArrayList<>();
        noteArrayList =(ArrayList) getIntent().getParcelableArrayListExtra("listData");


        back = this.<ImageView>findViewById(R.id.backAddNote);

        tvDeleteNote = this.<TextView>findViewById(R.id.tvDeleteNote);

        tvTitle = this.<TextView>findViewById(R.id.tvTitle);


        iClickSelect = new IClickSelect() {
            @Override
            public void select(Note note) {

                int indexObj =  noteArrayList.indexOf(note);

              if(note.getCheckSelect())
              {

                  note.setCheckSelect(false);
                  noteArrayList.set(indexObj,note);
                  adapterSelect.notifyDataSetChanged();


              }else {

                  note.setCheckSelect(true);
                  noteArrayList.set(indexObj,note);
                  adapterSelect.notifyDataSetChanged();


              }
            }
        };

        adapterSelect = new AdapterSelect(noteArrayList,this,iClickSelect);

        rcvHome = findViewById(R.id.rcvSelectAll);

        rcvHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rcvHome.setAdapter(adapterSelect);

        adapterSelect.notifyDataSetChanged();

        back.setOnClickListener(v -> {
            finish();
        });


        sharedPreferences = SelectAllActivity.this.getSharedPreferences("MyTheme", Context.MODE_PRIVATE);


        themeStyle = sharedPreferences.getString("theme_system", "Default");

        if(themeStyle.equals("Solarized"))
        {
//          constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));
            toolbarAdd.setBackgroundColor(getResources().getColor(R.color.themeSolari));
            tvDeleteNote.setTextColor(getResources().getColor(R.color.colorTextSolari));
            tvTitle.setTextColor(getResources().getColor(R.color.colorTextSolari));
//          tvSave.setTextColor(getResources().getColor(R.color.colorTextSolari));

        }else  {

             toolbarAdd.setBackgroundColor(getResources().getColor(R.color.appbar));
//           constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari2));
             tvDeleteNote.setTextColor(getResources().getColor(R.color.white));
             tvTitle.setTextColor(getResources().getColor(R.color.white));
//           tvSave.setTextColor(getResources().getColor(R.color.white));

        }


        // click delete

        tvDeleteNote.setOnClickListener(v -> {
            databaseHandler = new DBManager(this);
            databaseHandler.open();
            ArrayList<String> strings = new ArrayList<>();
            for (Note note: noteArrayList)
            {
                if(note.getCheckSelect())
                {
                    strings.add(String.valueOf(note.getIdNote()));
                }
            }
            //delete note duoc pick
            databaseHandler.deleteNode(strings);
            // tra ve mang moi
            Intent intent = new Intent();
            intent.putExtra("delete","success");
            setResult(RESULT_OK,intent);
            Toast.makeText(this, "Xóa thành công !", Toast.LENGTH_SHORT).show();
            finish();
        });
    }


}