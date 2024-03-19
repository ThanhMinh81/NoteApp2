package com.example.notepad.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Adapter.AdapterNote;
import com.example.notepad.Interface.IClickSelect;
import com.example.notepad.Interface.IClickUpdate;
import com.example.notepad.Interface.IStateList;
import com.example.notepad.Model.SelectAll;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.Interface.IData;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView rcvHome;
    private ArrayList<Note> notes;

    public static ArrayList<Note> notesSelect ;
    private AdapterNote apdaterNote;
    IData iData;
    IClickUpdate iClickUpdate;
    DataViewModel dataViewModel;

    ConstraintLayout constraintLayout ;
    SeekBar seekBar;

     Boolean checkUpdate  = false ;

     IStateList iStateList;

    public HomeFragment(IStateList iStateList) {
        this.iStateList = iStateList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);

// mang luu cac phan tu dc select ne
        notesSelect = new ArrayList<>();



        /// xetttt mauuuuu cho bg fragment
        constraintLayout = view.findViewById(R.id.fragmentHomeConstrain);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.backgroundItem));

        /////===

        ArrayList<Note> integers = new ArrayList<>();

        iClickUpdate = new IClickUpdate() {
            @Override
            public void click(Note note, ConstraintLayout constraintLayout) {
                 if(checkUpdate)
                 {


                     // neu nhu no dang o trang thai khong selected
                     if(constraintLayout.getBackground().getConstantState()
                             .equals(ContextCompat.getDrawable(getContext(), R.drawable.border_radius).getConstantState()))
                     {
                         // kich hoat che do selected
                         notesSelect.add(note);
                         apdaterNote.addList(notesSelect);
                         Log.d("fsfasfafd",notesSelect.size() + "" );
                         iStateList.totalList(notesSelect);
                         SelectAll selectAll = new SelectAll(true,false);
                         dataViewModel.setSelectAllMutableLiveData(selectAll);

                     }else {

                         notesSelect.remove(note);
                         apdaterNote.addList(notesSelect);
                         iStateList.totalList(notesSelect);
                         // chuyen no ve che do binh thuong
                         Log.d("fsafasd","233");
                     }
                 }else {
                // intent

                     Intent intent = new Intent(getActivity(), UpdateActivity.class);
                     intent.putExtra("checkUpdate", true);
                     intent.putExtra("note", note);
                     startActivityForResult(intent, 10);

                 }
            }

            @Override
            public void enableMote() {

               checkUpdate = true ;

            }
        };

        notes = new ArrayList<>();
        apdaterNote = new AdapterNote(notes, getContext(), iClickUpdate);

        rcvHome = view.findViewById(R.id.rcvHome);
        rcvHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvHome.setAdapter(apdaterNote);
        apdaterNote.notifyDataSetChanged();

        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dataViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), noteArrayList -> {
            notes.clear();
            notes.addAll(noteArrayList);
            apdaterNote.notifyDataSetChanged();
        });

        dataViewModel.getOptionString().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("Trash"))
                {

                    // Tạo một chuỗi có màu sắc cho văn bản nút "Đồng ý"
                    SpannableString positiveButtonText = new SpannableString("OK");
                    positiveButtonText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.appbar)), 0, positiveButtonText.length(), 0);
                    positiveButtonText.setSpan(new RelativeSizeSpan(1.0f), 0, positiveButtonText.length(), 0);


                    // Tạo một chuỗi có màu sắc cho văn bản nút "Hủy"
                    SpannableString negativeButtonText = new SpannableString("CANCLE");
                    negativeButtonText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.appbar)), 0, negativeButtonText.length(), 0);

                    negativeButtonText.setSpan(new RelativeSizeSpan(1.0f), 0, negativeButtonText.length(), 0);

                    if(notesSelect.size() > 0)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Delete the selected notes ?")
                                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Xử lý khi người dùng đồng ý xóa
                                        Log.d("sòhas",notesSelect.size() + " ");
                                        Toast.makeText(getContext(), "Deleted notes " + String.valueOf(notesSelect.size()), Toast.LENGTH_SHORT).show();
                                        dataViewModel.deleteMultipleNotes(notesSelect);
                                        dataViewModel.setOptionString("DeleteSuccess");


                                    }
                                })
                                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Xử lý khi người dùng hủy bỏ xóa
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
//                else if ()
//                {
//
//                }
            }
        });


        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), s -> {
            if(s.equals("Solarized")) {
                apdaterNote.setLayoutNote("Solarized","#FCF6E0");
            }else if(s.equals("Default"))
            {
                apdaterNote.setLayoutNote("Default","#FCF6E0");
            }
        });


        dataViewModel.getOnSelectedSort().observe(getViewLifecycleOwner(), s -> {

            switch (s) {
                case "TitleAtoZ":
                    Collections.sort(notes, Comparator.comparing(Note::getTitle));
                    apdaterNote.notifyDataSetChanged();
                    break;
                case "TitleZtoA":
                    Collections.sort(notes, Comparator.comparing(Note::getTitle).reversed());
                    apdaterNote.notifyDataSetChanged();

                    break;
                case "title_newest":
                    Collections.sort(notes, Comparator.comparing(Note::getTimeEdit).reversed());
                    apdaterNote.notifyDataSetChanged();

                    break;
                case "title_oldest":
                    Collections.sort(notes, Comparator.comparing(Note::getTimeEdit));
                    apdaterNote.notifyDataSetChanged();
                    break;

                default:
                    return;
            }
        });





        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case "Solarized":
                        Log.d("Fasfsa","thaydoi");
                         constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));
                        break;
                }

            }
        });

        // cai nay no nhan su kien viewmodel ben activity gui dene ne
        // de thoat khoi selectt or selectt alll ne

        dataViewModel.getSelectAllMutableLiveData().observe(getViewLifecycleOwner(), new Observer<SelectAll>() {
            @Override
            public void onChanged(SelectAll selectAll) {
                if(selectAll.isSelect())
                {
                    checkUpdate = true ;
                if(selectAll.isSelectAll())
                {
                    apdaterNote.selectAllNote("yes");
                    notesSelect.clear();
                    notesSelect.addAll(notes);
                    apdaterNote.addList(notesSelect);
                    iStateList.totalList(notesSelect);

                }

                }else {
             // khi nguoi dung huy

                    checkUpdate = false;
                    notesSelect.clear();
                    apdaterNote.addList(notesSelect);
//                    iStateList.totalList(notesSelect);


                }
                  }
        });


        dataViewModel.getStringMutableLiveData().observe(getViewLifecycleOwner(), s -> apdaterNote.setFilter(notes, s));

        return view;

    }

    public void getData(List<Note> noteArrayList) {

        noteArrayList.addAll(noteArrayList);
        apdaterNote.notifyDataSetChanged();
        checkUpdate = false;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10) {

            if (data != null) {
                Note note = (Note) data.getParcelableExtra("note");
//                Log.d("strikeHomeFragment",note.getBackgroundColorText());
                Log.d("strikeHomeFragment",note.getCategories().size() + " ");

                dataViewModel.updateNote(note);
                dataViewModel.setMutableLiveDataNote(note);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
