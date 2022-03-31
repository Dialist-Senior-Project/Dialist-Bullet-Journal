package com.example.dialist;

import static com.example.dialist.First.num_page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;

public class Page_2 extends Fragment {
    int pagenum;

    //ConstraintLayout layout;
    ConstraintLayout thisLayout;
    float relatValX, relatValY;
    float oriX, oriY;
    boolean longTch = false;
    int sel = 0;
    public static Context context_pg2;
    int once = 0;

    public Page_2(int i) {
        if(num_page<i) {
            pagenum = 0;
        }
        else{ pagenum=i; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.page_2, container, false);

        context_pg2 = getContext();

        /* 임시 버튼!! */
        Button button = rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEditbox();
            }
        });

        thisLayout = rootView.findViewById(R.id.p_layout);
        thisLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisLayout.requestFocus();
            }
        });

        TextView pagenumtext = (TextView) rootView.findViewById(R.id.pagenumtext);
        pagenumtext.setText(String.valueOf(pagenum));

        return rootView;
    }


    public void insertItem(int sel) {
        Toast.makeText(getContext(), "함수 진입 성공", Toast.LENGTH_LONG).show();
        /*switch (sel) {
            case 1:
                Toast.makeText(getContext(), "1 전달됨", Toast.LENGTH_SHORT).show();
                createEditbox();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                Toast.makeText(getContext(), "예외 값", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void createEditbox() {
        Toast.makeText(getContext(), "아이템 만드는 중", Toast.LENGTH_SHORT).show();

        EditText editText = new EditText(getContext());
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

        thisLayout.addView(editText);

        editText.setClickable(true);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.context_first).mPager.setUserInputEnabled(false);

                    relatValX = event.getX();
                    relatValY = event.getY();
                    editText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            longTch = true;
                            return false;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longTch = false;
                    // 편집?
                    ((First)First.context_first).mPager.setUserInputEnabled(true);
                    //editText.setShowSoftInputOnFocus(true);
                } //editText.setOnEditorActionListener();

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        //v.setX(event.getRawX() - relatValX);
                        //v.setY(event.getRawY() - (relatValY + v.getHeight()));
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

}
