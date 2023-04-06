package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // 이메일 발송 메소드
    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "인증 이메일이 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd, mEtPwdCheck, mEtName, mEtPhone;
    private Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton btnCancle = findViewById(R.id.btnCancle);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
            }
        });

        // 회원가입 부분
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("workoutapp");

        mEtEmail = findViewById(R.id.txtEmail);
        mEtPwd = findViewById(R.id.txtPasswd);
        mEtPwdCheck = findViewById(R.id.txtPasswdCheck);
        mEtName = findViewById(R.id.txtName);
        mEtPhone = findViewById(R.id.txtPhone);

        mBtnRegister = findViewById(R.id.btnRegister);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtEmail.getText().toString().trim();
                String strPwd = mEtPwd.getText().toString().trim();
                String strPwdCheck = mEtPwdCheck.getText().toString().trim();
                String strName = mEtName.getText().toString().trim();
                String strPhone = mEtPhone.getText().toString().trim();

                //빈칸 알림
                if(strEmail.equals("")){
                    Toast.makeText(RegisterActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strPwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strPwdCheck.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(strName.equals("")){
                    Toast.makeText(RegisterActivity.this, "이름 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(strPwd.equals(strPwdCheck))){
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //암호화 코드
                String pwd = strPwd.trim();
                String salt = Encryption.getSalt();
                String pwdGetEncrypt = Encryption.getEncrypt(pwd, salt);

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, pwdGetEncrypt).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            sendVerificationEmail(); // 이메일 발송

                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(pwdGetEncrypt);
                            account.setName(strName);
                            account.setPhone(strPhone); // 확인 필요함 입력이 안되어 있을 시 빈문자열이 들어가는지
                            account.setSalt(salt);

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(RegisterActivity.this, "이미 회원가입 된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private long backKeyPressedTime = 0;
    private Toast toast;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}