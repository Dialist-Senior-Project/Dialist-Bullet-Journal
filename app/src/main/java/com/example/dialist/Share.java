package com.example.dialist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import java.util.Timer;
import java.util.TimerTask;

public class Share extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_PERMISSION = 11;
    public static int num_page = 1;

    private static int np = 1;
    private TextView mText;
    private LinearLayout mView;
    private DatabaseReference mDatabase;
    private Intent return_intent;
    int mSelect = -1;

    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_capture);

        // 캡처 및 공유할 뷰
        mView = findViewById(R.id.capture_layout);
        mText = findViewById(R.id.capturetext);

        // 각 메뉴로부터 구분 값 받아옴
        Intent intent = getIntent();
        mSelect = intent.getIntExtra("kind", 1);

        // 권한 검사
        verifyStoragePermission();

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

        mText.setOnClickListener(view -> saveFile(mSelect));

        mTimerTask = createTimerTask();
        mTimer.schedule(mTimerTask, 100, 1000);
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            saveFile(mSelect);
        }
    };

    private TimerTask createTimerTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count = count + 1;

                if (mTimerTask != null && count == 1) {
                    mTimerTask.cancel();
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        };
        return timerTask;
    }

    public void saveFile(int select) {
        String tmp;
        Bitmap tbm;
        String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        mView.setDrawingCacheEnabled(true);

        switch(select) {
            case 1: // PDF
                try {
                    Display display = getWindowManager().getDefaultDisplay();  // in Activity
                    Point size = new Point();
                    display.getRealSize(size); // or getSize(size)

                    /*
                    String filename = "/bulletjournal_" + stamp + ".pdf";
                    File file = new File( getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/BulletJournal", filename);
                    displayMessage(file.getAbsolutePath());
                    */
                    String filename = "bulletjournal_capture_" + stamp + ".pdf";
                    File filepath = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/BulletJournal");
                    File file = new File(filepath.getAbsolutePath(), filename);
                    displayMessage(file.getAbsolutePath());

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    }

                    PdfDocument document = new PdfDocument();

                    for (int i = 0; i < num_page; i++) {
                        mText.setText(String.valueOf(i + 1));
                        //getViewInfo();
                        np = i;

                        mView.buildDrawingCache();
                        tbm = Bitmap.createBitmap(mView.getDrawingCache());
                        mView.destroyDrawingCache();

                        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(size.x, size.y, i).create();
                        PdfDocument.Page page = document.startPage(pi);

                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.parseColor("#ffffff"));
                        canvas.drawPaint(paint);
                        paint.setColor(Color.BLUE);
                        canvas.drawBitmap(tbm, 0,0, null);
                        document.finishPage(page);
                        //savePDF(tbm, tmp);

                        np = i + 1;
                    }
                    document.writeTo(new FileOutputStream(file));
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filepath.getAbsolutePath())));
                    document.close();

                    displayMessage("Success: saving PDF");
                } catch (Exception e) {
                    displayMessage("Error: Image Capture (" + np + ")\nFail: saving PDF\n:" + e.getMessage());
                } finally {
                    mView.setDrawingCacheEnabled(false);
                    finish();
                }
                break;
            case 2: // Gallary
                try {
                    for (int i = 0; i < num_page; i++) {
                        mText.setText(String.valueOf(i + 1));
                        tmp = stamp + "(" + (i + 1) + ")";
                        //getViewInfo();
                        np = i;

                        mView.buildDrawingCache();
                        tbm = Bitmap.createBitmap(mView.getDrawingCache());
                        mView.destroyDrawingCache();

                        if(tbm == null) { throw new Exception(); }
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
                break;
            default:
                displayMessage("Fail: The Value is Difference");
                //return_intent.putParcelableArrayListExtra("func", bm);
                setResult(RESULT_CANCELED, return_intent);
                finish();
        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // DB에서 뷰를 가져옴
    //public void getViewInfo() {
    //}

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}