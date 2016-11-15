package com.mu.compet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Mu on 2016-10-24.
 */

public class MyPageGridAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<String> items = new ArrayList<>();
    private LayoutInflater inflater;

    public void add(String image) {
        items.add(image);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> image) {
        items.addAll(image);
        notifyDataSetChanged();
    }

    public MyPageGridAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.isEmpty() ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(layout, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.image);
        Glide.with(parent.getContext()).load(items.get(position)).into(iv);


        return convertView;
    }
}
