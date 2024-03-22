package com.example.notepad;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    Button button ;

    Dialog dialog;

    ImageView imageView ;
    FrameLayout frameLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main2);
        setContentView(R.layout.layout_call_app);

        imageView  = this.<ImageView>findViewById(R.id.imageView);

        startAnimation(imageView);





//        button = this.<Button>findViewById(R.id.btnClick);
//
//
//        dialog = new Dialog(MainActivity2.this);
//
//        button.setOnClickListener(view -> {
//
//      showDialog();
//
//        });
    }


    public void startAnimation(View view) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        imageView.startAnimation(animation);
    }


    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this, R.style.WrapContentDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity2.this);
        final View dialogView = layoutInflater.inflate(R.layout.test2, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        dialog.show();

//        dialog.setContentView(R.layout.test2);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
//        dialog.show();

    }

}