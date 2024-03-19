package com.example.notepad.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Note implements Parcelable {


    int idNote ;
    String title ;
    String content ;
    String timeEdit ;

    // caii nayyy la mauu cua ca background cua note
    String bgColors ;

    Boolean checkSelect = true ;

    int idNoteStyle ;
    String styleItalic ;
    String styleBold ;

    // cai nay la mau cua chu trong text trong note
    public String styleTextColor ;
    String styleUnderline ;
    String idCategory ;


    // them 2 truong nua background va strike

    // cai nay la background cua text trong note
    String backgroundColorText ;
    String strikerText = "false" ;

    ArrayList<Category> categories = new ArrayList<>() ;



    public Note() {
        categories = new ArrayList<>();
    }

    public Note(int idNote, String title, String content, String timeEdit, String bgColors, Boolean checkSelect, int idNoteStyle, String styleItalic, String styleBold, String styleTextColor, String styleUnderline, String idCategory, String backgroundColorText, String strikerText) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
        this.timeEdit = timeEdit;
        this.bgColors = bgColors;
        this.checkSelect = checkSelect;
        this.idNoteStyle = idNoteStyle;
        this.styleItalic = styleItalic;
        this.styleBold = styleBold;
        this.styleTextColor = styleTextColor;
        this.styleUnderline = styleUnderline;
        this.idCategory = idCategory;
        this.backgroundColorText = backgroundColorText;
        this.strikerText = strikerText;
    }


    protected Note(Parcel in) {
        idNote = in.readInt();
        title = in.readString();
        content = in.readString();
        timeEdit = in.readString();
        bgColors = in.readString();
        byte tmpCheckSelect = in.readByte();
        checkSelect = tmpCheckSelect == 0 ? null : tmpCheckSelect == 1;
        idNoteStyle = in.readInt();
        styleItalic = in.readString();
        styleBold = in.readString();
        styleTextColor = in.readString();
        styleUnderline = in.readString();
        idCategory = in.readString();
        backgroundColorText = in.readString();
        strikerText = in.readString();
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getStyleItalic() {
        return styleItalic;
    }

    public void setStyleItalic(String styleItalic) {
        this.styleItalic = styleItalic;
    }

    public String getStyleBold() {
        return styleBold;
    }

    public void setStyleBold(String styleBold) {
        this.styleBold = styleBold;
    }

    public String getStyleTextColor() {
        return styleTextColor;
    }

    public void setStyleTextColor(String styleTextColor) {
        this.styleTextColor = styleTextColor;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getStyleUnderline() {
        return styleUnderline;
    }

    public void setStyleUnderline(String styleUnderline) {
        this.styleUnderline = styleUnderline;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeEdit() {
        return timeEdit;
    }

    public void setTimeEdit(String timeEdit) {
        this.timeEdit = timeEdit;
    }

    public Boolean getCheckSelect() {
        return checkSelect;
    }

    public void setCheckSelect(Boolean checkSelect) {
        this.checkSelect = checkSelect;
    }

    public String getBgColors() {
        return bgColors;
    }



    public String getBackgroundColorText() {
        return backgroundColorText;
    }

    public void setBackgroundColorText(String backgroundColorText) {
        this.backgroundColorText = backgroundColorText;
    }

    public String getStrike() {
        return strikerText;
    }

    public void setStrike(String strike) {
        this.strikerText = strike;
    }

    public void setBgColors(String bgColors) {
        this.bgColors = bgColors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(idNote);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(timeEdit);
        parcel.writeString(bgColors);
        parcel.writeByte((byte) (checkSelect == null ? 0 : checkSelect ? 1 : 2));
        parcel.writeInt(idNoteStyle);
        parcel.writeString(styleItalic);
        parcel.writeString(styleBold);
        parcel.writeString(styleTextColor);
        parcel.writeString(styleUnderline);
        parcel.writeString(idCategory);
        parcel.writeString(backgroundColorText);
        parcel.writeString(strikerText);
        parcel.writeTypedList(categories);
    }


    public int getIdNoteStyle() {
        return idNoteStyle;
    }

    public void setIdNoteStyle(int idNoteStyle) {
        this.idNoteStyle = idNoteStyle;
    }


    @Override
    public String toString() {
        return "Note{" +
                "idNote=" + idNote +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timeEdit='" + timeEdit + '\'' +
                ", bgColors='" + bgColors + '\'' +
                ", checkSelect=" + checkSelect +
                ", idNoteStyle=" + idNoteStyle +
                ", styleItalic='" + styleItalic + '\'' +
                ", styleBold='" + styleBold + '\'' +
                ", styleTextColor='" + styleTextColor + '\'' +
                ", styleUnderline='" + styleUnderline + '\'' +
                ", idCategory='" + idCategory + '\'' +
                ", backgroundColor='" + backgroundColorText + '\'' +
                ", strike='" + strikerText + '\'' +
                '}';
    }
}
