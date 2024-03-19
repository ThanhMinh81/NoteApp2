package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.Adapter.AdapterSelectCategory;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.IClickCategory;
import com.example.notepad.Interface.IData;
import com.example.notepad.Interface.IStateList;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.Model.SelectAll;
import com.example.notepad.view.AboutFragment;
import com.example.notepad.view.CategoryFragment;
import com.example.notepad.view.HomeFragment;
import com.example.notepad.view.SelectAllActivity;
import com.example.notepad.view.SettingFragment;
import com.example.notepad.view.UpdateActivity;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.ViewModel.MyViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    DBManager databaseHandler;

    FloatingActionButton floatingActionButton;
    DataViewModel dataViewModel;
    ArrayList<Note> noteArrayList;

    Dialog dialog;

    IStateList iStateList;

    IClickCategory clickCategory;

    // Khai báo biến toàn cục để lưu trạng thái của DrawerLayout
    private boolean isDrawerOpen = false;

    String selector = "";

    Toolbar toolbar;

    MenuItem searchItem;
    MenuItem menuItemSort;


    // icon open menu selecter
    Drawable overflowIcon;

    NavigationView navigationView;

    String valueTheme;

    ActionBarDrawerToggle toggle;
    int itemId = 0;

    boolean checkSelected = false;

    SearchView searchView;

    AdapterSelectCategory adapterSelectCategory;
    ArrayList<Category> categoryArrayList;

    ArrayList<Category> categories;

    ArrayList<Note> noteArrayListSelected = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menuverticalicon));
        overflowIcon = toolbar.getOverflowIcon();


        toolbar.setTitle("Notepad Free");

        drawerLayout = findViewById(R.id.drawer_layout);
        floatingActionButton = findViewById(R.id.fab);

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        dialog = new Dialog(MainActivity.this);

        // tham so truyen vao
        databaseHandler = new DBManager(this);
        databaseHandler.open();
