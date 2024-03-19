package com.example.notepad.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickLongTime;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterItemNoteCategory extends RecyclerView.Adapter<AdapterItemNoteCategory.ViewHolder> {

    private Context context ;
    private ArrayList<Note> noteArrayList ;
   private   IClickLongTime iClickLongTime ;

    public AdapterItemNoteCategory(Context context, ArrayList<Note> noteArrayList,  IClickLongTime  iClickLongTime) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.iClickLongTime = iClickLongTime;
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


        Note note = noteArrayList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickLongTime.click(note);
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle,  tvContent;
        private ImageView imageView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

            tvContent = itemView.findViewById(R.id.tvContent);


        }
    }
}
