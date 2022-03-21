package com.example.dialist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class delete_email_enter_password extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_email_enter_password);

        //First에서 email 받아오기
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        Button deleteOK = (Button)findViewById(R.id.deleteOK);
        Button deleteNO = (Button)findViewById(R.id.deleteNO);
        EditText password = (EditText)findViewById(R.id.password);

        deleteOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(email, password.getText().toString());
            }
        });

        deleteNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void signIn(String id, String password){
        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplication(), Really_Delete_email.class);
                            intent.putExtra("deleteorlogout","delete");
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(delete_email_enter_password.this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}