package com.example.notepad.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.ICategory;
import com.example.notepad.Model.Category;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    private Context context ;
    private ArrayList<Category> categoryArrayList ;

    ICategory iCategory ;


    public AdapterCategory(Context context, ArrayList<Category> categoryArrayList, ICategory iCategory) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.iCategory = iCategory ;
    }

    @NonNull
    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;

        view = inflater.inflate(R.layout.item_layout_category, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.ViewHolder holder, int position) {

        Category category = categoryArrayList.get(position);

        holder.tvName.setText(category.getNameCategory());

        holder.imgEdit.setOnClickListener(v -> {
           iCategory.clickUpdate(category);
        });

        holder.imgRemove.setOnClickListener(v -> {
          iCategory.clickDelete(category);
        });


    }

    @Override
    public int getItemCount() {

        return categoryArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName ;
        private ImageView imgEdit , imgRemove ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameCategory);
            imgEdit = itemView.findViewById(R.id.imgEditCategory);
            imgRemove = itemView.findViewById(R.id.imgDeleteCategory);


        }
    }
}
