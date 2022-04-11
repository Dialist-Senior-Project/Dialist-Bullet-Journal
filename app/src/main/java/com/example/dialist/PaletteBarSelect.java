package com.example.dialist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class PaletteBarSelect extends Activity {
    public static int clickbutton=1;
    public static int color1=-16777216;
    public static int color2=0;
    public static int color3=0;
    public static int color4=0;
    public static int color5=0;
    public static int color=0;

    private DatabaseReference mDatabase;

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

        //final int[][] colors = {new int[0]};

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(First.enEmail).child(String.valueOf(First.now_page)).child("BrushColors");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                //while (child.hasNext()) {
                //    colors[0] = (int[]) child.next().getValue();
                //}
                /*
                for (DataSnapshot postsnapshot: snapshot.getChildren()) {
                    String key = postsnapshot.getKey();
                    ColorPost get = postsnapshot.getValue(ColorPost.class);
                    int[] info = {get.c1, get.c2, get.c3, get.c4, get.c5};
                    button1.setBackgroundColor(info[0]);
                    button2.setBackgroundColor(info[1]);
                    button3.setBackgroundColor(info[2]);
                    button4.setBackgroundColor(info[3]);
                    button5.setBackgroundColor(info[4]);

                    String key = postsnapshot.getKey();
                    HashMap<String, Integer> colorInfo = (HashMap<String, Integer>) postsnapshot.getValue();
                    int[] getData = {colorInfo.get("c1"), colorInfo.get("c2"), colorInfo.get("c3"), colorInfo.get("c4"), colorInfo.get("c5")};
                    button1.setBackgroundColor(getData[0]);
                    button2.setBackgroundColor(getData[1]);
                    button3.setBackgroundColor(getData[2]);
                    button4.setBackgroundColor(getData[3]);
                    button5.setBackgroundColor(getData[4]);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        PaletteBar paletteBar = findViewById(R.id.paletteBar);
        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int colorInt) {
                switch(clickbutton){
                    case 1:
                        button1.setBackgroundColor(colorInt);
                        color1=colorInt;
                        color=colorInt;
                        break;
                    case 2:
                        button2.setBackgroundColor(colorInt);
                        color2=colorInt;
                        color=colorInt;
                        break;
                    case 3:
                        button3.setBackgroundColor(colorInt);
                        color3=colorInt;
                        color=colorInt;
                        break;
                    case 4:
                        button4.setBackgroundColor(colorInt);
                        color4=colorInt;
                        color=colorInt;
                        break;
                    case 5:
                        button5.setBackgroundColor(colorInt);
                        color5=colorInt;
                        color=colorInt;
                        break;
                }
            }
        });

        button1.setOnClickListener(view -> {
            clickbutton=1;
            color=color1;
        });

        button2.setOnClickListener(view -> {
            clickbutton=2;
            color=color2;
        });

        button3.setOnClickListener(view -> {
            clickbutton=3;
            color=color3;
        });

        button4.setOnClickListener(view -> {
            clickbutton=4;
            color=color4;
        });

        button5.setOnClickListener(view -> {
            clickbutton=5;
            color=color5;
        });

        (findViewById(R.id.colorOK)).setOnClickListener(view -> {
            DBColor(color1, color2, color3, color4, color5, color);
            Page_1.brushcolor=color;
            finish();
        });
    }


    private void DBColor(int c1, int c2, int c3, int c4, int c5, int c){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DBColor thing = new DBColor(c1, c2, c3, c4, c5, c);
        mDatabase.child("User").child(First.enEmail).child(String.valueOf(First.now_page)).child("BrushColors").setValue(thing).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "DBC성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "DBC실패", Toast.LENGTH_SHORT).show();

            }
        });
    }
}