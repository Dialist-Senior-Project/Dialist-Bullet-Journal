package com.example.dialist;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;



public class PaintView extends AppCompatActivity {
    //Button redBtn, blueBtn, blackBtn;
    Button clearBtn, saveBtn, cancelBtn;
    LinearLayout linear_draw;
    Bitmap bitmap;
    boolean dbit = false;

    int color = Color.BLACK;

    class Point {
        float x;
        float y;
        boolean check;
        int color;

        public Point(float x, float y, boolean check, int color) {
            this.x = x;
            this.y = y;
            this.check = check;
            this.color = color;
        }
    }

    class MyDraw extends View {
        ArrayList<Point> points = new ArrayList<Point>();

        public MyDraw(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (dbit == true) {
                points.clear();

                canvas.drawBitmap(
                        bitmap, // 출력할 bitmap
                        new Rect(0, 0, 150, 150),   // 출력할 bitmap의 지정된 영역을 (sub bitmap)
                        new Rect(0, 0, 150, 150),  // 이 영역에 출력한다. (화면을 벗어나면 clipping됨)
                        null);
                //super.onDraw(canvas);

                findViewById(R.id.btnClear).setBackgroundColor(Color.rgb(255,0,0));

                dbit = false;
                //invalidate();
                return;
            }


            Paint p = new Paint();
            p.setStrokeWidth(15);
            for (int i = 1; i < points.size(); i++) {
                p.setColor(points.get(i).color);
                if (!points.get(i).check)
                    continue;
                canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    points.add(new Point(x, y,false, color));
                case MotionEvent.ACTION_MOVE :
                    points.add(new Point(x, y,true, color));
                    break;
                case MotionEvent.ACTION_UP :
                    break;
            }
            invalidate();
            return true;
        }

        public void drawbitmap() {
            dbit = true;
            invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        linear_draw = findViewById(R.id.linearDraw);
        //linear_draw.setDrawingCacheEnabled(true);
        final MyDraw m = new MyDraw(getApplicationContext());

        findViewById(R.id.btnRed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { color = Color.RED; }
        });
        findViewById(R.id.btnBlue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { color = Color.BLUE; }
        });
        findViewById(R.id.btnBlack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { color = Color.BLACK;
            }
        });

        saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_draw.buildDrawingCache();
                bitmap = linear_draw.getDrawingCache();
                FileOutputStream fos;

                /*if (bitmap == null) {
                    findViewById(R.id.btnClear).setBackgroundColor(Color.rgb(255,0,0));
                } else {
                    findViewById(R.id.btnClear).setBackgroundColor(Color.rgb(0,0,255));
                }*/

                String filePath = getFilesDir().getPath();
                String s = filePath + "/capture.png";
                String s2 = "/data/data/dialist";
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
                TextView tv = findViewById(R.id.textView4);

                //m.drawbitmap();

                try {
                    //fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/capture1.png");
                    fos = new FileOutputStream(s);
                    tv.setText(s);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    tv.setText("except");
                }


            }
        });

        clearBtn = findViewById(R.id.btnClear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.points.clear();
                m.invalidate();
            }
        });

        linear_draw.addView(m);


        /*findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //View v = findViewById(R.id.linearDraw);
                linear_draw.buildDrawingCache();
                Bitmap bitmap = linear_draw.getDrawingCache();
                FileOutputStream fos;

                //File fileFile = getFilesDir();
                //String getFile = fileFile.getPath();
                String filePath = getFilesDir().getPath();

                String s = filePath + "/capture.png";
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);

                try {
                    //fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/capture1.png");
                    fos = new FileOutputStream(filePath + "/capture.png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });*/
    }
}



