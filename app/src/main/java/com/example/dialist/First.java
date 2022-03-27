package com.example.dialist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.content.Context;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class First extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private EditText mEditText;
    private View mDrawer;
    String email;
    int mChecked = 0;

    public static Context mContext;

    public static ViewPager2 mPager;
    private static FragmentStateAdapter pagerAdapter;
    public static int num_page = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //사용자 정보 받아오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();

                String uid = profile.getUid();

                String name = profile.getDisplayName();
                email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
        }

        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (View) findViewById(R.id.drawer);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // 드로어 닫음
        (findViewById(R.id.dw_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerLayout.setDrawerListener(listener);
        mDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 드로어 메뉴 1
        (findViewById(R.id.dw_thema1)).setOnClickListener(view -> {
            ((ConstraintLayout) findViewById(R.id.first_layout)).setBackgroundResource(android.R.color.white);
            ((Toolbar) findViewById(R.id.toolbar)).setBackgroundResource(android.R.color.black);
        });
        (findViewById(R.id.dw_thema2)).setOnClickListener(view -> {
            ((ConstraintLayout) findViewById(R.id.first_layout)).setBackgroundColor((Color.parseColor("#FFE8A1")));
            ((Toolbar) findViewById(R.id.toolbar)).setBackgroundColor((Color.parseColor("#3F2424")));
        });
        (findViewById(R.id.dw_themaStore)).setOnClickListener(view -> {
            Intent thema_intent = new Intent(getApplicationContext(), Store.class);
            startActivity(thema_intent);
        });
        (findViewById(R.id.dw_fontStore)).setOnClickListener(view -> {
            Intent font_intent = new Intent(getApplicationContext(), Store.class);
            startActivity(font_intent);
        });

        ((Switch)findViewById(R.id.dw_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) mChecked = 1;
                else           mChecked = 0;
            }
        });

        // 드로어 메뉴 2
        (findViewById(R.id.dw_changePwd)).setOnClickListener(view -> {
            Intent pwd_intent = new Intent(getApplication(), PasswordReset.class);
            startActivity(pwd_intent);
            mDrawerLayout.closeDrawers();
        });
        (findViewById(R.id.dw_logout)).setOnClickListener(view -> {
            Intent logout_intent = new Intent(getApplication(), Really_Delete_email.class);
            logout_intent.putExtra("deleteorlogout", "logout");
            startActivity(logout_intent);
        });
        (findViewById(R.id.dw_terms)).setOnClickListener(view -> {
            View dialogView = getLayoutInflater().inflate(R.layout.activity_terms, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            ((TextView)findViewById(R.id.tm_txtText)).setMovementMethod(new ScrollingMovementMethod());

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int pos) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        (findViewById(R.id.dw_review)).setOnClickListener(view -> {
            Intent review_intent = new Intent(Intent.ACTION_VIEW);
            review_intent.addCategory(Intent.CATEGORY_DEFAULT);
            review_intent.setData(Uri.parse("market://details?id=jp.naver.line.android"));
            startActivity(review_intent);
        });
        (findViewById(R.id.dw_deleteAcc)).setOnClickListener(view -> {
            Intent delete_intent = new Intent(First.this, delete_email_enter_password.class);
            delete_intent.putExtra("email", email);
            startActivity(delete_intent);
        });

        // 툴바 메뉴
        // basic
        /*
        (findViewById(R.id.ab_bookmark)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_star)).setOnClickListener(view -> {

        });
*/

        // readmode
        (findViewById(R.id.ab_menu)).setOnClickListener(view -> {
            mDrawerLayout.openDrawer(mDrawer);
        });

        (findViewById(R.id.ab_editmode)).setOnClickListener(view -> {

            findViewById(R.id.ab_editoff).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_add).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_allpage).setVisibility(View.VISIBLE);

            findViewById(R.id.ab_menu).setEnabled(false);
            findViewById(R.id.ab_editText).setVisibility(View.GONE);
            findViewById(R.id.ab_search).setVisibility(View.GONE);
            findViewById(R.id.ab_editmode).setVisibility(View.GONE);
            findViewById(R.id.ab_share).setVisibility(View.GONE);

            pagerAdapter = new PageAdapter(this, num_page, 1);
            mPager.setAdapter(pagerAdapter);
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            mPager.setCurrentItem(num_page-1);
        });
        /*
        (findViewById(R.id.ab_share)).setOnClickListener(view -> {

        });

 */
        (findViewById(R.id.ab_search)).setOnClickListener(view -> {
            mEditText = (EditText) findViewById(R.id.ab_editText);
        });

        // editmode
        (findViewById(R.id.ab_editoff)).setOnClickListener(view -> {
            findViewById(R.id.ab_editoff).setVisibility(View.GONE);
            findViewById(R.id.ab_add).setVisibility(View.GONE);
            findViewById(R.id.ab_allpage).setVisibility(View.GONE);

            findViewById(R.id.ab_menu).setEnabled(true);
            findViewById(R.id.ab_editText).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_search).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_editmode).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_share).setVisibility(View.VISIBLE);

            pagerAdapter = new PageAdapter(this, num_page, 0);
            mPager.setAdapter(pagerAdapter);
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            mPager.setCurrentItem(num_page-1);
        });

        /*
        (findViewById(R.id.ab_add)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_allpage)).setOnClickListener(view -> {

        });
*/


        //페이지 넘기기
        mPager = findViewById(R.id.viewpager);
        pagerAdapter = new PageAdapter(this, num_page, 0);
        mPager.setAdapter(pagerAdapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager.setCurrentItem(0); //시작 지점
        mPager.setOffscreenPageLimit(num_page); //최대 이미지 수

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                    if (position >= (num_page)) {
                        //새 페이지 추가 하실??
                        Intent newpage_intent = new Intent(getApplication(), AddNewPage.class);
                        startActivity(newpage_intent);
                        mPager.setCurrentItem(num_page-1);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}