package com.example.dialist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import java.util.Iterator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    String enEmail;
    int mChecked = 0;
    int firsttoast = 0;

    public static Context mContext;

    public static ViewPager2 mPager;
    private static FragmentStateAdapter pagerAdapter;
    public static int num_page = 1;
    int now_page=1;

    private DatabaseReference mDatabase;
    public static Context context_first;

    static int draw = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        context_first = this;

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(enEmail);
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

        ((Switch) findViewById(R.id.dw_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) mChecked = 1;
                else mChecked = 0;
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
        });

        /*
        (findViewById(R.id.ab_share)).setOnClickListener(view -> {

        });

 */
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
            Toast.makeText(First.this, "그림판 끔", Toast.LENGTH_SHORT).show();
        });


        /*********************/
        /*****여기서 시작 *****/
        /*********************/
        (findViewById(R.id.ab_add)).setOnClickListener(view -> {
            Intent intent = new Intent(this, Add_items.class);
            startActivity(intent);
        });
        (findViewById(R.id.ab_allpage)).setOnClickListener(view -> {
            Intent intent = new Intent(First.this, AllPages.class);
            mStartForResult.launch(intent);
        });

        //손으로 그림 그리기(그림판)
        (findViewById(R.id.ab_drawbrush1)).setOnClickListener(view -> {
            draw=0;
            mPager.setUserInputEnabled(true);
            mPager.setCurrentItem(now_page-1, true);
            Toast.makeText(First.this, "그림판 끔", Toast.LENGTH_SHORT).show();
            findViewById(R.id.ab_drawbrush1).setVisibility(View.GONE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.VISIBLE);
            Toast.makeText(First.this, "now_page"+now_page, Toast.LENGTH_SHORT).show();
        });

        (findViewById(R.id.ab_drawbrush2)).setOnClickListener(view -> {
            draw=1;
            mPager.setUserInputEnabled(false);
            mPager.setCurrentItem(now_page-1, false);
            Toast.makeText(First.this, "그림판 킴", Toast.LENGTH_SHORT).show();
            findViewById(R.id.ab_drawbrush1).setVisibility(View.VISIBLE);
            findViewById(R.id.ab_drawbrush2).setVisibility(View.GONE);
            Toast.makeText(First.this, "now_page"+now_page, Toast.LENGTH_SHORT).show();
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

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

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

    // toolbar menu
    public void onClickBookmark(View view) {

    }

    public void onClickStar(View view) {

    }

    public void onClickMenu(View view) {
        mDrawerLayout.openDrawer(mDrawer);
    }

    public void onClickSearch(View view) {
        EditText mEditText = (EditText) findViewById(R.id.ab_editText);
    }

    public void onClickEdit(View view) {
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
    }

    public void onClickShare(View view) {
        if (mView == null) {
            displayMessage("Error: view==NULL");
            return;
        }

        mView.setDrawingCacheEnabled(true);
        mView.buildDrawingCache();
        Bitmap viewBitmap = Bitmap.createBitmap(mView.getDrawingCache());

        String fileStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "bulletjournal_capture_" + fileStamp + ".png";
        File mFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/BulletJournal");
        File mFile = new File(mFilePath, mFileName);
        displayMessage(mFile.getAbsolutePath());

        if (!mFilePath.exists()) {
            mFilePath.mkdirs();
            displayMessage("Success: 파일 경로 생성 완료");
        } else { // 확인용
            displayMessage("Check: 파일 경로 이미 존재함");
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, mFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Pictures/BulletJournal");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(uri, "w", null);
            if (pdf == null) {
                displayMessage("Error: 객체 null: 파일 저장 실패");
            } else {
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());

                viewBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                displayMessage("Success: 파일 저장 성공");

                mView.destroyDrawingCache();
                mView.setDrawingCacheEnabled(false);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFile.getAbsolutePath())));
            }
        } catch (Exception e) {
            displayMessage("Error: " + e);
            return;
        }
    }

    public void onClickRead(View view) {
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
    }

    public void onClickAddItem(View view) {

    }

    public void onClickAllPage(View view) {

    }

    // drawer menu
    public void onClickClose(View view) {
        mDrawerLayout.closeDrawers();
    }

    public void onClickThema1(View view) {
        ((ConstraintLayout) findViewById(R.id.first_layout)).setBackgroundResource(android.R.color.white);
        ((Toolbar) findViewById(R.id.toolbar)).setBackgroundResource(android.R.color.black);
    }

    public void onClickThema2(View view) {
        ((ConstraintLayout) findViewById(R.id.first_layout)).setBackgroundColor((Color.parseColor("#FFE8A1")));
        ((Toolbar) findViewById(R.id.toolbar)).setBackgroundColor((Color.parseColor("#3F2424")));
    }

    public void onClickThemaStore(View view) {
        Intent thema_intent = new Intent(getApplicationContext(), Store.class);
        startActivity(thema_intent);
    }

    public void onClickFontStore(View view) {
        Intent font_intent = new Intent(getApplicationContext(), Store.class);
        startActivity(font_intent);
    }

    public void onClickSwitch(View view) {
    }

    public void onClickChangePwd(View view) {
        Intent pwd_intent = new Intent(getApplication(), PasswordReset.class);
        startActivity(pwd_intent);
        mDrawerLayout.closeDrawers();
    }

    public void onClickLogout(View view) {
        Intent logout_intent = new Intent(getApplication(), Really_Delete_email.class);
        logout_intent.putExtra("deleteorlogout", "logout");
        startActivity(logout_intent);
    }

    public void onClickTerms(View view) {
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
    }

    public void onClickReview(View view) {
        Intent review_intent = new Intent(Intent.ACTION_VIEW);
        review_intent.addCategory(Intent.CATEGORY_DEFAULT);
        review_intent.setData(Uri.parse("market://details?id=jp.naver.line.android"));
        startActivity(review_intent);
    }

    public void onClickDeleteAcc(View view) {
        Intent delete_intent = new Intent(First.this, delete_email_enter_password.class);
        delete_intent.putExtra("email", email);
        startActivity(delete_intent);
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

        mDatabase.child("User").child(Email).child(String.valueOf(page)).child(Itemname).setValue(thing).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();

            }
        });
    }
}