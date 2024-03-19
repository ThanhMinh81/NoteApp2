package com.example.notepad.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickSelect;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterSelect extends RecyclerView.Adapter<AdapterSelect.ViewHolder> {


    private ArrayList<Note> noteArrayList ;
    private Context context ;

    private IClickSelect iClickSelect ;

    public AdapterSelect(ArrayList<Note> noteArrayList, Context context, IClickSelect iClickSelect) {
        this.noteArrayList = noteArrayList;
        this.context = context;
        this.iClickSelect = iClickSelect;
    }

    @NonNull
    @Override
    public AdapterSelect.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view ;


        view = inflater.inflate(R.layout.item_layout_note, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelect.ViewHolder holder, int position) {


            Note note = noteArrayList.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvTime.setText(note.getTimeEdit());
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickSelect.select(note);
                }
            });

            if(note.getCheckSelect())
            {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.border_selected));
            }else {
                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.border_radius_select));

            }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle , tvTime  ;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            constraintLayout = itemView.findViewById(R.id.itemLayout);

        }
    }
}
