package com.example.dialist;

import static android.view.Window.FEATURE_NO_TITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Add_items extends AppCompatActivity {

    int selected = 0;
    public static Context context_additem;
    static Button button;

    public Add_items() {
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_add_items);

        context_additem = this;

        // Adds the Fragment C to the right container
        //getFragmentManager().beginTransaction().add(R.id.right_container, new FragmentC()).commit();

        button = findViewById(R.id.btn_insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == 0)
                    return;

                finish();
                ((First)First.mContext).insertItem(selected);

                //  //Page_2 myFragment = new Page_2("f()", currentPg, selected, context);
                //Bundle bundle = new Bundle(1); // 파라미터의 숫자는 전달하려는 값의 갯수
                //bundle.putString("key", "value");
                //myFragment.setArgument(bundle);
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
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 2;
            }
        });
        ImageView checkbox2 = findViewById(R.id.img_chkbox);
        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 2;
                checkbox.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        TextView checkbox3 = findViewById(R.id.tv_chkbox);
        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 2;
                checkbox.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });

        FrameLayout paint = findViewById(R.id.tch_paint);
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 3;
            }
        });
        ImageView paint2 = findViewById(R.id.img_paint);
        paint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 3;
                paint.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        TextView paint3 = findViewById(R.id.tv_paint);
        paint3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 3;
                paint.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });

        FrameLayout pagelink = findViewById(R.id.tch_link);
        pagelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 4;
            }
        });
        ImageView pagelink2 = findViewById(R.id.img_link);
        pagelink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 4;
                pagelink.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        TextView pagelink3 = findViewById(R.id.tv_link);
        pagelink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 4;
                pagelink.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });

        FrameLayout time = findViewById(R.id.tch_time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 5;
            }
        });
        ImageView time2 = findViewById(R.id.img_time);
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 5;
                time.setBackgroundColor(Color.rgb(200, 200, 200));
            }
        });
        TextView time3 = findViewById(R.id.tv_time);
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 5;
                time.setBackgroundColor(Color.rgb(200, 200, 200));
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



    public void add_real_item() {
        finish();

        //Page_2 fragment = new Page_2("sel", 1);
        //fragment.createEditbox(PageAdapter.context_padapter);

        //FrameLayout contentFrame = findViewById(R.id.content_frame); // 1. 기반이 되는 FrameLayout
        ViewPager2 vpager = findViewById(R.id.viewpager);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  // 2. inflater 생성
        inflater.inflate(R.layout.page_2, vpager,false);

        getSupportFragmentManager()
                .beginTransaction();
                //.add(R.id.viewpager, R.id.p_layout);
                //.

        //inflater.inflate(R.layout.content_1_1,contentFrame,true); // 3. (넣을 xml 파일명, 기반 layout 객체, true)


        //fragment = (Page_2) getSupportFragmentManager().findFragmentByTag("page2");

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //Page_2 fragment = (Page_2) fragmentManager.findFragmentByTag("page2");

        /*if (fragment == null) {
            Toast.makeText(getApplicationContext(), "null !!!", Toast.LENGTH_SHORT).show();
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_container, new Frag1(), name)
                    .addToBackStack(name)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();//
            //return;
        }
        else {
            Toast.makeText(getApplicationContext(), "not null !!!", Toast.LENGTH_SHORT).show();
        }*/

        /*
        fragmentManager.beginTransaction()
                //.replace(R.id.viewpager, Page_2.class, null, "page2")
                .add(fragment, "page2")
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        */

        //fragment.createEditbox();
        /*if (fragment == null) {
            int i = getSupportFragmentManager().getBackStackEntryCount();
            Toast.makeText(getApplicationContext(), "null !!! ::  "+i, Toast.LENGTH_SHORT).show();

            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_container, new Frag1(), name)
                    .addToBackStack(name)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();//
            return;
        }
        else {
            Toast.makeText(getApplicationContext(), "not null !!!", Toast.LENGTH_SHORT).show();
        }*/

        /*
        // Instantiate a new instance before adding
        ExampleFragment myFragment = new ExampleFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_view_container, myFragment)
                .setReorderingAllowed(true)
                .commit();
         */

        //-------------

        //Toast.makeText(getApplicationContext(), "실행ok", Toast.LENGTH_LONG);
        //화면에 보여지는 fragment를 추가하거나 바꿀 수 있는 객체를 만든다
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Page_2 pg = new Page_2();
        //첫번째로 보여지는 fragment는 firstFragment로 설정한다
        ft.add(R.id.viewpager, pg, "tag_page2");

        ft.addToBackStack(null);
        //fragment의 변경사항을 반영시킨다.
        ft.commit();
        //getSupportFragmentManager().executePendingTransactions();*/

        //-------------

        //Page_2 pg = (Page_2) getSupportFragmentManager().findFragmentById(R.id.p_layout);

        //(getSupportFragmentManager().findFragmentByTag("tag_page2")).createEditbox();

        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Incompatible types.
        //   Found: 'android.app.FragmentTransaction',
        //   required: 'androidx.fragment.app.FragmentTransaction'

        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); //getFragmentManager().beginTransaction();
        //ft.add(R.id.f_layout, new Page_2(), "tag_page2");  //ft.add(R.id.p, fragment, tag);
        ft.addToBackStack("tag_page2");
        ft.commit(); //Finishes the transaction
        getSupportFragmentManager().executePendingTransactions();
        //getFragmentManager().executePendingTransactions();

        Page_2 pg = (Page_2) getSupportFragmentManager().findFragmentByTag("tag_page2");*/

        //FragmentManager fm = getSupportFragmentManager();
        //fm.findFragmentByTag("tag_page2");
        //Page_2 pg = (Page_2) getSupportFragmentManager().findFragmentById(R.id.p_layout);
        //.findFragmentByTag("tag_page2");
        //pg.insertItem(selected);  // 여기서 강종됨4

        /*if (fm.findFragmentByTag("tag_page2") == null) {
            fm.beginTransaction()
                    .add(R.id.f_layout, new Page_2(), "tag_page2")
                    .addToBackStack("tag_page2")
                    .commit();
        }*/


        /*Page_2 pg = (Page_2) fm.findFragmentByTag("tag_page2");
        if (pg == null) {
            Toast.makeText(getApplicationContext(), "null---2", Toast.LENGTH_SHORT).show();
            return;
        }*/

        //pg.createEditbox();

        selected = 0;
    }
}