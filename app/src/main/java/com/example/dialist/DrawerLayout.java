package com.example.dialist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DrawerLayout extends AppCompatActivity {
    private EditText edittext;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


    }

    // drawer_layout
    public void setThema1(View v) {
        final ConstraintLayout backGround = (ConstraintLayout) findViewById(R.id.const_layout);
        backGround.setBackgroundResource(android.R.color.white);
        final Toolbar backGround2 = (Toolbar) findViewById(R.id.toolbar);
        backGround2.setBackgroundResource(android.R.color.black);
    }
    public void setThema2(View v) {
        final ConstraintLayout backGround = (ConstraintLayout) findViewById(R.id.const_layout);
        backGround.setBackgroundColor(0xffe8a1);
        final Toolbar backGround2 = (Toolbar) findViewById(R.id.toolbar);
        backGround2.setBackgroundColor(0x3f2424);
    }
    public void showOthers(View v) {
        Intent intent = new Intent(getApplicationContext(), Store.class);
        startActivity(intent);
    }
    public void showOthers2(View v) {
        Intent intent = new Intent(getApplicationContext(), Store.class);
        startActivity(intent);
    }
    public void setFontSize(View v) {
        displayMessage("아직 미구현");
    }
    public void setMode(View v) {
        displayMessage("아직 미구현");
    }
    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}