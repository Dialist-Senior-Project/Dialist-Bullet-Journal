package com.example.dialist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PopupShare extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_share);

        Button pageNo = (Button) findViewById(R.id.pageback);

        pageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupShare.this, First.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        findViewById(R.id.selectPDF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent(PopupShare.this, Share.class);
                share_intent.putExtra("kind", 1);
                startActivity(share_intent);
                finish();
            }
        });

        findViewById(R.id.textPDF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent(PopupShare.this, Share.class);
                share_intent.putExtra("kind", 1);
                startActivity(share_intent);
                finish();
            }
        });

        findViewById(R.id.selectGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent(PopupShare.this, Share.class);
                share_intent.putExtra("kind", 2);
                startActivity(share_intent);
                finish();
            }
        });

        findViewById(R.id.textGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent(PopupShare.this, Share.class);
                share_intent.putExtra("kind", 2);
                startActivity(share_intent);
                finish();
            }
        });
    }
}