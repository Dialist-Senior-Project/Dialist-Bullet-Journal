package com.example.dialist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Really_Delete_email extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_really_delete_email);

        Intent intent = getIntent();
        String deleteorlogout = intent.getExtras().getString("deleteorlogout");

        Button deleteOK = (Button)findViewById(R.id.deleteOK);
        Button deleteNO = (Button)findViewById(R.id.deleteNO);
        TextView textView = (TextView)findViewById(R.id.textView);

        if(deleteorlogout.equals("logout")){
            textView.setText("정말 로그아웃 하시겠습니까?");
        }

        deleteOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteorlogout.equals("delete")) {
                    mAuth.getCurrentUser().delete();
                }
                else if(deleteorlogout.equals("logout")){
                    FirebaseAuth.getInstance().signOut();
                }
                finishAffinity();
            }
        });

        deleteNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}