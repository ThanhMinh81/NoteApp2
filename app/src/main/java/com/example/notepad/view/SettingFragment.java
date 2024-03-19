package com.example.notepad.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.notepad.R;
import com.example.notepad.ViewModel.DataViewModel;

public class SettingFragment extends Fragment {

    private TextView tvTheme;
    private View view;

    Dialog dialog;

    DataViewModel  dataViewModel ;

    String selected ;

    String THEME = "default";

    SharedPreferences sharedPreferences  ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dialog = new Dialog(getContext());

        sharedPreferences = getActivity().getSharedPreferences("MyTheme", getContext().MODE_PRIVATE);



        tvTheme = view.findViewById(R.id.changeTheme);

        tvTheme.setOnClickListener(view -> {


        showDialogSort();

        });



        // Inflate the layout for this fragment
        return view;

    }

    private void showDialogSort() {


        TextView tvCancle, tvSort;

        dialog.setContentView(R.layout.theme_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        tvCancle = dialog.findViewById(R.id.tvCancle);
        tvSort = dialog.findViewById(R.id.tvupdate);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);



        // xap xep theo tieu de
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.theme_Solarized) {
                selected = "Solarized";
            } else if (checkedId == R.id.theme_Default) {

                selected = "Default";

            }

        });

        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // xoa tat ca
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyTheme", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorDelete = sharedPreferences.edit();
                editorDelete.clear();
                editorDelete.apply();

                // Them lai theme moi

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("theme_system", selected);
                editor.apply();

                dataViewModel.setThemeString(selected);
                dialog.dismiss();

            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

//                Toast.makeText(MainActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();

    }


}