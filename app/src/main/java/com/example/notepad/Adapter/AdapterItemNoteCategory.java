package com.example.notepad.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickLongTime;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterItemNoteCategory extends RecyclerView.Adapter<AdapterItemNoteCategory.ViewHolder> {

    private Context context ;
    private ArrayList<Note> noteArrayList ;
   private   IClickLongTime iClickLongTime ;

   private String category ;

    public AdapterItemNoteCategory(Context context, ArrayList<Note> noteArrayList,  IClickLongTime  iClickLongTime ,  String category ) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.iClickLongTime = iClickLongTime;
        this.category = category;
    }

    @NonNull
    @Override
    public AdapterItemNoteCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;



        view = inflater.inflate(R.layout.item_layout_category_note, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemNoteCategory.ViewHolder holder, int position) {

        String s = "Last edit : ";

        Note note = noteArrayList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(this.category);
        s +=  note.getTimeEdit() ;
        holder.tvTime.setText(s);

    //        holder.tvContent.setText(note.getContent());


//        for (Category item : note.getCategories())
//        {
//            s +=  item.getNameCategory() + " , ";
//        }
//        holder.tvContent.setText(s);
//
//        Log.d("fsfas232",s);


//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iClickLongTime.click(note);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle,  tvContent , tvTime;
        private ImageView imageView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);


        }
    }
}
