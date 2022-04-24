package com.example.dialist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Objects;

public class AllPages extends AppCompatActivity {
    public static final int REQUEST_PERMISSION = 11;
    public static int num_page = 1;

    private static int np = 1;
    private TextView mText;
    private LinearLayout mView;
    private DatabaseReference mDatabase;
    private GridView gridview = null;
    private GridViewAdapter adapter = null;
    private Intent return_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pages);

        // 각 메뉴로부터 구분 값 받아옴
        Intent intent = getIntent();
        num_page = intent.getIntExtra("pageinfo", 1);

        // 권한 검사
        verifyStoragePermission();

        // 캡처 및 공유할 뷰
        mView = findViewById(R.id.screenshot);
        mText = findViewById(R.id.sstext);

        // 뷰 검사
        if (mView == null) {
            displayMessage("Fail: The View is NULL");
            //return_intent.putParcelableArrayListExtra("func", bm);
            setResult(RESULT_CANCELED, return_intent);
            finish();
        }

        // DB에 저장된 데이터 가져옴
        // 페이지 수
        try {
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
        } catch(Exception e) {
            displayMessage("Error:" + e);
        }

        return_intent = new Intent();
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new GridViewAdapter();
        mText.setOnClickListener(view -> {
            Bitmap tbm;

            mView.setDrawingCacheEnabled(true);

            try {
                for (int i = 0; i < num_page; i++) {
                    mText.setText(String.valueOf(i + 1));
                    //getViewInfo();
                    np = i;
                    tbm = createBitmap();
                    adapter.addItem(new PagesInfo(String.valueOf(i + 1), tbm));
                    np = i + 1;
                }
                // 리스트뷰에 Adapter 설정
                gridview.setAdapter(adapter);

                displayMessage("Success: Image Capture (" + np + ")");
                mView.setDrawingCacheEnabled(false);

            } catch (Exception e) {
                displayMessage("Error: Image Capture (" + np + ")\n:" + e);
                setResult(RESULT_CANCELED, return_intent);
                mView.setDrawingCacheEnabled(false);
                finish();
            } finally {
                findViewById(R.id.screenshot).setVisibility(View.GONE);
            }
        });
    }

    public Bitmap createBitmap() {
        mView.buildDrawingCache();
        Bitmap viewBitmap = Bitmap.createBitmap(mView.getDrawingCache());
        mView.destroyDrawingCache();
        return viewBitmap;
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

    private void displayMessage(String message){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


    /* 그리드뷰 어댑터 */
    class GridViewAdapter extends BaseAdapter {
        ArrayList<PagesInfo> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PagesInfo item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final PagesInfo pagesinfo = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gridview_list_item, viewGroup, false);

                TextView page_tit = (TextView) convertView.findViewById(R.id.pageTitle);
                ImageView page_img = (ImageView) convertView.findViewById(R.id.pageImage);

                page_tit.setText(pagesinfo.getPageTitle());
                page_img.setImageBitmap(pagesinfo.getPageImage());
            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            convertView.setOnClickListener(view -> {
                return_intent.putExtra("pageinfo",  Integer.parseInt(pagesinfo.getPageTitle()));
                setResult(RESULT_OK, return_intent);
                finish();
            });
            return convertView;
        }
    }
}