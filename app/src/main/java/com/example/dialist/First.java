package com.example.dialist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class First extends AppCompatActivity implements View.OnClickListener {
    Button logoutButton;

    private ArrayList<Template> templateArrayList;
    private TemplateAdapter templateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //로그아웃
        logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        //리사이클러뷰

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerGridView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        templateArrayList = new ArrayList<>();
        templateAdapter = new TemplateAdapter(templateArrayList);
        recyclerView.setAdapter(templateAdapter);

        Button button = findViewById(R.id.Addtemplate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Template newTodo = new Template("일기장");
                templateArrayList.add(newTodo);
                templateAdapter.notifyDataSetChanged();
            }
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                signOut();
                finishAffinity();
                break;
        }
    }
}