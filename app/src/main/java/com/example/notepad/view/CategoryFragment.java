package com.example.notepad.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.notepad.Adapter.AdapterCategory;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.ICategory;
import com.example.notepad.Model.Category;
import com.example.notepad.R;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.ViewModel.MyViewModelFactory;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class CategoryFragment extends Fragment   {
    View view;
    MaterialButton btnAddCaterory ;
    EditText edName ;

    DBManager databaseHandler;

    DataViewModel dataViewModel ;

    RecyclerView rcvCategory ;

    AdapterCategory adapterCategory ;
    ArrayList<Category> categoryArrayList ;
    Dialog dialog;
    ICategory iCategory ;

    LinearLayout linearLayout ;

    SharedPreferences sharedPreferences  ;

    String themeSystem ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_category, container, false);

        btnAddCaterory = view.findViewById(R.id.btnAddCaterory);
        rcvCategory = view.findViewById(R.id.rcvCategory);

        linearLayout = view.findViewById(R.id.layoutcategory);

        categoryArrayList = new ArrayList<>();
        dialog = new Dialog(getContext());
        iCategory = new ICategory() {
            @Override
            public void clickDelete(Category category) {
                deleteCate(category);
            }

            @Override
            public void clickUpdate(Category category) {
               updateCaate(category);
            }
        };

        adapterCategory = new AdapterCategory(getContext(),categoryArrayList, iCategory);

        sharedPreferences = getActivity().getSharedPreferences("MyTheme", getContext().MODE_PRIVATE);

        themeSystem = sharedPreferences.getString("MyTheme","Default");

        if(themeSystem.equals("Default"))
        {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.backgroundItem));
        }else {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));

        }


        LinearLayoutManager layoutManager  = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        rcvCategory.setAdapter(adapterCategory);
        rcvCategory.setLayoutManager(layoutManager);

        edName = view.findViewById(R.id.edNameCategory);

        databaseHandler = new DBManager(getActivity());
        databaseHandler.open();

        MyViewModelFactory factory = new MyViewModelFactory(databaseHandler);
        dataViewModel = new ViewModelProvider(requireActivity(),factory).get(DataViewModel.class);

        dataViewModel.getAllListCategory();

        dataViewModel.getListCategory().observe(requireActivity(), categories -> {
            categoryArrayList.clear();
            categoryArrayList.addAll(categories);
            adapterCategory.notifyDataSetChanged();
        });


        btnAddCaterory.setOnClickListener(v -> {
            Category category = new Category();
            category.setNameCategory(edName.getText().toString());
            edName.setText("");
            dataViewModel.addCategory(category);

        });

        return view ;

    }

    private void updateCaate(Category category) {

        EditText edName ;
        Button btnUpdate , btnCancle ;

        dialog.setContentView(R.layout.update_category_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnCancle = dialog.findViewById(R.id.btnCancle);
        edName = dialog.findViewById(R.id.edNameCategory);

         btnUpdate.setOnClickListener(view -> {
            Category category1 = new Category();
            category1.setIdCategory(category.getIdCategory());
            category1.setNameCategory(edName.getText().toString());

            dataViewModel.updateCategory(category1);
             Toast.makeText(getActivity(), "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
             dialog.dismiss();

         });

         btnCancle.setOnClickListener(view -> {
             dialog.dismiss();
         });

         dialog.show();


    }

    private void deleteCate(Category category) {

        MyDialog.showConfirmationDialog(getContext(), "Xác nhận xóa", "Bạn muốn xóa danh mục này ?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng chọn đồng ý

                 dataViewModel.removeCategory(category);
                 dialog.dismiss();
                Toast.makeText(getContext(), "Xóa thành công !", Toast.LENGTH_SHORT).show();

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng chọn hủy
                dialog.dismiss();
            }
        });


    }

}