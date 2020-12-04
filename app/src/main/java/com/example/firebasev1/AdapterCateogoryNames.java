package com.example.firebasev1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class AdapterCateogoryNames extends BaseAdapter {

    Context context;
    String[] CategoryName;
    int[] CategoryImages;

    public AdapterCateogoryNames() {
    }

    public AdapterCateogoryNames(Context context, String[] categoryName, int[] categoryImages) {
        this.context = context;
        CategoryName = categoryName;
        CategoryImages = categoryImages;
    }

    @Override
    public int getCount() {
        return CategoryName.length;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.catogorylistliewlayout, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewCateogoryList);
        TextView textView = (TextView) convertView.findViewById(R.id.textViewCatorgorylist);

        imageView.setImageResource(CategoryImages[position]);
        textView.setText(CategoryName[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowCountryWiseRecipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Toast.makeText(context,""+getItem(position),Toast.LENGTH_LONG).show();
                intent.putExtra("category", CategoryName[position]);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
