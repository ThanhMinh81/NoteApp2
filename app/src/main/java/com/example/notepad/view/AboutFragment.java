package com.example.notepad.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Adapter.AdapterItemNoteCategory;
import com.example.notepad.Interface.IClickLongTime;
import com.example.notepad.Model.Note;
import com.example.notepad.R;
import com.example.notepad.ViewModel.DataViewModel;

import java.util.ArrayList;

public class AboutFragment extends Fragment {

    private View view;
    String idCategory;

    AdapterItemNoteCategory adapterItemNoteCategory;

    ArrayList<Note> notes;

    RecyclerView rcvAboutFragment;

    DataViewModel dataViewModel;

    IClickLongTime iClickLongTime;

    ConstraintLayout constraintLayout;

    String nameCategory ;



    public AboutFragment(String idCategory , String nameCategory)
    {
        this.idCategory = idCategory ;
        this.nameCategory = nameCategory ;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_about, container, false);

        iClickLongTime = new IClickLongTime() {
            @Override
            public void click(Note note) {
                Log.d("Fsfas", "sdfas");
                dataViewModel.deleteNoteCategory(note, idCategory);
            }
        };

        notes = new ArrayList<>();
        constraintLayout = view.findViewById(R.id.constraintAbout);
        adapterItemNoteCategory = new AdapterItemNoteCategory(getContext(), notes, iClickLongTime, nameCategory);
        rcvAboutFragment = view.findViewById(R.id.rcvFragmentAbout);
        rcvAboutFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvAboutFragment.setAdapter(adapterItemNoteCategory);
        adapterItemNoteCategory.notifyDataSetChanged();

        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dataViewModel.getNoteIDCategory(this.idCategory);

        dataViewModel.getListNoteCategory().observe(getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> noteArrayList) {
                Log.d("2122121",noteArrayList.size() + " ");
                notes.clear();
                notes.addAll(noteArrayList);
                adapterItemNoteCategory.notifyDataSetChanged();
            }
        });

        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case "Solarized":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));
                        break;
                    case "Default":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari2));
                        break;
                }


            }

        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

    }
}
