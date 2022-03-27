package com.example.dialist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AddNewPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_new_page);

        Button pageOK = (Button)findViewById(R.id.pageOK);
        Button pageNO = (Button)findViewById(R.id.pageNO);

        pageOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                First.num_page++;
                finish();
                First.mPager.setCurrentItem(First.num_page-1);
            }
        });

        pageNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                First.mPager.setCurrentItem(First.num_page-1);
            }
        });
    }
}