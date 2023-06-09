package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity0 extends AppCompatActivity {

    private EditText txtNick;
    private Button btnNick;
    private TextView txtWarning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question0);

        txtNick = findViewById(R.id.txtNickname);
        txtWarning = findViewById(R.id.txtWarning);
        btnNick = findViewById(R.id.btnNext);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        btnNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtNick.getText().toString();
                String previousActivityClassName = "QuestionActivity0";
                Intent intent = new Intent(QuestionActivity0.this, QuestionActivity1.class);
                intent.putExtra("previous_activity", previousActivityClassName);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);
            }
        });

        String previousActivityClassName = getIntent().getStringExtra("previous_activity");

        //프로그래스바 애니메이션
        if("LoginActivity".equals(previousActivityClassName)){
            ProgressBar pb = findViewById(R.id.progressbarPercent);
            pb.setMax(100);
            pb.setProgress(0);
            ObjectAnimator animation = ObjectAnimator.ofInt(pb,"progress",0,20);
            animation.setDuration(1000);
            animation.start();
        }

        else{
            ProgressBar pb = findViewById(R.id.progressbarPercent);
            pb.setMax(100);
            pb.setProgress(0);
            ObjectAnimator animation = ObjectAnimator.ofInt(pb,"progress",40,20);
            animation.setDuration(1000);
            animation.start();
        }

        // 정보 입력 안됐으면 버튼 비활성화`
        // 닉네임 규칙 한글 2글자, 영문 4글자, 한영혼합 시 4글자 이상
        NicknameTextWatcher textWatcher = new NicknameTextWatcher(txtNick,btnNick,txtWarning);
        txtNick.addTextChangedListener(textWatcher);

        //버튼 클릭 시 색상 변경
        btnNick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (btnNick.isEnabled()) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        btnNick.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        btnNick.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    }
                }
                return false;
            }
        });
    }

    private long backKeyPressedTime = 0;
    private Toast toast;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }

    private class NicknameTextWatcher implements TextWatcher{
        private EditText txtNick;
        private Button btnNick;
        private TextView txtWaring;

        public NicknameTextWatcher(EditText txtNick, Button btnNick, TextView txtWaring){
            this.txtNick = txtNick;
            this.btnNick = btnNick;
            this.txtWaring = txtWaring;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {        }
        @Override
        public void afterTextChanged(Editable s){
            String inputText = txtNick.getText().toString();
            Drawable enabledButtonBackground = ResourcesCompat.getDrawable(getResources(),R.drawable.roundshape_white_button,null);
            Drawable disnabledButtonBackground = ResourcesCompat.getDrawable(getResources(),R.drawable.roundshape_grey_button,null);

            // 한글 2글자, 영문 4글자, 한영혼합시 4글자 이상으로 규칙설정
            if(inputText.matches("^[가-힣]{2,}|[a-zA-Z]{4,}|[가-힣a-zA-Z]{4,}$")){
                btnNick.setBackground(enabledButtonBackground);
                btnNick.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                txtWaring.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                btnNick.setEnabled(true);
            } else {
                btnNick.setBackground(disnabledButtonBackground);
                btnNick.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtWaring.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                btnNick.setEnabled(false);
            }
        }
    }
}