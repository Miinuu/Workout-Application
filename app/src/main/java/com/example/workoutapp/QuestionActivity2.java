package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class QuestionActivity2 extends AppCompatActivity {

//    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        Button btnGym = findViewById(R.id.btnGym);
        Button btnHome = findViewById(R.id.btnHome);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userGender = intent.getStringExtra("userGender");
        String userName = intent.getStringExtra("userName");
        String gym = "헬스장";
        String home ="집";


        btnGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String previousActivityClassName = "QuestionActivity2";
                Intent intent = new Intent(QuestionActivity2.this, QuestionActivity3.class);
                intent.putExtra("previous_activity", previousActivityClassName);
                intent.putExtra("userID",userID);
                intent.putExtra("userGender",userGender);
                intent.putExtra("userLocation",gym);
                intent.putExtra("userName",userName);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String previousActivityClassName = "QuestionActivity2";
                Intent intent = new Intent(QuestionActivity2.this, QuestionActivity3.class);
                intent.putExtra("previous_activity", previousActivityClassName);
                intent.putExtra("userID",userID);
                intent.putExtra("userGender",userGender);
                intent.putExtra("userLocation",home);
                intent.putExtra("userName",userName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);
            }
        });


        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB 입력 수정해야 됌
                Intent intent = new Intent(QuestionActivity2.this,QuestionActivity1.class);
                //getIntent().getExtras().remove(userID);
                //getIntent().getExtras().remove(userGender);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
            }
        });

        //프로그래스바 애니메이션
        String previousActivityClassName = getIntent().getStringExtra("previous_activity");

        if("QuestionActivity1".equals(previousActivityClassName)) {
            ProgressBar pb = findViewById(R.id.progressbarPercent);
            pb.setMax(100);
            pb.setProgress(0);
            ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 40, 60);
            animation.setDuration(1000);
            animation.start();
        }else{
            ProgressBar pb = findViewById(R.id.progressbarPercent);
            pb.setMax(100);
            pb.setProgress(0);
            ObjectAnimator animation = ObjectAnimator.ofInt(pb,"progress",80,60);
            animation.setDuration(1000);
            animation.start();
        }
    }

    private long backKeyPressedTime = 0;
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            //-------------------------------------------------------back intent
            Intent backintent = getIntent();
            String userID = backintent.getStringExtra("userID");
            String userName = backintent.getStringExtra("userName");
            //-------------------------------------------------------------------

            Intent intent = new Intent(QuestionActivity2.this,QuestionActivity1.class);
            intent.putExtra("userID",userID);
            intent.putExtra("userName",userName);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
            backKeyPressedTime = System.currentTimeMillis();
            return;
        }
    }
}