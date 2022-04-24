package com.example.dialist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Iterator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class First extends AppCompatActivity {
    public static final int REQUEST_PERMISSION = 11;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private View mDrawer;
    private LinearLayout mView;

    String email;
    public static String enEmail;
    int mChecked = 0;
    int firsttoast = 0;

    public static Context mContext;

    public static ViewPager2 mPager;
    private static FragmentStateAdapter pagerAdapter;
    public static int num_page = 1;
    public static int now_page=1;

    private DatabaseReference mDatabase;
    //public static Context context_first;

    float relatValX, relatValY;
    float oriX, oriY;
    boolean longTch = false;
    public Context context2;
    ConstraintLayout thisLayout;
    int viewWidth, viewHeight;

    int cnt = 0;
    public static int brushcolor = -16777216;

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int resint = data.getIntExtra("sel", 0);
        Toast.makeText(getApplicationContext(), "sel == "+resint, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "pager == "+mPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

        //Long id = pagerAdapter.getItemId(1);
        thisLayout = findViewById(R.id.t_layout);

        //mPager.getId()
        FragmentManager fm = getSupportFragmentManager();
        //Page_2 pg = (Page_2) fm.findFragmentByTag("page2");
        //Page_2 pg = new Page_2("k", 0);
        fm.beginTransaction()
                //.replace(mPager.getCurrentItem(), pg)
                //.add(pg, "page2")
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        //getSupportFragmentManager().executePendingTransactions();
        //fm.executePendingTransactions();


        //pg.createEditbox();
        createEditbox();
    }*/

    static int draw = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        thisLayout = findViewById(R.id.t_layout);

        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        Point size = new Point();
        display.getSize(size);
        viewWidth = size.x;
        viewHeight = size.y;

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

        //만약 처음이라면 DB에 첫장이 저장된다.
        enEmail = email.replace(".", ",");
        DB(enEmail, 1, "Notthing", 0, 0, 0, 0, "blank");

        //DB에 저장된 데이터를 불러오자.
        //일단 먼저 페이지가 몇장인지
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(enEmail).child("Page");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while (child.hasNext()) {
                    num_page = Integer.parseInt(child.next().getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //펜색 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(First.enEmail).child("BrushColors");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while (child.hasNext()) {
                    if(cnt==0){
                        brushcolor=Integer.parseInt(child.next().getValue().toString());
                    }
                    else {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //.getCurrentItem()//현재 페이지

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

        // 드로어 Close
        mDrawerLayout.setDrawerListener(listener);
        mDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 캡처 및 공유하기
        mView = (LinearLayout) findViewById(R.id.capture_layout);
        verifyStoragePermission(this);

        // 툴바 메뉴
        // basic
        (findViewById(R.id.ab_bookmark)).setOnClickListener(view -> {

        });
        (findViewById(R.id.ab_star)).setOnClickListener(view -> {

        });
        // readmode
        (findViewById(R.id.ab_menu)).setOnClickListener(view -> {
            thisLayout.setVisibility(View.GONE);
            mDrawerLayout.openDrawer(mDrawer);
        });

        (findViewById(R.id.ab_editmode)).setOnClickListener(view -> {
            findViewById(R.id.ab_editoff).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_add).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_allpage).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.VISIBLE);

            findViewById(R.id.ab_menu).setEnabled(false);
            findViewById(R.id.ab_editText).setVisibility(View.GONE);
            findViewById(R.id.ab_search).setVisibility(View.GONE);
            findViewById(R.id.ab_editmode).setVisibility(View.GONE);
            findViewById(R.id.ab_share).setVisibility(View.GONE);

            //findViewById(R.id.button3).setEnabled(true);

            pagerAdapter = new PageAdapter(this, num_page, 1);
            mPager.setAdapter(pagerAdapter);
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

            mPager.setCurrentItem(now_page-1, true);
        });
        (findViewById(R.id.ab_share)).setOnClickListener(view -> {
            Intent share_intent = new Intent(this, Share.class);
            startActivity(share_intent);
        });
        (findViewById(R.id.ab_search)).setOnClickListener(view -> {
            EditText mEditText = (EditText) findViewById(R.id.ab_editText);
        });

        // editmode
        (findViewById(R.id.ab_editoff)).setOnClickListener(view -> {
            mPager.setUserInputEnabled(true);

            findViewById(R.id.ab_editoff).setVisibility(View.GONE);
            findViewById(R.id.ab_add).setVisibility(View.GONE);
            findViewById(R.id.ab_allpage).setVisibility(View.GONE);
            findViewById(R.id.ab_drawbrush1).setVisibility(View.GONE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.GONE);

            findViewById(R.id.ab_menu).setEnabled(true);
            findViewById(R.id.ab_editText).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_search).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_editmode).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_share).setVisibility(View.VISIBLE);

            pagerAdapter = new PageAdapter(this, num_page, 0);
            mPager.setAdapter(pagerAdapter);
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

            mPager.setUserInputEnabled(true);
            mPager.setCurrentItem(0, true);

            mPager.setCurrentItem(now_page-1, true);
        });


        (findViewById(R.id.ab_add)).setOnClickListener(view -> {
            /*Page_2 pg = (Page_2) getSupportFragmentManager().findFragmentByTag("tag_page2");
            if(pg == null)
                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
            else
                pg.createEditbox();*/
            //BlankFragment b = BlankFragment.getInstance();
            //b.show(getSupportFragmentManager(), BlankFragment.TAG_EVENT_DIALOG);
            Intent intent = new Intent(this, Add_items.class);
            startActivity(intent);
        });


        (findViewById(R.id.ab_allpage)).setOnClickListener(view -> {
            Intent allpages_intent = new Intent(First.this, AllPages.class);
            allpages_intent.putExtra("pageinfo", num_page);
            resultLauncher.launch(allpages_intent);
        });

        // 드로어 메뉴
        (findViewById(R.id.dw_back)).setOnClickListener(view -> {
            mDrawerLayout.closeDrawers();
            thisLayout.setVisibility(View.VISIBLE);
        });

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
        (findViewById(R.id.dw_changePwd)).setOnClickListener(view -> {
            Intent pwd_intent = new Intent(getApplication(), PasswordReset.class);
            startActivity(pwd_intent);
            mDrawerLayout.closeDrawers();
            thisLayout.setVisibility(View.VISIBLE);
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

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int pos) {
                    dialog.dismiss();
                }
            });

            ((TextView) dialogView.findViewById(R.id.tm_txtText)).setMovementMethod(new ScrollingMovementMethod());

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
        (findViewById(R.id.dw_switch)).setOnClickListener(view -> {
        });

        //손으로 그림 그리기(그림판)
        (findViewById(R.id.ab_drawbrush1)).setOnClickListener(view -> {
            draw=0;
            mPager.setUserInputEnabled(true);
            mPager.setCurrentItem(now_page-1, true);
            Toast.makeText(First.this, "그림판 끔", Toast.LENGTH_SHORT).show();
            findViewById(R.id.ab_drawbrush1).setVisibility(View.GONE);
            findViewById(R.id.ab_brush_color).setVisibility(View.GONE);
            findViewById(R.id.ab_brush_eraser).setVisibility(View.GONE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_allpage).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_add).setVisibility(View.VISIBLE);
            Toast.makeText(First.this, "now_page"+now_page, Toast.LENGTH_SHORT).show();
        });

        (findViewById(R.id.ab_drawbrush2)).setOnClickListener(view -> {
            draw=1;
            mPager.setUserInputEnabled(false);
            mPager.setCurrentItem(now_page-1, false);
            Toast.makeText(First.this, "그림판 킴", Toast.LENGTH_SHORT).show();
            findViewById(R.id.ab_drawbrush1).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_brush_color).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_brush_eraser).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.GONE);
            findViewById(R.id.ab_allpage).setVisibility(View.GONE);
            findViewById(R.id.ab_add).setVisibility(View.GONE);
            Toast.makeText(First.this, "now_page"+now_page, Toast.LENGTH_SHORT).show();
        });

        (findViewById(R.id.ab_brush_color)).setOnClickListener(view -> {
            Intent intent = new Intent(First.this, PaletteBarSelect.class);
            startActivity(intent);
        });

        //페이지 넘기기
        mPager = findViewById(R.id.viewpager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout Loading = (LinearLayout) findViewById(R.id.Loadinglayout);
                Loading.setVisibility(View.INVISIBLE);
                Toast.makeText(First.this, "1", Toast.LENGTH_SHORT).show();
                int i = Loading();
            }
        }, 3000);

        pagerAdapter = new PageAdapter(this, num_page, 0);
        mPager.setAdapter(pagerAdapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager.setCurrentItem(0); //시작 지점
        mPager.setOffscreenPageLimit(num_page); //최대 이미지 수

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // 기존 페이지 내용 삭제
                //thisLayout.removeAllViews();
                // 해당 페이지 저장된 내용 불러오기

                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                    if (firsttoast != 0 && draw==0) {
                        now_page = position+1;
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), (position + 1) + "/" + num_page, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 20, 20);
                        toast.show();
                    }
                    firsttoast = 1;
                    if (position >= (num_page)) {
                        //새 페이지 추가 하실??
                        Intent intent = new Intent(First.this, AddNewPage.class);
                        intent.putExtra("state", 1);
                        mStartForResult.launch(intent);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }



    public void insertItem(int selected) {
        switch (selected) {
            case 1:
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                createEditbox();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                createCheckbox();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                createPaint();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
                createPageLink();
                break;
            case 5:
                Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_LONG).show();
                createTime();
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                Toast.makeText(getApplicationContext(), "예외 값", Toast.LENGTH_SHORT).show();
        }
    }


    public void createEditbox() {
        EditText editText = new EditText(getApplicationContext());
        editText.setPadding(30, 10, 30, 10);
        editText.setHint("배치 후 터치하세요");

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //params.leftMargin = 100;

        editText.setLayoutParams(params);
        editText.setBackgroundColor(Color.rgb(255,255,255));
        //editText.setShowSoftInputOnFocus(false);
        editText.setX(viewWidth/3);
        editText.setY(viewHeight/2);
        editText.setBackgroundResource(R.drawable.background);

        thisLayout.addView(editText);

        editText.setClickable(true);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.requestFocus();
                editText.getShowSoftInputOnFocus();
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.mContext).mPager.setUserInputEnabled(false);

                    relatValX = event.getX();
                    relatValY = event.getY();
                    editText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            longTch = true;
                            return true;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longTch = false;
                    ((First)First.mContext).mPager.setUserInputEnabled(true);
                    //editText.setShowSoftInputOnFocus(true);
                } //editText.setOnEditorActionListener();

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        oriX = event.getRawX() - relatValX;
                        oriY = event.getRawY() - (relatValY + v.getHeight());
                        if (oriX % 50 < 25) {
                            v.setX(oriX - (oriX % 50));
                        }
                        if (oriY % 50 < 25) {
                            v.setY(oriY - (oriY % 50));
                        }
                    }
                }
                return false;
            }
        });
    }

    public void createCheckbox() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(30, 20, 30, 20);
        linearLayout.setBackgroundColor(Color.rgb(255,255,255));
        linearLayout.setX(viewWidth/4);
        linearLayout.setY(viewHeight/2);
        linearLayout.setBackgroundResource(R.drawable.background);


        CheckBox checkBox = new CheckBox(linearLayout.getContext());
        checkBox.setText("");
        linearLayout.addView(checkBox);
        EditText editText = new EditText(linearLayout.getContext());
        editText.setHint("배치 후 클릭하세요.");
        linearLayout.addView(editText);

        thisLayout.addView(linearLayout);

        linearLayout.setClickable(true);
        linearLayout.setOnTouchListener(touchListener);
        /*editText.setClickable(true);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.mContext).mPager.setUserInputEnabled(false);

                    relatValX = event.getX();
                    relatValY = event.getY();
                    checkBox.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            longTch = true;
                            return true;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longTch = false;
                    ((First)First.mContext).mPager.setUserInputEnabled(true);
                }

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        oriX = event.getRawX() - relatValX;
                        oriY = event.getRawY() - (relatValY + v.getHeight());
                        if (oriX % 50 < 25) {
                            linearLayout.setX(oriX - (oriX % 50));
                        }
                        if (oriY % 50 < 25) {
                            linearLayout.setY(oriY - (oriY % 50));
                        }
                    }
                }
                return false;
            }
        });*/
    }

    public void createPaint() {
        Intent intent = new Intent(getApplicationContext(), PaintView.class);
        startActivity(intent);
    }

    public void createPageLink() {
        //
    }

    public void createTime() {
        Date now = Calendar.getInstance().getTime();
        String textFormat = "yyyy. M. d. EE.";
        String nowText = new SimpleDateFormat(textFormat, Locale.getDefault()).format(now);

        TextView textView = new TextView(getApplicationContext());
        textView.setText(nowText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundColor(Color.rgb(255,255,255));
        textView.setX(viewWidth/3);
        textView.setY(viewHeight/2);
        textView.setBackgroundResource(R.drawable.background);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(params);
        thisLayout.addView(textView);

        textView.setClickable(true);
        textView.setOnTouchListener(touchListener);
    }

    public void createTable() {
        //
    }

    public void createList() {
        //
    }

    public void createGrid() {
        //
    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ((First) mContext).mPager.setUserInputEnabled(false);
                relatValX = event.getX();
                relatValY = event.getY();
                view.setOnLongClickListener(longClickListener);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                longTch = false;
                //((First)First.context_first).mPager.setUserInputEnabled(true);
                ((First) mContext).mPager.setUserInputEnabled(true);
            }

            if (longTch) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    oriX = event.getRawX() - relatValX;
                    oriY = event.getRawY() - (relatValY + view.getHeight());
                    if (oriX % 50 < 25) {
                        view.setX(oriX - (oriX % 50));
                    }
                    if (oriY % 50 < 25) {
                        view.setY(oriY - (oriY % 50));
                    }
                }
            }
            return false;
        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            longTch = true;
            return false;
        }
    };

    public int getCurrentItemNum() { return mPager.getCurrentItem(); }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //thisLayout.setVisibility(View.GONE);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            thisLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        DB(enEmail, num_page, "Notthing", 0, 0, 0, 0, "blank");
                        pagerAdapter = new PageAdapter(this, num_page, 1);
                        mPager.setAdapter(pagerAdapter);
                        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        mPager.setCurrentItem(num_page - 1); //시작 지점
                        mPager.setOffscreenPageLimit(num_page); //최대 이미지 수
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        mPager.setCurrentItem(num_page - 1); //시작 지점
                    }
            });

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent re_intent = result.getData();
                    if (re_intent == null) {
                        displayMessage("Fail: The Data is NULL");
                        return;
                    }

                    int pageinfo = re_intent.getIntExtra("pageinfo", 1);
                    mPager.setCurrentItem(pageinfo - 1);
                    displayMessage("Success: Moving Pages");
                }
            });


    public int Loading(){
        pagerAdapter = new PageAdapter(this, num_page, 0);
        mPager.setAdapter(pagerAdapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(num_page - 1); //시작 지점
        mPager.setOffscreenPageLimit(num_page); //최대 이미지 수
        return 0;
    }

    //권한 확인
    public void verifyStoragePermission(AppCompatActivity Activity) {
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //권한이 없으면 권한 요청
        if (permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionWrite != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void DB (String Email, int page, String Itemname, int x, int y, int xx, int yy, String value) {

        //User 객체 만들기
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DB thing = new DB(x, y, xx, yy, value);

        mDatabase.child("User").child(Email).child("Page").child(String.valueOf(page)).child(Itemname).setValue(thing).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "DB성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "DB실패", Toast.LENGTH_SHORT).show();

            }
        });
    }
}