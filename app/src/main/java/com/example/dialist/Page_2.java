package com.example.dialist;

import static com.example.dialist.First.num_page;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;

public class Page_2 extends Fragment {
    int pagenum;

    float relatValX, relatValY;
    float oriX, oriY;

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

        ConstraintLayout layout = rootView.findViewById(R.id.p_layout);

        Button button = rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(getContext());
                editText.setText("입력해주세요.");


                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
                //params.leftMargin = 100;

                editText.setLayoutParams(params);
                editText.setBackgroundColor(Color.rgb(184,236,184));

                layout.addView(editText);

                editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            relatValX = event.getX();
                            relatValY = event.getY();
                        }
                        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
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
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //
                        }
                        return false;
                    }
                });
            }
        });

        TextView pagenumtext = (TextView) rootView.findViewById(R.id.pagenumtext);
        pagenumtext.setText(String.valueOf(pagenum));

        return rootView;
    }
}
