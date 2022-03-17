package com.example.dialist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signupButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.signupButton:
                    signUp();
                    break;
            }
        }
    };

    private void signUp(){
        String id=((EditText)findViewById(R.id.editTextID)).getText().toString();
        String password=((EditText)findViewById(R.id.editTextPassword1)).getText().toString();
        String passwordCheck=((EditText)findViewById(R.id.editTextPassword2)).getText().toString();

        if(id.length()>0 && password.length()>0 && passwordCheck.length()>0){
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(id,password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUp.this,"회원가입 성공", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    if(task.getException().toString()!=null)
                                        Toast.makeText(SignUp.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
                Toast.makeText(SignUp.this,"비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(SignUp.this, "아이디와 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
    }
}