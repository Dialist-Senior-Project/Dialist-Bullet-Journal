package com.example.dialist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class First extends AppCompatActivity {
    Toolbar mToolbar;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    ActionBar mActionBar;
    EditText mEditText;

    String email;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        this.InitializeLayout();
    }

    // 초기
    public void InitializeLayout() {
        //MainActivity에서 email 받아오기
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        mContext = this;

        // 툴바
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNavigationView = findViewById(R.id.navigationView);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);

        /*
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // basic
                    case R.id.ab_bookmark:
                        return;
                    case R.id.ab_star:
                        return;

                    // readmode
                    case R.id.ab_menu:
                        if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            mDrawerLayout.openDrawer(Gravity.RIGHT);
                        }
                        return;
                    case R.id.ab_editmode:
                        findViewById(R.id.ab_search).setVisibility(View.VISIBLE);
                        findViewById(R.id.ab_editoff).setVisibility(View.VISIBLE);
                        findViewById(R.id.ab_add).setVisibility(View.VISIBLE);
                        findViewById(R.id.ab_allpage).setVisibility(View.VISIBLE);

                        findViewById(R.id.ab_menu).setVisibility(View.GONE);
                        findViewById(R.id.ab_editmode).setVisibility(View.GONE);
                        findViewById(R.id.ab_share).setVisibility(View.GONE);
                        return;
                    case R.id.ab_share:
                        return;
                    case R.id.ab_search:
                        mEditText = (EditText) findViewById(R.id.ab_editText);

/*

                        if ( mEditText.getText.toString().length() == 0 ) {
                            return;
                        }
                        else {
                            return;
                        }

                        return;
                    case R.id.ab_editoff:
                        findViewById(R.id.ab_search).setVisibility(View.GONE);
                        findViewById(R.id.ab_editoff).setVisibility(View.GONE);
                        findViewById(R.id.ab_add).setVisibility(View.GONE);
                        findViewById(R.id.ab_allpage).setVisibility(View.GONE);

                        findViewById(R.id.ab_menu).setVisibility(View.VISIBLE);
                        findViewById(R.id.ab_editmode).setVisibility(View.VISIBLE);
                        findViewById(R.id.ab_share).setVisibility(View.VISIBLE);
                        return;
                    case R.id.ab_add:
                        return;
                    case R.id.ab_allpage:
                        return;
                    default:
                        return;
                }
            }
        });
*/
        // 네비 메뉴
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.nav_1: // 비밀번호 변경
                        Intent intent1 = new Intent(getApplication(), PasswordReset.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_2: // 로그아웃
                        Intent intent2 = new Intent(getApplication(), Really_Delete_email.class);
                        intent2.putExtra("deleteorlogout", "logout");
                        startActivity(intent2);
                        return true;
                    case R.id.nav_3: // 서비스 약관
                        return true;
                    case R.id.nav_4: // 리뷰 남기기
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


        // 툴바 메뉴
        // basic
        (findViewById(R.id.ab_bookmark)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_star)).setOnClickListener(view -> {

        });

        // readmode
        (findViewById(R.id.ab_menu)).setOnClickListener(view -> {
            if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.openDrawer(Gravity.RIGHT) ;
            }
        });
        (findViewById(R.id.ab_editmode)).setOnClickListener(view -> {
            findViewById(R.id.ab_editText).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_search).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_editoff).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_add).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_allpage).setVisibility(View.VISIBLE);

            findViewById(R.id.ab_menu).setEnabled(false);
            findViewById(R.id.ab_editmode).setVisibility(View.GONE);
            findViewById(R.id.ab_share).setVisibility(View.GONE);
        });
        (findViewById(R.id.ab_share)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_search)).setOnClickListener(view -> {
            mEditText = (EditText)findViewById(R.id.ab_editText);
/*
            if ( mEditText.getText.toString().length() == 0 ) {
                return;
            }
            else {
                return;
            }*/
        });

        // editmode
        (findViewById(R.id.ab_editoff)).setOnClickListener(view -> {
            findViewById(R.id.ab_editText).setVisibility(View.GONE);
            findViewById(R.id.ab_search).setVisibility(View.GONE);
            findViewById(R.id.ab_editoff).setVisibility(View.GONE);
            findViewById(R.id.ab_add).setVisibility(View.GONE);
            findViewById(R.id.ab_allpage).setVisibility(View.GONE);

            findViewById(R.id.ab_menu).setEnabled(false);
            findViewById(R.id.ab_editmode).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_share).setVisibility(View.VISIBLE);

            return;
        });
        (findViewById(R.id.ab_add)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_allpage)).setOnClickListener(view -> {

        });

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}