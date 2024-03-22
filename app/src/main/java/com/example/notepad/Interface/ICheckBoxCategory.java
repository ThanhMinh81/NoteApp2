package com.example.notepad.Interface;

import android.widget.CheckBox;

import com.example.notepad.Model.Category;

public interface ICheckBoxCategory {

    void clickCheckbox(Category  category,Boolean aBoolean);

    void initValueCheckBox(Category category , CheckBox checkBox);


}
