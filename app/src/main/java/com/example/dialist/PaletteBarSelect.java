package com.example.dialist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class PaletteBarSelect extends Activity {
    public static int clickbutton=1;
    public static int color1=-16777216;
    public static int color2=0;
    public static int color3=0;
    public static int color4=0;
    public static int color5=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_palette_bar_select);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);

        PaletteBar paletteBar = findViewById(R.id.paletteBar);
        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int colorInt) {
                switch(clickbutton){
                    case 1:
                        button1.setBackgroundColor(colorInt);
                        color1=colorInt;
                        break;
                    case 2:
                        button2.setBackgroundColor(colorInt);
                        color2=colorInt;
                        break;
                    case 3:
                        button3.setBackgroundColor(colorInt);
                        color3=colorInt;
                        break;
                    case 4:
                        button4.setBackgroundColor(colorInt);
                        color4=colorInt;
                        break;
                    case 5:
                        button5.setBackgroundColor(colorInt);
                        color5=colorInt;
                        break;
                }
            }
        });

        button1.setOnClickListener(view -> {
            clickbutton=1;
        });

        button2.setOnClickListener(view -> {
            clickbutton=2;
        });

        button3.setOnClickListener(view -> {
            clickbutton=3;
        });

        button4.setOnClickListener(view -> {
            clickbutton=4;
        });

        button5.setOnClickListener(view -> {
            clickbutton=5;
        });

        (findViewById(R.id.colorOK)).setOnClickListener(view -> {
            Page_1.brushcolor = color1;
            finish();
        });
    }
}