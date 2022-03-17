package com.example.dialist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Button resetButton = (Button)findViewById(R.id.resetButton);
        EditText findID = (EditText)findViewById(R.id.findID);

        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.resetButton:
                        ResetPassword(findID.getText().toString());
                        break;
                }
            }
        });
    }

    private void ResetPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PasswordReset.this, "비밀번호 변경 이메일을 전송했습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(PasswordReset.this, "이메일을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}