package com.example.dialist;

import static com.example.dialist.First.num_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Page_2 extends Fragment {
    int pagenum;

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

        TextView pagenumtext = (TextView) rootView.findViewById(R.id.pagenumtext);
        pagenumtext.setText(String.valueOf(pagenum));

        return rootView;
    }
}
