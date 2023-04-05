package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

public class QuestionActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question3);

        ProgressBar pb = findViewById(R.id.progressbarPercent);
        pb.setProgress(60);

        Button btnBegginer = findViewById(R.id.btnBegginer);
        Button btnLow = findViewById(R.id.btnLow);
        Button btnMiddle = findViewById(R.id.btnMiddle);
        Button btnHigh = findViewById(R.id.btnHigh);
        Button btnMaster = findViewById(R.id.btnMaster);

        Spannable spanBeginner = (Spannable) btnBegginer.getText();
        spanBeginner.setSpan(new RelativeSizeSpan(1.5f),2,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBeginner.setSpan(new StyleSpan(Typeface.BOLD),2,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable spanLow = (Spannable) btnLow.getText();
        spanLow.setSpan(new RelativeSizeSpan(1.5f),2,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanLow.setSpan(new StyleSpan(Typeface.BOLD),2,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable spanMiddle = (Spannable) btnMiddle.getText();
        spanMiddle.setSpan(new RelativeSizeSpan(1.5f),2,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanMiddle.setSpan(new StyleSpan(Typeface.BOLD),2,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable spanHigh = (Spannable) btnHigh.getText();
        spanHigh.setSpan(new RelativeSizeSpan(1.5f),2,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanHigh.setSpan(new StyleSpan(Typeface.BOLD),2,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable spanMaster = (Spannable) btnMaster.getText();
        spanMaster.setSpan(new RelativeSizeSpan(1.5f),2,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanMaster.setSpan(new StyleSpan(Typeface.BOLD),2,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    private long backKeyPressedTime = 0;
    private Toast toast;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            Intent intent = new Intent(QuestionActivity3.this,QuestionActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}