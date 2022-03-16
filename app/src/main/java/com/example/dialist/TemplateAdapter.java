package com.example.dialist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<Holder> {
    private ArrayList<Template> mData = null;

    TemplateAdapter(ArrayList<Template> list) {
        mData = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_template_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.text.setText(mData.get(position).geTemplatetitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class Holder extends RecyclerView.ViewHolder{
    TextView text;

    public Holder(View itemview){
        super(itemview);
        text = itemview.findViewById(R.id.templateTitle);
    }
}
