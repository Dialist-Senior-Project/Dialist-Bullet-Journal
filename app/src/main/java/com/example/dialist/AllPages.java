package com.example.dialist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

public class AllPages extends Activity {

    private ArrayList<Template> templateArrayList;
    private TemplateAdapter templateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pages);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerGridView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        templateArrayList = new ArrayList<>();
        templateAdapter = new TemplateAdapter(templateArrayList);
        recyclerView.setAdapter(templateAdapter);
    }
}