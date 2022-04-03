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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
    ViewGroup rootView;
    CheckBox checkBoxV;

    public Page_2(int i) {
        if(num_page<i) {
            pagenum = 0;
        }
        else{ pagenum=i; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.page_2, container, false);

        context_pg2 = getContext();

        /* ----- 임시 버튼 ----- */
        Button btntb = rootView.findViewById(R.id.btntb);
        btntb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEditbox();
            }
        });
        Button btncb = rootView.findViewById(R.id.btncb);
        btncb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCheckbox();
            }
        });
        /* -------------------- */

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
        createEditbox();
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
        EditText editText = new EditText(getContext());
        editText.setText("배치 후 터치하세요");
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                editText.requestFocus();
                editText.getShowSoftInputOnFocus();
            }
        });
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
                            return true;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longTch = false;
                    ((First)First.context_first).mPager.setUserInputEnabled(true);
                    //editText.setShowSoftInputOnFocus(true);
                } //editText.setOnEditorActionListener();

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
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


    public void createCheckbox() {
        CheckBox checkBox = new CheckBox(getContext());
        //LinearLayout checkbox = rootView.findViewById(R.id.a_checkbox);
        checkBox.setText("배치 후 터치하세요");
        //checkBox.setHint("배치 후 터치하세요");
        checkBox.setPadding(30, 10, 30, 10);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        checkBox.setLayoutParams(params);
        checkBox.setBackgroundColor(Color.rgb(255,255,255));

        thisLayout.addView(checkBox);

        checkBox.setClickable(true);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                Intent intent = new Intent(getContext(), edit_chkbox_content.class);
                startActivityForResult(intent, 110);
                //Toast.makeText(getContext(), "@@@@@@@@@", Toast.LENGTH_LONG).show();
                checkBoxV = checkBox;
            }
        });
        checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.context_first).mPager.setUserInputEnabled(false);

                    relatValX = event.getX();
                    relatValY = event.getY();
                    checkBox.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            longTch = true;
                            return true;
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longTch = false;
                    ((First)First.context_first).mPager.setUserInputEnabled(true);
                    //editText.setShowSoftInputOnFocus(true);
                } //editText.setOnEditorActionListener();

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String s = data.getStringExtra("cntt");
        //Toast.makeText(getContext(), "!!!!!", Toast.LENGTH_SHORT).show();
        checkBoxV.setText(s);
    }

    public void createPaint() {
        //
    }


    public void createPageLink() {
        //
    }


    public void createTime() {
        //
    }


    public void createTable() {
        //
    }


    public void createList() {
        //
    }


    public void createGrid() {
        //
    }
}
