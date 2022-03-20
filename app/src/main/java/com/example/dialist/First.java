package com.example.dialist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class First extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ActionBar actionBar;

    String email;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //MainActivity에서 email 받아오기
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        mContext = this;

        // 툴바
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
                switch(item.getItemId()){
                    case R.id.nav_1: // 비밀번호 변경
                        Intent intent1 = new Intent(getApplication(), PasswordReset.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_2: // 로그아웃
                        Intent intent2 = new Intent(getApplication(), Really_Delete_email.class);
                        intent2.putExtra("deleteorlogout", "logout");
                        startActivity(intent2);
                        return true;
                    case R.id.nav_3: // 서비스 약관
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_4: // 리뷰 남기기
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_5: // 계정 삭제
                        Intent intent3 = new Intent(First.this, delete_email_enter_password.class);
                        intent3.putExtra("email", email);
                        startActivity(intent3);
                        return true;
                }
                return true;
            }
        });
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}