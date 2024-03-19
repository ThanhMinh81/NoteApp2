package com.example.notepad.view;

import static android.app.PendingIntent.getActivity;
import static androidx.appcompat.widget.SearchView.*;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import com.example.notepad.Adapter.AdapterSelectCategory;
import com.example.notepad.ColorCustom;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.ICheckSelect;
import com.example.notepad.Interface.IClickCategory;
import com.example.notepad.MainActivity;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.Model.Selected;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.ViewModel.MyViewModelFactory;
import com.example.notepad.ViewModel.UpdateViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.notepad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UpdateActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edTitle, edContent;

    DBManager databaseHandler;

    Note noteData;

    // cai nay color cho background
    String DefaultColor;

    // cai nay la mau cua background color
    String bgColorText     ;

    SearchView searchView ;

    LinearLayout linearLayout;

    MenuItem menuItemAdd ;

    MenuItem menuItemSearch ;

    SearchManager searchManager ;

    MenuItem menuItemUndo ;

    CheckBox cbBold, cbItalic, cbUnderline, cbColorText, cbBgColor, cbStrike;

    UpdateViewModel updateViewModel;

    DataViewModel dataViewModel;

    Boolean  isSearchViewExpanded = false;

    // mau cua hinh anhh ne
    String colorText;

    String themeStyle = "Default";

    Dialog dialog;

    SharedPreferences sharedPreferences;

    Drawable overflowIcon;
    ArrayList<Category> categoryArrayList;
    String idCategory = "null";
    IClickCategory iClickCategory;

    TextWatcher textWatcher ;

    StrikethroughSpan strikethroughSpan;
    AdapterSelectCategory adapterSelectCategory;

    Boolean checkUpdate = false;
    String format1;

    String colorTextAddNote = "";

    String format2;

    SpannableStringBuilder spannableStringBuilder;
    SpannableString spannableString2;
    Note note;

    ArrayList<Category> categories = new ArrayList<>();


    ArrayList<Category> categoriAvailabe = new ArrayList<>();





    boolean check = false ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        toolbar = this.<Toolbar>findViewById(R.id.toolbarUpdate);
        setSupportActionBar(toolbar);


        // xet icon back cho toolbar ne


        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(view -> {

            if(searchView != null)
            {
                if (!searchView.isIconified()) {
                    // close search view
                    searchView.setIconified(true);
                }else {
                    finish();
                }
            }
            else {
                finish();
            }


        });

        // icon de xo ra thanh menu bar
        overflowIcon = toolbar.getNavigationIcon();

        Intent intent = getIntent();
        checkUpdate = intent.getBooleanExtra("checkUpdate", false);
        Log.d("fsffasf", checkUpdate + " ");

        databaseHandler = new DBManager(this);

        databaseHandler.open();

        // thay doi mau sac cho icons

        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menuverticalicon));
        overflowIcon = toolbar.getOverflowIcon();


        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        linearLayout = this.<LinearLayout>findViewById(R.id.layoutUpdateNote);

        MyViewModelFactory factory = new MyViewModelFactory(databaseHandler);
        dataViewModel = new ViewModelProvider(this, factory).get(DataViewModel.class);

        categoryArrayList = new ArrayList<>();


        iClickCategory = new IClickCategory() {
            @Override
            public void click(Category category, Boolean aBoolean) {
                if (aBoolean)
                {
                    categories.add(category);
                }else {
                  try {
                      for (Category category1 : categories)
                      {
                          if(category1.getNameCategory().trim().trim().equals(category.getNameCategory().trim()))
                          {
                              categories.remove(category1);
                          }
                      }
                      Log.d("fspfas",categories.size() + "");
                  }catch (Exception e)
                  {
                      Log.d("sdfasfs",e.toString());

                  }
                }
            }

            @Override
            public void readyCheck(Category category) {  }
        };



        sharedPreferences = UpdateActivity.this.getSharedPreferences(" ", Context.MODE_PRIVATE);

        themeStyle = sharedPreferences.getString("theme_system", "Default");

        // lay dai mau mo do cho add
        DefaultColor = ColorCustom.colorDefault;

        updateViewModel = new UpdateViewModel();

        colorText = "#000000";

        dialog = new Dialog(UpdateActivity.this);

        // ban dau la color mau trong suot
        bgColorText = "#FFFFFF" ;

        checkBoxSelect();
        strikethroughSpan = new StrikethroughSpan();


         handleEventLiveData();

        if (checkUpdate) {

            noteData = getIntent().getExtras().getParcelable("note");


//             #A2F38C
//            #FCFBCE

//            if (!noteData.getBgColors().equals("#FCFBCE"))
//            {
//                // neui ko phai la bg mac dinh doi mau bg
//                Log.d("fsodfiashdfasf", noteData.getBgColors());
//
//                Drawable myIcon1 = AppCompatResources.getDrawable(this, R.drawable.border_selected);
//                GradientDrawable gradientDrawable1 = (GradientDrawable) myIcon1;
//                gradientDrawable1.setColor(Color.parseColor(noteData.getBgColors()));
//                // Thiết lập hướng của gradient
//                gradientDrawable1.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//                 linearLayout.setBackground(myIcon1);
//
//            }

            ICheckSelect iCheckSelect = new ICheckSelect() {
                @Override
                public void check(Category category, CheckBox checkBox) {
                    for (Category category1 : noteData.getCategories())
                    {
                        if (category1.getIdCategory().equals(category.getIdCategory()))
                        {
                            checkBox.setChecked(true);
                          if(!categories.contains(category1))
                          {
                              categories.add(category1);
                          }
                        }
                    }
                }
            };

            adapterSelectCategory = new AdapterSelectCategory(categoryArrayList, iClickCategory,iCheckSelect);

            dataViewModel.getAllListCategory();

            dataViewModel.getListCategory().observe(UpdateActivity.this, new Observer<ArrayList<Category>>() {
                @Override
                public void onChanged(ArrayList<Category> categories) {

                    categoryArrayList.clear();
                    categoryArrayList.addAll(categories);

                    adapterSelectCategory.notifyDataSetChanged();
                }
            });



            edTitle.setText(noteData.getTitle());

            edContent.setText(noteData.getContent());

            format1 = edContent.getText().toString();

            spannableStringBuilder = new SpannableStringBuilder(format1);


            cbColorText.setBackground(getDrawable(R.drawable.checkbox_formmated));

            initValueCheckBox();

            checkSelect();


        } else {

            format1 = edContent.getText().toString();
            spannableStringBuilder = new SpannableStringBuilder(format1);
            Log.d("kiemtradulieu", format1.toString());
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.appbar));
            spannableStringBuilder.setSpan(colorSpan1, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            ICheckSelect iCheckSelect = new ICheckSelect() {
                @Override
                public void check(Category category, CheckBox checkBox) {

                }
            };

            adapterSelectCategory = new AdapterSelectCategory(categoryArrayList, iClickCategory,iCheckSelect);

            dataViewModel.getAllListCategory();

            dataViewModel.getListCategory().observe(UpdateActivity.this, new Observer<ArrayList<Category>>() {
                @Override
                public void onChanged(ArrayList<Category> categories) {

                    categoryArrayList.clear();
                    categoryArrayList.addAll(categories);

                    adapterSelectCategory.notifyDataSetChanged();
                }
            });


//            cbBold.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CheckBox checkBox = (CheckBox) view;
//                    cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
//
//                    if(checkBox.isChecked())
//                    {
//                        if (!TextUtils.isEmpty(edContent.getText())) {
//
//                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format1);
//                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            edContent.setText(spannableStringBuilder);
//                            edContent.setSelection(edContent.getText().length());
//                            cbBold.setChecked(true);
//
//                        } else {
//                            // Nếu không có văn bản, chỉ đặt kiểu đậm cho văn bản mới khi người dùng nhập
//                            edContent.setTypeface(Typeface.DEFAULT_BOLD);
//                            cbBold.setChecked(true);
//                        }
//                    }else {
//                        cbBold.setChecked(false);
//                    }
//                }
//            });

             checkSelect();


        }

    }

    private void handleEventLiveData() {
        updateViewModel.getMutableLiveData().observe(this, selected -> {

            if (selected.isCheck()) {
                if (selected.getIndex().contains("#")) {
                    // set Color
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(selected.getIndex()));
                    spannableStringBuilder.setSpan(colorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("Under")) {
                    // set Underline
                    spannableStringBuilder.setSpan(new UnderlineSpan(), 0, format1.length(), 0);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("Strike")) {

                    spannableStringBuilder.setSpan(strikethroughSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("BgColorText")) {
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(selected.getValue()));
                    spannableStringBuilder.setSpan(backgroundColorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else {
                    // cai nay xet text
                    spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt(selected.getIndex())), 0, spannableStringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                }
            }
            else
            {
                StyleSpan[] styleSpans = spannableStringBuilder.getSpans(0, format1.length(), StyleSpan.class);

                if (selected.getIndex().equals("Black")) {
                    Log.d("kiemtraduleu", "sasfdf");
                    // xet cho no ve mau den
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
                    colorText =   "#000000";
                    spannableStringBuilder.setSpan(colorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (selected.getIndex().equals("BgColorText")) {
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#00000000"));
                    spannableStringBuilder.setSpan(backgroundColorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    bgColorText = "#FFFFFF" ;
                }

                if (selected.getIndex().equals("Strike")) {
                    spannableStringBuilder.removeSpan(strikethroughSpan);
                }

                for (StyleSpan styleSpan : styleSpans)
                {
                    if (selected.getIndex().equals("Under")) {
                        spannableStringBuilder.removeSpan((new UnderlineSpan()));
                    } else if (!selected.getIndex().equals("Black")
                            && !selected.getIndex().equals("BgColorText")
                            && !selected.getIndex().equals("Strike")) {
                        if (styleSpan.getStyle() == Integer.parseInt(selected.getIndex())) {
                            Log.d(" xoaaa ", styleSpan.getStyle() + " ");
                            spannableStringBuilder.removeSpan(styleSpan);
                        }
                    }
                }

                edContent.setText(spannableStringBuilder);
            }

        });
    }

    private void initValueCheckBox() {

        // check mau cho bg color
        if (!noteData.getBgColors().equals(ColorCustom.colorDefault)) {
            DefaultColor = noteData.getBgColors();
        }

        // set background cho ca layout
        if (!noteData.getBgColors().equals("#FCFBCE")) {


            Drawable myIcon1 = AppCompatResources.getDrawable(this, R.drawable.border_radius);
            GradientDrawable gradientDrawable1 = (GradientDrawable) myIcon1;
            gradientDrawable1.setColor(Color.parseColor(noteData.getBgColors()));
            // Thiết lập hướng của gradient
            gradientDrawable1.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            linearLayout.setBackground(myIcon1);

        }else {

            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                    new int[]{Color.parseColor("#F9F8C2"),
                      Color.parseColor("#FCFCCF")});
            drawable.setShape(GradientDrawable.RECTANGLE);

            drawable.setStroke(4, Color.parseColor("#836E4C"));
            drawable.setCornerRadius(15);

//            yourView.setBackground(drawable);
//
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setShape(GradientDrawable.RECTANGLE); // Đặt hình dạng là hình chữ nhật
//            drawable.setStroke(4, Color.parseColor("#836E4C")); // Độ dày và màu của border
//            drawable.setColor(Color.parseColor("#F6EABE")); // Màu nền solid
//            drawable.setCornerRadius(15);


// Áp dụng drawable làm background cho view của bạn
            linearLayout.setBackground(drawable);


//            linearLayout.setBackgroundColor(Color.parseColor(DefaultColor));
//            linearLayout.setBackground(getDrawable(R.drawable.border_radius));
        }

        if (noteData.getStyleBold().equals("true")) {
            cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbBold.setChecked(true);
            spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt("1")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        } else {
            cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbBold.setChecked(false);
        }
        if (noteData.getStyleItalic().equals("true")) {
            Log.d("lieiee", "idfsadfa");
            cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbItalic.setChecked(true);
            spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt("2")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        } else {
            cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbItalic.setChecked(false);
        }

        if (noteData.getStyleUnderline().equals("true")) {
//            Log.d("aacasae",note.getStyleUnderline());
            cbUnderline.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbUnderline.setChecked(true);
            spannableStringBuilder.setSpan(new UnderlineSpan(), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        }


        if(noteData.getStrike().equals("true"))
        {
            // khong hieu sao nhung bat buoc phai co dong nay moi duoc
            cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbStrike.setChecked(true);
            spannableStringBuilder.setSpan(strikethroughSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        }

        if(!noteData.getBackgroundColorText().equals("#FFFFFF"))
        {
            cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
            cbBgColor.setChecked(true);
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(noteData.getBackgroundColorText()));
            spannableStringBuilder.setSpan(backgroundColorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.d("656565",noteData.getBackgroundColorText());
            edContent.setText(spannableStringBuilder);
            bgColorText = noteData.getBackgroundColorText();
        }

        if(!noteData.getStyleTextColor().equals("#000000"))
        {
            cbColorText.setChecked(true);
            colorText = noteData.getStyleTextColor();
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor(noteData.getStyleTextColor()));
            spannableStringBuilder.setSpan(colorSpan1, 0, format1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        }else {
            cbColorText.setChecked(false);
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#000000"));
            spannableStringBuilder.setSpan(colorSpan1, 0, format1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);
        }
    }

    private void checkSelect() {

         if(cbBold.isChecked())
         {
             spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableStringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             edContent.setText(spannableStringBuilder);


         }

        if(cbItalic.isChecked())
        {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableStringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);

        }

        if(cbUnderline.isChecked())
        {
            spannableStringBuilder.setSpan(new UnderlineSpan(), 0, spannableStringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableStringBuilder);

        }

          textWatcher = new TextWatcher() {

            private boolean isBold = false;
            String resour ;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("Fsafasdfa2222","asfasfa");
                spannableStringBuilder.replace(0,spannableStringBuilder.toString().length(), charSequence.toString());
                format1 = charSequence.toString() ;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isBold) {
                    Log.d("Fsafasdfa","asfasfa");
                    isBold = true;
//                    format1 = resour;
                    edContent.setText(spannableStringBuilder);
                    // chuyen con tro ve cuoi doan editext
                    edContent.setSelection(spannableStringBuilder.toString().length());
                } else {
                    isBold = false;
                }
            }
        };

        edContent.addTextChangedListener(textWatcher);


//        edContent.addTextChangedListener(new TextWatcher() {
//
//            private boolean isBold = false;
//            String resour ;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("Fsafasdfa2222","asfasfa");
//
//                spannableStringBuilder.replace(0,spannableStringBuilder.toString().length(), s.toString());
//                 format1 = s.toString() ;
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // ban dau la  false
//
//                if (!isBold) {
//                    Log.d("Fsafasdfa","asfasfa");
//                    isBold = true;
////                    format1 = resour;
//                    edContent.setText(spannableStringBuilder);
//                    // chuyen con tro ve cuoi doan editext
//                    edContent.setSelection(spannableStringBuilder.toString().length());
//                } else {
//                    isBold = false;
//                }
//            }
//        });
    }



    private void checkBoxSelect() {
        cbBold = findViewById(R.id.cbBold);
        cbItalic = findViewById(R.id.cbItalic);
        cbUnderline = findViewById(R.id.cbUnderline);
        cbColorText = findViewById(R.id.cbColorText);
        cbBgColor = findViewById(R.id.cbBgColor);
        cbStrike = findViewById(R.id.cbStrike);
        cbBold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbBold.setChecked(true);
                    Selected selected = new Selected("1", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbBold.setChecked(false);
                    Selected selected = new Selected("1", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });
        cbItalic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbItalic.setChecked(true);
                    Selected selected = new Selected("2", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbItalic.setChecked(false);
                    Selected selected = new Selected("2", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });
        cbUnderline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    cbUnderline.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbUnderline.setChecked(true);
                    Selected selected = new Selected("Under", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbUnderline.setChecked(false);
                    Selected selected = new Selected("Under", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });

        cbStrike.setOnClickListener(view -> {

            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()) {
                cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbStrike.setChecked(true);
                Selected selected = new Selected("Strike", true);
                updateViewModel.setMutableLiveData(selected);
            } else {
                cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbStrike.setChecked(false);
                Selected selected = new Selected("Strike", false);
                updateViewModel.setMutableLiveData(selected);
            }
        });

        cbBgColor.setOnClickListener(view -> {

            CheckBox checkBox = (CheckBox) view;

            if (checkBox.isChecked()) {

                Log.d("fsfdafaaaa", "saa");
                AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(UpdateActivity.this, R.color.appbar, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));

                        bgColorText = hexColor ;

                        cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
                        cbBgColor.setChecked(true);
                        Log.d("fsdfasfafs", String.valueOf(bgColorText) + " ");
                        Selected selected = new Selected("BgColorText", String.valueOf(bgColorText), true);
                        updateViewModel.setMutableLiveData(selected);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                    }
                });
                ambilWarnaDialog.show();
            } else {
                cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbBgColor.setChecked(false);
                Selected selected = new Selected("BgColorText", false);
                updateViewModel.setMutableLiveData(selected);
            }

        });
        cbColorText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(UpdateActivity.this, R.color.appbar, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color)
                        {

                            colorText = String.format("#%06X", (0xFFFFFF & color));

                            Selected selected = new Selected(String.valueOf(colorText), true);

                            Log.d("sdfsaodf", String.valueOf(colorText));

                            updateViewModel.setMutableLiveData(selected);

                            cbColorText.setBackground(getDrawable(R.drawable.checkbox_formmated));

                            cbColorText.setChecked(true);

                        }

                        @Override
                        public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                        }
                    });
                    ambilWarnaDialog.show();
                } else {

                    Selected selected = new Selected("Black", false);
                    updateViewModel.setMutableLiveData(selected);

                }
            }
        });

    }


    private void showDialogCategory(Context context) {

        TextView tvCancelCategory, tvOkCategory;


        RecyclerView rcvCategory;



        dialog.setContentView(R.layout.selected_category);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        tvCancelCategory = dialog.findViewById(R.id.tvCancleCategory);
        tvOkCategory = dialog.findViewById(R.id.tvOkCategory);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        rcvCategory = dialog.findViewById(R.id.rcvSelectCategory);
        rcvCategory.setLayoutManager(layoutManager);
        rcvCategory.setAdapter(adapterSelectCategory);




//        dataViewModel.getMutableLiveDataAvailableCategory().observe(UpdateActivity.this, new Observer<ArrayList<Category>>() {
//            @Override
//            public void onChanged(ArrayList<Category> categories) {
//
//                Log.d("kiemtradulieu",categories.size() + "  ");
//                categoriAvailabe.addAll(categories);
//                adapterSelectCategory.restarCheck(categoriAvailabe );
//
//
//            }
//        });
//
//        dataViewModel.getCategoryOfNote(String.valueOf(noteData.getIdNote()));

        tvOkCategory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                dataViewModel.updateCategoryNote(noteData, idCategory);
                Toast.makeText(context, "Cập nhật thể loại thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        tvCancelCategory.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);

        getMenuInflater().inflate(R.menu.menu_save, menu);

        getMenuInflater().inflate(R.menu.searchview,menu);

        menuItemSearch = menu.findItem(R.id.item_search);

        SearchManager searchManager = (SearchManager) UpdateActivity.this.getSystemService(Context.SEARCH_SERVICE);


        searchView = null;

        if (menuItemSearch != null) {
            searchView = (SearchView) menuItemSearch.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(UpdateActivity.this.getComponentName()));
        }



        if (menuItemSearch != null) {
            Drawable icon = menuItemSearch.getIcon();

            // Thay doi mau icon
            if (icon != null) {
                icon.setColorFilter(ContextCompat.getColor(UpdateActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                menuItemSearch.setIcon(icon);

//                searchView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }

        // ham searchview

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                highlightText2(newText.toString().trim());

                Log.d("newttt",newText);

                return true;
            }
        });




 // thay doi mau cho icon back toolbar
        // Tạo một Drawable từ vector drawable hoặc shape drawable
        Drawable navigationIcon = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        if (navigationIcon != null) {
            // Thiết lập màu cho Drawable
            navigationIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

            // Đặt Drawable đã tùy chỉnh làm NavigationIcon cho Toolbar
            toolbar.setNavigationIcon(navigationIcon);
        }


        // item menu save
        menuItemAdd = menu.findItem(R.id.nav_saveUpdate);

        // item menu search
        // khong cho menuSearch duoc hien thi
        MenuItem menuItem =  menu.findItem(R.id.item_search);
        menuItem.setVisible(true);


//      Tao menu undo va thay doi mau cho title
        getMenuInflater().inflate(R.menu.menu_undo, menu);
        menuItemUndo = menu.findItem(R.id.undo_menu);
        SpannableString spannable1 = new SpannableString(menuItemUndo.getTitle().toString());

        menuItemSearch = menu.findItem(R.id.item_search);
        menuItemSearch.setVisible(false);
          searchManager = (SearchManager) UpdateActivity.this.getSystemService(Context.SEARCH_SERVICE);
        spannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable1.length(), 0);
        menuItemUndo.setTitle(spannable1);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // thay doi mau cho menu toolbar
        SpannableString spannable = new SpannableString(menuItemAdd.getTitle().toString());
        // Thay doi mau cua menu Sort
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable.length(), 0);
        menuItemAdd.setTitle(spannable);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if (overflowIcon != null) {
            overflowIcon.setColorFilter(ContextCompat.getColor(UpdateActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            toolbar.setOverflowIcon(overflowIcon);
        }


        if (checkUpdate) {
//            if (themeStyle.equals("Solarized")) {
//                // thay doi mau cho menu toolbar
//                SpannableString spannable = new SpannableString(menuItemAdd.getTitle().toString()
//
//                );
//                // Thay doi mau cua menu Sort
//                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextSolari)), 0, spannable.length(), 0);
//                menuItemAdd.setTitle(spannable);
//
//                //   Thay đổi màu của OverflowIcon
//                if (overflowIcon != null) {
//                    overflowIcon.setColorFilter(ContextCompat.getColor(UpdateActivity.this, R.color.colorTextSolari), PorterDuff.Mode.SRC_IN);
//                    toolbar.setOverflowIcon(overflowIcon);
//                }
//
//                toolbar.setBackgroundColor(getResources().getColor(R.color.themeSolari));
//                toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextSolari));
//
//            } else {




        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_saveUpdate) {
            if (checkUpdate) {
                // update note
                eventClickUpdate();
            } else {
                // add note
                addNote();
            }
        } else if (id == R.id.nav_colorize) {

            OpenColorPickerDialog(true);

        } else if (id == R.id.item_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_category) {
            showDialogCategory(UpdateActivity.this);
        }else if(id == R.id.nav_search)
        {
            menuItemAdd.setVisible(false);
            menuItemUndo.setVisible(false);
            menuItemSearch.setVisible(true);


            menuItemSearch.expandActionView();
            searchView.setIconified(false);




            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    Log.d("Fsafasfas","Dong");

                    edContent.addTextChangedListener(textWatcher);

                    menuItemAdd.setVisible(true);
                    menuItemUndo.setVisible(true);
                    menuItemSearch.setVisible(false);

                    // Xử lý khi searchView được đóng
                    // Ở đây bạn có thể thực hiện các hành động tùy ý khi searchView được đóng
                    return false;
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {

        if (edTitle.getText().toString().length() > 0 && edContent.getText().toString().length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();

            note.setContent(edContent.getText().toString());
            note.setTitle(edTitle.getText().toString());
            note.setTimeEdit(time.toString());

            // set mau cho text color
            note.setStyleTextColor(colorText);


            note.setBgColors(DefaultColor);



            if (cbBold.isChecked()) {
//                Log.d("boldUpdateActivity", "sdfda");
                note.setStyleBold("true");
            } else {
                note.setStyleBold("false");
            }


            if (cbItalic.isChecked()) {
                Log.d("itaalcca", "sdfda");

                note.setStyleItalic("true");
            } else {
                note.setStyleItalic("false");

            }

            if (cbUnderline.isChecked()) {
                note.setStyleUnderline("true");
            } else {
                note.setStyleUnderline("false");

            }

            if(cbColorText.isChecked())
            {
                note.setStyleTextColor(String.valueOf(colorText));
            }


            if(cbStrike.isChecked())
            {
                note.setStrike("true");
            }else {
                note.setStrike("false");

            }

            note.setBackgroundColorText(String.valueOf(bgColorText));

            note.setBgColors(String.valueOf(DefaultColor));

            note.setIdCategory("null");

            note.setCategories(categories);

            Intent intent = new Intent();


            Log.d("corrrrr",note.toString() + " ");

            intent.putExtra("note", note);

            setResult(RESULT_OK, intent);

            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }


    }

    private void eventClickUpdate() {
        if (edTitle.getText().toString().length() > 0 && edContent.getText().toString().length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();
            note.setIdNote(noteData.getIdNote());
            note.setContent(edContent.getText().toString());
            note.setTitle(edTitle.getText().toString());
            note.setTimeEdit(time.toString());


            note.setStyleTextColor(colorText);

            note.setBgColors(DefaultColor);

            if (cbBold.isChecked()) {
                note.setStyleBold("true");
            } else {
                note.setStyleBold("false");
            }

            if (cbUnderline.isChecked()) {
                note.setStyleUnderline("true");
            } else {
                note.setStyleUnderline("false");

            }

            if (cbItalic.isChecked()) {
                Log.d("itaalcca", "sdfda");

                note.setStyleItalic("true");
            } else {
                note.setStyleItalic("false");
            }

            if(cbStrike.isChecked())
            {
                note.setStrike("true");
            }else {
                note.setStrike("false");
            }


            note.setBackgroundColorText(String.valueOf(bgColorText));
            note.setIdNoteStyle(noteData.getIdNoteStyle());

            Log.d("checkNotess", categories.size() + " ");
            // ban dau toi xet id category la null
            note.setCategories(categories);

            Intent intent = new Intent();



            intent.putExtra("note", note);


            setResult(RESULT_OK, intent);

//            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }
    }

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, R.color.appbar, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                // chuyen doi ma mau  -15741650 thanh ma mau  0xFFFFFF

                String hexColor = String.format("#%06X", (0xFFFFFF & color));

                DefaultColor = hexColor;

                Log.d("kemtraddulie", String.valueOf(DefaultColor) + " ");

                linearLayout.setBackgroundColor(Color.parseColor(String.valueOf(hexColor)));

            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

//                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }


    private void highlightText2(String s) {

        edContent.removeTextChangedListener(textWatcher);

        SpannableString spannableString = new SpannableString(edContent.getText());
        edContent.setText(spannableString);
        BackgroundColorSpan[] backgroundColorSpan =
                spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);
        for (BackgroundColorSpan bgSpan : backgroundColorSpan) {
            spannableString.removeSpan(bgSpan);
        }
        int indexOfKeyWord = spannableString.toString().indexOf(s);
        Log.d("newtTet",indexOfKeyWord + " ==  " + s.length());
        while (indexOfKeyWord > 0) {

            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyWord,
                    indexOfKeyWord + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            indexOfKeyWord = spannableString.toString().indexOf(s, indexOfKeyWord + s.length());

        }
        edContent.setText(spannableString);
    }







//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()) {
//            // dong searchview
//            searchView.setIconified(true);
//        } else {
//            super.onBackPressed();
//        }
//    }

}