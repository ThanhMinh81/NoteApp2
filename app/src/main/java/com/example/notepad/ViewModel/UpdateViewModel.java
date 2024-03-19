package com.example.notepad.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.notepad.Model.Selected;

public class UpdateViewModel {

    private MutableLiveData<Selected> mutableLiveData = new MutableLiveData<>();


    public MutableLiveData<Selected> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setMutableLiveData(Selected formattable) {
        this.mutableLiveData.setValue(formattable);
    }
}