//        int abc = databaseHandler.getAllNotes().size();
//        databaseHandler.getAllNoteCategory() ;
        databaseHandler.getAllCategory();

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(3.0f);


        MyViewModelFactory factory = new MyViewModelFactory(databaseHandler);

        dataViewModel = new ViewModelProvider(this, factory).get(DataViewModel.class);


        dataViewModel.getData();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        dataViewModel.getAllListCategory();

        iStateList = new IStateList() {
            @Override
            public void totalList(ArrayList<Note> noteArrayList) {
                noteArrayListSelected.clear();
                noteArrayListSelected.addAll(noteArrayList);
                toolbar.setTitle(String.valueOf(noteArrayList.size()));
            }
        };


        dataViewModel.getListCategory().observe(this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                itemId = 0;
                MenuItem item = navigationView.getMenu().getItem(1);
                SubMenu subMenu = item.getSubMenu();
                int size = subMenu.size();
                for (int i = size - 1; i > 0; i--) {
                    subMenu.removeItem(i);
                }
                for (Category category : categories) {
                    itemId++;
                    subMenu.add(Menu.NONE, Integer.parseInt(category.getIdCategory()), Menu.NONE, category.getNameCategory()).setIcon(R.drawable.icons8tag40);
                }

            }
        });


        initDataCategory();

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        noteArrayList = new ArrayList<>();

        // backup data note
        noteArrayList.addAll(dataViewModel.getValueArr());

        dataViewModel.getMutableLiveDataNote().observe(this, note -> {
            Log.d("safdfa", note.getTitle());
            noteArrayList.add(note);
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(iStateList)).commit();
            navigationView.setCheckedItem(R.id.fragmentHome);
        }

        floatingActionButton.setOnClickListener(view -> {
//                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
//                intent.putExtra("checkUpdate",false);

//                Bundle extras = new Bundle();
//                extras.putBoolean("checkUpdate",false);
//                intent.putExtras(extras);

            intent.putExtra("checkUpdate", false);

            startActivityForResult(intent, 10);

        });


        dataViewModel.getSelectAllMutableLiveData().observe(this, new Observer<SelectAll>() {
            @Override
            public void onChanged(SelectAll selectAll) {
                Log.d("sfasdffasd", selectAll.toString());
                // cai nay la ssu ly khi ma nguoi ddung chon item ben homefraggment
                checkSelected = selectAll.isSelect();
                invalidateOptionsMenu();
                toolbar.setTitle(String.valueOf(HomeFragment.notesSelect.size()));
            }
        });


        // cai nay minh phai tu custom lai vi  drawerLayout bi loi chi do ko tu mo nua
        // khi ma them icon back cho select all thi bi nhu vay chua fix dc
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false; // DrawerLayout đã được đóng
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true; // DrawerLayout đã được mở
            }
        });

        dataViewModel.getOptionString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("DeleteSuccess")) {
                    SelectAll selectAll = new SelectAll(false, false);
                    dataViewModel.setSelectAllMutableLiveData(selectAll);
                    checkSelected = false;
                    invalidateOptionsMenu();
                }
            }
        });


        setUpMenuToolbar();


    }

    private void setUpMenuToolbar() {

        dataViewModel.getDataCurrentPage().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s) {
                    case "Category":
                        searchItem.setVisible(false);
                        menuItemSort.setVisible(false);
                        toolbar.getOverflowIcon().setVisible(false, false);
                        toolbar.setTitle("Categories");
                        floatingActionButton.setVisibility(View.GONE);

                        break;

                    case "Home":
                        searchItem.setVisible(true);
                        menuItemSort.setVisible(true);
                        toolbar.hideOverflowMenu();
                        toolbar.setTitle("Notepad Free");
                        floatingActionButton.setVisibility(View.VISIBLE);

                        break;

                    default:


                }


            }
        });

    }

    private void initDataCategory() {

        categories = new ArrayList<>();
        clickCategory = new IClickCategory() {
            @Override
            public void click(Category category, Boolean aBoolean) {

                if (aBoolean) {
                    categories.add(category);
                } else {
                    for (Category category1 : categories) {
                        if (category1.getNameCategory().trim().trim().equals(category.getNameCategory().trim())) {
                            categories.remove(category1);
                        }
                    }
                }
            }

            @Override
            public void readyCheck(Category category) {
            }
        };
        categoryArrayList = new ArrayList<>();
        adapterSelectCategory = new AdapterSelectCategory(categoryArrayList, clickCategory);

        dataViewModel.getAllListCategory();

        dataViewModel.getListCategory().observe(MainActivity.this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                categoryArrayList.clear();
                categoryArrayList.addAll(categories);
                adapterSelectCategory.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyTheme", Context.MODE_PRIVATE);
        valueTheme = sharedPreferences.getString("theme_system", "default");

        Log.d("kiemtratheme", valueTheme.toString());

        if (!valueTheme.equals("default")) {
            dataViewModel.setThemeString(valueTheme);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_select) {

            // guui su kien chon tata ca den cho fragmeent dang lang nghe
            SelectAll selectAll = new SelectAll(true, true);
            dataViewModel.setSelectAllMutableLiveData(selectAll);


//            Intent intent = new Intent(MainActivity.this, SelectAllActivity.class);
//            intent.putParcelableArrayListExtra("listData", noteArrayList);
//
//            startActivityForResult(intent, 20);

//            Toast.makeText(this, "itemImportText", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_import) {
            Toast.makeText(this, "itemSelectAll", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_export) {
        } else if (id == R.id.sort_menu_item) {
            Toast.makeText(this, "Sort", Toast.LENGTH_SHORT).show();
            showDialogSort();
        } else if (id == R.id.menu_category_item) {
            if (checkSelected) {
                SelectAll selectAll = new SelectAll(false, false);
                dataViewModel.setSelectAllMutableLiveData(selectAll);
            } else {
                SelectAll selectAll = new SelectAll(true, true);
                dataViewModel.setSelectAllMutableLiveData(selectAll);
            }

        } else if (id == R.id.menu_trash_item) {
            dataViewModel.setOptionString("Trash");
        } else if (id == R.id.nav_category_select) {
            showDialogCategory(MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
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


        tvOkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("óhdfhashfa", noteArrayListSelected.size() + " " + categories.size());
                dataViewModel.updateNoteCategoryList(noteArrayListSelected, categories);
                Toast.makeText(context, "Cập nhật thể loại thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        tvCancelCategory.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflate menu resource file.

// su ly toolbar kkhi nguoi dung pick item note
//        =======================================================================

        if (checkSelected) {
            getMenuInflater().inflate(R.menu.menu_option_selected, menu);
//              searchItem.setVisible(false);

            hideIcon();

            getMenuInflater().inflate(R.menu.menu_category, menu);
            MenuItem menuCategory = menu.findItem(R.id.menu_category_item);
            Drawable icCategory = menuCategory.getIcon();
            icCategory.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            menuCategory.setIcon(icCategory);

            searchItem = menu.findItem(R.id.item_search);

            getMenuInflater().inflate(R.menu.menu_trash, menu);
            MenuItem menuTrash = menu.findItem(R.id.menu_trash_item);
            // thay doi mau cua icon
            Drawable icon = menuTrash.getIcon();
            icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            menuTrash.setIcon(icon);


            // Thay doi mau sac cho icon back
            Drawable navigationIcon = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
            if (navigationIcon != null) {
                // Thiết lập màu cho Drawable
                navigationIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

                // Đặt Drawable đã tùy chỉnh làm NavigationIcon cho Toolbar
                toolbar.setNavigationIcon(navigationIcon);
            }


            if (checkSelected) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("sdodfasofaa", "safasffa");
                        // thoat khoi che do select va select all
                        SelectAll selectAll = new SelectAll(false, false);
                        dataViewModel.setSelectAllMutableLiveData(selectAll);
                        checkSelected = false;
                        invalidateOptionsMenu();

                    }
                });
            }

        }
//         =======================================================================
        else {
            getMenuInflater().inflate(R.menu.menu_option, menu);
            getMenuInflater().inflate(R.menu.searchview, menu);
            getMenuInflater().inflate(R.menu.sort_menu, menu);

            toggle.setDrawerIndicatorEnabled(true);
            toolbar.setTitle("Notepad Free");


            toolbar.getNavigationIcon().setVisible(false, false);

            searchItem = menu.findItem(R.id.item_search);

            SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

            searchView = null;

//              toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View view) {
//                      // Xử lý sự kiện khi click vào nút Navigation
//                      // Ví dụ: Mở ra menu Navigation
//                      drawerLayout.openDrawer(GravityCompat.START); // Mở menu Navigation từ bên trái
//                  }
//              });


            // doii mauu cho iconn search
            searchView = (SearchView) searchItem.getActionView();

            ImageView imageView = searchView.findViewById(androidx.appcompat.R.id.search_button);
            imageView.setColorFilter(Color.WHITE);

            // thhay dổi màu cuẩ menu item sort
            menuItemSort = menu.findItem(R.id.sort_menu_item);
            SpannableString spannableString = new SpannableString(menuItemSort.getTitle());
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 0, spannableString.length(), 0);
            menuItemSort.setTitle(spannableString);


            // Đặt sự kiện cho nút Navigation trên toolbar
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Nếu DrawerLayout đang mở, đóng nó lại
                    if (isDrawerOpen) {
                        Log.d("sfsadfasf", "dong");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        isDrawerOpen = false;
                    } else { // Nếu DrawerLayout đang đóng, mở nó ra
                        Log.d("sfsadfasf", "mo");
                        drawerLayout.openDrawer(GravityCompat.START);
                        isDrawerOpen = true;
                    }
                }
            });

            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(MainActivity.this, "mO", Toast.LENGTH_SHORT).show();
                    toggle.getDrawerArrowDrawable().setVisible(false, false);
                    menuItemSort.setVisible(false);
                }
            });


            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //do what you want  searchview is not expanded
                    Toast.makeText(MainActivity.this, "dONG", Toast.LENGTH_SHORT).show();

                    toggle.getDrawerArrowDrawable().setVisible(true, true);
                    menuItemSort.setVisible(true);

                    return false;
                }
            });


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Handle search query submission

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    dataViewModel.setStringMutableLiveData(newText.trim());
                    // list result search
                    ArrayList<Note> noteNewArrayList = new ArrayList<>();
                    // xét cho nó list mới là xong
                    if (newText.trim().length() > 0) {
                        int count = 0;
                        // check xem co tim ra hay khong
                        Boolean check = false;
                        for (Note note : noteArrayList) {
                            count++;
                            if (note.getTitle().contains(newText.trim().toLowerCase()) || note.getTitle().contains(newText.trim().toUpperCase())) {
                                check = true;
                                noteNewArrayList.add(note);
                                dataViewModel.setListMutableLiveData(noteNewArrayList);
                            }
                        }
                        if (count == noteArrayList.size() && !check) {
                            // neu khong tim thay set mang ve empty
                            ArrayList<Note> notes = new ArrayList<>();
                            dataViewModel.setListMutableLiveData(notes);
                        }
                    } else if (newText.trim().length() == 0) {
                        Log.d("fasfa", noteArrayList.size() + "");
                        dataViewModel.setListMutableLiveData(noteArrayList);
                    }
                    // Handle search query text change
                    return false;
                }
            });


        }


        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 10) {

            if (data != null) {


                Note note = (Note) data.getParcelableExtra("note");

                Log.d("fasfasf", note.getStyleBold());
                dataViewModel.addNote(note);

                dataViewModel.setMutableLiveDataNote(note);

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // menu drawer selector
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id > 0 && id <= itemId) {
            Log.d("Fsfsafafsa", item.getTitle().toString() + " ");

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment(String.valueOf(id), item.getTitle().toString())).commit();

        } else {
            if (id == R.id.fragmentHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(iStateList)).commit();
            } else if (id == R.id.nav_setting_theme) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();

            } else if (id == R.id.nav_editCategory) {
                Toast.makeText(this, "HHHH", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // dialog sap xep

    private void showDialogSort() {

        TextView tvCancle, tvSort;

        dialog.setContentView(R.layout.sort_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        tvCancle = dialog.findViewById(R.id.tvCancle);
        tvSort = dialog.findViewById(R.id.tvSort);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);


        // xap xep theo tieu de
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.title_AtoZ) {
                selector = "TitleAtoZ";

            } else if (checkedId == R.id.title_newest) {
                selector = "title_newest";

            } else if (checkedId == R.id.title_ZtoA) {
                selector = "TitleZtoA";
            } else if (checkedId == R.id.title_oldest) {
                selector = "title_oldest";
            }

        });

        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataViewModel.setOnSelectedSort(selector);

                dialog.dismiss();

            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Toast.makeText(MainActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();

    }


    void hideIcon() {
        // an toggle

        toggle.setDrawerIndicatorEnabled(false);


    }


}