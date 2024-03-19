package com.example.notepad.Adapter;


import static com.example.notepad.R.*;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickUpdate;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder>  {

    private ArrayList<Note> noteArrayList;

    private ArrayList<Note> searchArrayList;
    private Context context;

    private IClickUpdate iClickUpdate;

    private String selectAll = "no";

    private String bgColorForAll= "";

    private ArrayList<Integer>  positionArray = new ArrayList<>();

    private GestureDetector gestureDetector;


    private String themeNote = "default";

    // day la mau xet cho cac item
    private String colorTheme = "#ffff99";

    private String searchText = new String();

    private ArrayList<Note> noteArrayList2 = new ArrayList<>() ;


    public AdapterNote(ArrayList<Note> noteArrayList, Context context, IClickUpdate iClickUpdate) {
        this.noteArrayList = noteArrayList;
        this.context = context;
        this.iClickUpdate = iClickUpdate;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;



            view = inflater.inflate(R.layout.item_layout_note, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("c",position + "" );

        if (searchText.length() > 0) {

            Note note = noteArrayList.get(position);

            holder.tvContent.setVisibility(View.VISIBLE);
            int index = note.getTitle().toString().indexOf(searchText);
            Log.d("searchjhh", note.getTitle());
            int index2 = note.getContent().indexOf(searchText);

            holder.tvTitle.setText(Html.fromHtml(note.getTitle()));
            holder.tvContent.setText(Html.fromHtml(note.getContent()));
            // số với chữ thì nó chỉ nhận số thôi


            while (index > 0) {

                SpannableStringBuilder sb = new SpannableStringBuilder(String.valueOf(note.getTitle()));

                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(191, 255, 0)); //specify color here
                sb.setSpan(fcs, index, index + searchText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                index = note.getTitle().indexOf(searchText, index + 1);
                holder.tvTitle.setText(sb);
            }

            if (index2 > 0) {
                SpannableStringBuilder sb = new SpannableStringBuilder(note.getContent());
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(191, 255, 0)); //specify color here
                sb.setSpan(fcs, index2, index2 + searchText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                index2 = note.getContent().indexOf(searchText, index2 + 1);
                holder.tvContent.setText(sb);
            }


//            holder.constraintLayout.setOnClickListener(v -> {
//                iClickUpdate.click(note);
//            });

        }
        else
        {

            Note note = noteArrayList.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvTime.setText(note.getTimeEdit());
            holder.tvContent.setVisibility(View.GONE);

//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    // Xử lý sự kiện long press ở đây
//                    iClickUpdate.enableMote();
//                    iClickUpdate.click(note,holder.constraintLayout);
//                }
//            });

//            holder.constraintLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                   return   gestureDetector.onTouchEvent(motionEvent);
//                }
//            });

            holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Log.d("keeieje","trfgs"+ note.getTitle());

                    iClickUpdate.enableMote();
                    iClickUpdate.click(note,holder.constraintLayout);

                    return true;
                }
            });

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickUpdate.click(note,holder.constraintLayout);

                }
            });

            if(noteArrayList2.size() > 0)
            {

                if(noteArrayList2.contains(note))
                {

                        Log.d("f38e97ee9wu",note.getTitle() + " " + note.getBgColors());
                        Drawable myIcon = AppCompatResources.getDrawable(context, drawable.border_selected);
                        GradientDrawable gradientDrawable = (GradientDrawable) myIcon;
                        gradientDrawable.setColor(Color.parseColor(note.getBgColors()));

                        // Thiết lập loại gradient
                        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

                        // Thiết lập màu gradient cho start, center và end
                        int startColor = Color.parseColor("#F5F1CE");
                        int centerColor = Color.parseColor("#E1CFAE");
                        int endColor = Color.parseColor("#CBB495");

                        // Thiết lập màu gradient cho start, center và end
                        gradientDrawable.setColors(new int[]{startColor, centerColor, endColor});

                        // Thiết lập hướng của gradient
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);

                        holder.constraintLayout.setBackground(myIcon);

//                    }else if(!note.getBgColors().toString().trim().equals("#FCFBCE")) {
//
//                        Drawable myIcon1 = AppCompatResources.getDrawable(context, drawable.border_selected);
//                        GradientDrawable gradientDrawable1 = (GradientDrawable) myIcon1;
//                        gradientDrawable1.setColor(Color.parseColor(note.getBgColors()));
//                        // Thiết lập hướng của gradient
//                        gradientDrawable1.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//                        holder.constraintLayout.setBackground(myIcon1);
//
//
                



                }
                else
                {
                    Drawable myIcon = AppCompatResources.getDrawable(context, drawable.border_radius);
                    GradientDrawable gradientDrawable = (GradientDrawable) myIcon;

                    // o theme mac dinh thi xet mau nhu bt
                    gradientDrawable.setStroke(3, Color.parseColor("#836E4C"));
                    gradientDrawable.setColor(Color.parseColor(note.getBgColors()));
                    holder.constraintLayout.setBackground(myIcon);
                }

            }
            else {
                Drawable myIcon = AppCompatResources.getDrawable(context, drawable.border_radius);
                GradientDrawable gradientDrawable = (GradientDrawable) myIcon;

                // o theme mac dinh thi xet mau nhu bt
                gradientDrawable.setStroke(3, Color.parseColor("#836E4C"));
                gradientDrawable.setColor(Color.parseColor(note.getBgColors()));
                holder.constraintLayout.setBackground(myIcon);
            }





            if(iClickUpdate != null)
            {
                holder.constraintLayout.setOnClickListener(v -> {
                    iClickUpdate.click(note,holder.constraintLayout);
                });
            }



         }

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvTime, tvContent;
        ConstraintLayout constraintLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (searchText.length() > 0) {
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvContent = itemView.findViewById(R.id.tvContent);
                constraintLayout = itemView.findViewById(R.id.itemLayout);

            } else {

                     tvTitle = itemView.findViewById(R.id.tvTitle);
                     tvTime = itemView.findViewById(R.id.tvTime);
                     tvContent = itemView.findViewById(R.id.tvContent);
                     constraintLayout = itemView.findViewById(R.id.itemLayout);

            }
        }
    }

    public void setFilter(ArrayList<Note> myNotes, String searchText) {
        searchArrayList = new ArrayList<>();
        searchArrayList.addAll(myNotes);
        this.searchText = searchText;
        notifyDataSetChanged();
    }

    public void setLayoutNote(String textTheme, String bgColor) {
        this.themeNote = textTheme;
        this.colorTheme = bgColor;
        notifyDataSetChanged();
    }

    public void selectAllNote(String selectAllNote)
    {
        this.selectAll = selectAllNote ;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<Note> notes)
    {
        this.noteArrayList2.clear();
        this.noteArrayList2.addAll(notes);

        notifyDataSetChanged();

    }


    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // Giảm giá trị brightness
        return Color.HSVToColor(hsv);
    }

    private int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.2f; // Tăng giá trị brightness
        return Color.HSVToColor(hsv);
    }


}
