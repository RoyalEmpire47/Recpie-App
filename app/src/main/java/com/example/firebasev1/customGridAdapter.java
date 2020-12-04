package com.example.firebasev1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class customGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<DummyData> data = new ArrayList<>();
    ArrayList<String> keylist = new ArrayList<>();

    public customGridAdapter() {
    }

    public customGridAdapter(Context context, ArrayList<DummyData> d, ArrayList<String> keyList) {
        this.context = context;
        data = d;
        this.keylist = keyList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.row_grid, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_image);
        TextView recipeName = (TextView) convertView.findViewById(R.id.item_text);
        Glide.with(context).load(data.get(position).getImageURL()).into(imageView);
        recipeName.setText(data.get(position).getRecipeName());

        return convertView;
    }
}
