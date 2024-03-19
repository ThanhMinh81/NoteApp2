package com.example.notepad.Interface;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.notepad.Model.Note;

public interface IClickUpdate {

    void click(Note note , ConstraintLayout constraintLayout);

    void enableMote();

}
