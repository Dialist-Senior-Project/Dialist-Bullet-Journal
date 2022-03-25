package com.example.dialist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

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

    public static Context mContext;

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

        // 드로어 메뉴
        (findViewById(R.id.dw_thema1)).setOnClickListener(view -> {
            final ConstraintLayout backGround = (ConstraintLayout) findViewById(R.id.first_layout);
            backGround.setBackgroundResource(android.R.color.white);
            final Toolbar backGround2 = (Toolbar) findViewById(R.id.toolbar);
            backGround2.setBackgroundResource(android.R.color.black);
        });
        (findViewById(R.id.dw_thema2)).setOnClickListener(view -> {
            final ConstraintLayout backGround = (ConstraintLayout) findViewById(R.id.first_layout);
            backGround.setBackgroundColor(0xffe8a1);
            final Toolbar backGround2 = (Toolbar) findViewById(R.id.toolbar);
            backGround2.setBackgroundColor(0x3f2424);
        });
        (findViewById(R.id.dw_fontStore)).setOnClickListener(view -> {
            Intent store_intent = new Intent(getApplicationContext(), Store.class);
            startActivity(store_intent);
        });

        (findViewById(R.id.dw_changePwd)).setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplication(), PasswordReset.class);
            startActivity(intent1);
            mDrawerLayout.closeDrawers();
        });
        (findViewById(R.id.dw_logout)).setOnClickListener(view -> {
            Intent intent2 = new Intent(getApplication(), Really_Delete_email.class);
            intent2.putExtra("deleteorlogout", "logout");
            startActivity(intent2);
        });
        (findViewById(R.id.dw_terms)).setOnClickListener(view -> {

        });
        (findViewById(R.id.dw_review)).setOnClickListener(view -> {

        });
        (findViewById(R.id.dw_deleteAcc)).setOnClickListener(view -> {
            Intent intent3 = new Intent(First.this, delete_email_enter_password.class);
            intent3.putExtra("email", email);
            startActivity(intent3);
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
        });
        /*
        (findViewById(R.id.ab_share)).setOnClickListener(view -> {

        });

 */
        (findViewById(R.id.ab_search)).setOnClickListener(view -> {
            mEditText = (EditText)findViewById(R.id.ab_editText);
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

        });

        /*
        (findViewById(R.id.ab_add)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_allpage)).setOnClickListener(view -> {

        });
*/
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {}
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {}
        @Override
        public void onDrawerStateChanged(int newState) {}
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}