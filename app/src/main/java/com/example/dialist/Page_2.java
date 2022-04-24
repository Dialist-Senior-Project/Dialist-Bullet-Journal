package com.example.dialist;

import static com.example.dialist.First.mContext;
import static com.example.dialist.First.num_page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Page_2 extends Fragment {
    int pagenum;
    private MyPaintView myView;

    ConstraintLayout thisLayout;
    float relatValX, relatValY;
    float oriX, oriY;
    boolean longTch = false;
    private int sel = 0;
    public static Context context_pg2;
    int once = 0;
    ViewGroup rootView;
    private View header;
    private Adapter adapter;

    public Page_2(String key, int currPg, int sel, Context ctx) {
        this.sel = sel;
        pagenum = currPg;
        createEditbox();
    }

    public Page_2(int i) {
        if(num_page<i) {
            pagenum = 0;
        }
        else{ pagenum=i; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.page_2, container, false);
        LayoutInflater inflater2 = getLayoutInflater();
        View v1 = inflater2.inflate(R.layout.page_2, null);

        myView = new MyPaintView(getActivity().getApplicationContext());
        ((LinearLayout) rootView.findViewById(R.id.paintLayout)).addView(myView);

        context_pg2 = getContext();

        /* ------- 임시 ------- */
        Button btntb = rootView.findViewById(R.id.btn11);
        btntb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createTime(); }
        });
        Button btncb = rootView.findViewById(R.id.btn22);
        btncb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createCheckbox(); }
        });
        Button btn33 = rootView.findViewById(R.id.btn33);
        btn33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaintView.class);
                startActivity(intent);
            }
        });
        /* -------------------- */

        /*Button btn_insert = ((Add_items) Add_items.context_additem).button;
        try {
            btn_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createEditbox();
                    Toast.makeText(getContext(), "okok", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "null 발생", Toast.LENGTH_SHORT).show();
        }*/


        thisLayout = rootView.findViewById(R.id.p_layout);

        TextView pagenumtext = (TextView) rootView.findViewById(R.id.pagenumtext);
        pagenumtext.setText(String.valueOf(pagenum));

        return rootView;
    }

    public void setSelectedVal(int newSelectedVal)
    {
        if(newSelectedVal != sel)
        {
            onSelectedValueChanged(sel);
            sel = newSelectedVal;
        }
    }

    public void onSelectedValueChanged(int val) {
        insertItem(val);
    }

    private class MyPaintView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;

        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(First.brushcolor);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if(First.draw==1) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
                canvas.drawPath(mPath, mPaint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            mPaint.setColor(First.brushcolor);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint);
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }
    }

    public void insertItem(int sel) {
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
        editText.setHint("배치 후 터치하세요");
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
                editText.requestFocus();
                editText.getShowSoftInputOnFocus();
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.mContext).mPager.setUserInputEnabled(false);

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
                    ((First)First.mContext).mPager.setUserInputEnabled(true);
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
        LinearLayout linearLayout = new LinearLayout(getContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(30, 20, 30, 20);
        linearLayout.setBackgroundColor(Color.rgb(255,255,255));

        CheckBox checkBox = new CheckBox(linearLayout.getContext());
        checkBox.setText("");
        linearLayout.addView(checkBox);
        EditText editText = new EditText(linearLayout.getContext());
        editText.setHint("배치 후 클릭하세요.");
        linearLayout.addView(editText);

        thisLayout.addView(linearLayout);

        linearLayout.setClickable(true);
        linearLayout.setOnTouchListener(touchListener);
        /*editText.setClickable(true);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.mContext).mPager.setUserInputEnabled(false);

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
                    ((First)First.mContext).mPager.setUserInputEnabled(true);
                }

                if (longTch) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        oriX = event.getRawX() - relatValX;
                        oriY = event.getRawY() - (relatValY + v.getHeight());
                        if (oriX % 50 < 25) {
                            linearLayout.setX(oriX - (oriX % 50));
                        }
                        if (oriY % 50 < 25) {
                            linearLayout.setY(oriY - (oriY % 50));
                        }
                    }
                }
                return false;
            }
        });*/

    }


    /*public void createCheckboxx() {
        CheckBox checkBox = new CheckBox(getContext());
        //LinearLayout checkbox = rootView.findViewById(R.id.a_checkbox);
        checkBox.setHint("배치 후 터치하세요");
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
                //Intent intent = new Intent(getContext(), edit_chkbox_content.class);
                //startActivityForResult(intent, 110);
                ////Toast.makeText(getContext(), "@@@@@@@@@", Toast.LENGTH_LONG).show();
                //checkBoxV = checkBox;
                checkBox.requestFocus();
                checkBox.getShowSoftInputOnFocus();
            }
        });
        checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((First)First.mContext).mPager.setUserInputEnabled(false);

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
                    ((First)First.mContext).mPager.setUserInputEnabled(true);
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
    }*/


    public void createPaint() {
        Intent intent = new Intent(getActivity(), PaintView.class);
        startActivity(intent);
    }


    public void createPageLink() {
        //
    }


    public void createTime() {
        Date now = Calendar.getInstance().getTime();
        /*SimpleDateFormat weekdayF = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayF = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat monthF = new SimpleDateFormat("M", Locale.getDefault());
        SimpleDateFormat yearF = new SimpleDateFormat("yyyy", Locale.getDefault());
        String weekDay = weekdayF.format(now);
        String year = yearF.format(now);
        String month = monthF.format(now);
        String day = dayF.format(now);*/
        //int dayOfWeek = Calendar.DAY_OF_WEEK;
        //String weekday = new DateFormatSymbols().getShortWeekdays()[dayOfWeek];

        String textFormat = "yyyy. M. d. EE.";
        String nowText = new SimpleDateFormat(textFormat, Locale.getDefault()).format(now);

        TextView textView = new TextView(getContext());
        textView.setText(nowText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundColor(Color.rgb(255,255,255));

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(params);
        thisLayout.addView(textView);

        textView.setClickable(true);
        textView.setOnTouchListener(touchListener);
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


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ((First) mContext).mPager.setUserInputEnabled(false);
                relatValX = event.getX();
                relatValY = event.getY();
                view.setOnLongClickListener(longClickListener);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                longTch = false;
                //((First)First.context_first).mPager.setUserInputEnabled(true);
                ((First) mContext).mPager.setUserInputEnabled(true);
            }

            if (longTch) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    oriX = event.getRawX() - relatValX;
                    oriY = event.getRawY() - (relatValY + view.getHeight());
                    if (oriX % 50 < 25) {
                        view.setX(oriX - (oriX % 50));
                    }
                    if (oriY % 50 < 25) {
                        view.setY(oriY - (oriY % 50));
                    }
                }
            }
            return false;
        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            longTch = true;
            return false;
        }
    };


}
