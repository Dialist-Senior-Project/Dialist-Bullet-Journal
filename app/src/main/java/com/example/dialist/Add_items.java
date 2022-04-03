package com.example.dialist;

import static android.view.Window.FEATURE_NO_TITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Add_items extends AppCompatActivity {

    int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_add_items);

        /*********************/
        /****강종되는 지점 ****/
        /*********************/
        Button button = findViewById(R.id.btn_insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 화면에 실제 아이템 추가

                Page_2 pg2 = (Page_2) getSupportFragmentManager().findFragmentById(R.id.p_layout);
                pg2.insertItem(selected);  // 여기서 강종됨

                // 테스트 해보니 값 가져오는것도 안됨
                //pg2 = (Page_2) getSupportFragmentManager().findFragmentById(R.id.p_layout);
                //float tt = pg2.oriX;
                //Toast.makeText(getApplicationContext(), String.valueOf(tt), Toast.LENGTH_LONG);

                finish();

                selected = 0;
            }
        });

        FrameLayout editbox = findViewById(R.id.tch_txtbox);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 1;
                editbox.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        ImageView editbox2 = findViewById(R.id.img_txtbox);
        editbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 1;
                editbox.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        TextView editbox3 = findViewById(R.id.tv_txtbox);
        editbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 1;
                editbox.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });

        FrameLayout checkbox = findViewById(R.id.tch_chkbox);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 2;
            }
        });

        FrameLayout paint = findViewById(R.id.tch_paint);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 3;
            }
        });

        FrameLayout pagelink = findViewById(R.id.tch_link);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 4;
            }
        });

        FrameLayout time = findViewById(R.id.tch_time);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 5;
            }
        });

        FrameLayout table = findViewById(R.id.tch_table);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 6;
            }
        });

        FrameLayout list = findViewById(R.id.tch_list);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 7;
            }
        });

        FrameLayout grid = findViewById(R.id.tch_grid);
        editbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 8;
            }
        });

        //Intent intent = new Intent();
        //finish();
    }

    /*public void istitem(int sel) {
        EditText editText = new EditText(Page_2.context_pg2);
        editText.setText("입력해주세요");
        //editText.setWidth(350);
        editText.setPadding(30, 10, 30, 10);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //params.leftMargin = 100;

        editText.setLayoutParams(params);
        editText.setBackgroundColor(Color.rgb(255,255,255));
        //editText.setShowSoftInputOnFocus(false);

        Page_2 pg2 = (Page_2) getSupportFragmentManager().findFragmentById(R.id.p_layout);
        pg2.layout.addView(editText);

        editText.setClickable(true);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.context_first).mPager.setUserInputEnabled(false);

                    pg2.relatValX = event.getX();
                    pg2.relatValY = event.getY();
                    editText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            pg2.longTch = true;
                            return false;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    pg2.longTch = false;
                    // 편집가능?
                    ((First)First.context_first).mPager.setUserInputEnabled(true);
                    //editText.setShowSoftInputOnFocus(true);
                } //editText.setOnEditorActionListener();

                if (pg2.longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        //v.setX(event.getRawX() - relatValX);
                        //v.setY(event.getRawY() - (relatValY + v.getHeight()));
                        pg2.oriX = event.getRawX() - pg2.relatValX;
                        pg2.oriY = event.getRawY() - (pg2.relatValY + v.getHeight());
                        if (pg2.oriX % 50 < 25) {
                            v.setX(pg2.oriX - (pg2.oriX % 50));
                        }
                        if (pg2.oriY % 50 < 25) {
                            v.setY(pg2.oriY - (pg2.oriY % 50));
                        }
                    }
                }
                return false;
            }
        });
    }*/
}