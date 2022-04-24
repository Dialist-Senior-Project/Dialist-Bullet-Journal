package com.example.dialist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Share extends AppCompatActivity {
    public static final int REQUEST_PERMISSION = 11;
    public static int num_page = 1;

    private static int np = 1;
    private TextView mText;
    private LinearLayout mView;
    private DatabaseReference mDatabase;
    private Intent return_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_capture);

        // 권한 검사
        verifyStoragePermission();

        // 캡처 및 공유할 뷰
        mView = findViewById(R.id.capture_layout);
        mText = findViewById(R.id.capturetext);

        // 뷰 검사
        if (mView == null) {
            displayMessage("Fail: The View is NULL");
            //return_intent.putParcelableArrayListExtra("func", bm);
            setResult(RESULT_CANCELED, return_intent);
            finish();
        }

        // DB에 저장된 데이터 가져옴
        // 페이지 수
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(First.enEmail);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    num_page = Integer.parseInt(Objects.requireNonNull(dataSnapshot.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return_intent = new Intent();

        mText.setOnClickListener(view -> {
            String tmp;
            Bitmap tbm;
            String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            mView.setDrawingCacheEnabled(true);

            try {
                for (int i = 0; i < num_page; i++) {
                    mText.setText(String.valueOf(i + 1));
                    tmp = stamp + "(" + (i + 1) + ")";
                    //getViewInfo();
                    np = i;

                    tbm = createBitmap();
                    saveBitmap(tbm, tmp);

                    np = i + 1;
                }
                displayMessage("Success: Image Capture (" + np + ")");
            } catch (Exception e) {
                displayMessage("Error: Image Capture (" + np + ")\n:" + e);
            } finally {
                mView.setDrawingCacheEnabled(false);
                finish();
            }
        });
    }

    public Bitmap createBitmap() {
        //mView.setDrawingCacheEnabled(true);
        mView.buildDrawingCache();
        Bitmap viewBitmap = Bitmap.createBitmap(mView.getDrawingCache());
        mView.destroyDrawingCache();
        return viewBitmap;
    }

    public void saveBitmap(Bitmap viewBitmap, String filestamp) {
        //String fileStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String filename = "bulletjournal_capture_" + filestamp + ".png";
        File filepath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/BulletJournal");
        File file = new File(filepath, filename);
        displayMessage(file.getAbsolutePath());

        if (!filepath.exists()) {
            filepath.mkdirs();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BulletJournal");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(uri, "w", null);
            if (pfd == null) {
                displayMessage("Fall: The Object is NULL");
            } else {
                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                viewBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            }
        } catch (Exception e) {
            displayMessage("Error: " + e);
        }
        //finally {
            //mView.setDrawingCacheEnabled(false);
        //}
    }

    // 권한 확인
    public void verifyStoragePermission() {
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // 권한이 없으면 권한 요청
        if (permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionWrite != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    // DB에서 뷰를 가져옴
    //public void getViewInfo() {
    //}

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}