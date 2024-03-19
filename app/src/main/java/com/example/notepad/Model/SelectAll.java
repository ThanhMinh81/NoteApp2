package com.example.notepad.Model;

public class SelectAll {

    private boolean select ;
    private  boolean selectAll ;

    public SelectAll(boolean select, boolean selectAll) {
        this.select = select;
        this.selectAll = selectAll;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
}
