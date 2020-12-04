package com.example.firebasev1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterRecipeNameImgAuthLike extends BaseAdapter {
    Context context;
    ArrayList<DummyData>recipeList=new ArrayList<>();
    ArrayList<String>keyList=new ArrayList<>();
    int like_or_Dislike;
    public AdapterRecipeNameImgAuthLike(){}

    public AdapterRecipeNameImgAuthLike(Context context, ArrayList<DummyData> recipeList1,ArrayList<String>keyList,int like_or_Dislike) {
        this.context = context;
        this.like_or_Dislike=like_or_Dislike;
        for(int i=0;i<recipeList1.size();i++)
        {
            recipeList.add(recipeList1.get(i));
        }
        this.keyList=keyList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.listviewshowcountrywiserecipe,null);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageViewRecipeListView);
        TextView recipeName=(TextView)convertView.findViewById(R.id.txtRecipeNameListView);
        TextView recipeAuthor=(TextView)convertView.findViewById(R.id.txtRecipeAuthListView);
        final TextView recipeLike=(TextView)convertView.findViewById(R.id.txtRecipeLikeListView);

        recipeName.setText(recipeList.get(position).getRecipeName());
        recipeAuthor.setText(recipeList.get(position).getAuthor());
        final String s;
        if(like_or_Dislike==0)
        {   s="RecipeLike/";}
        else
        {s="RecipeReport/";
            recipeLike.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_unchekedlike,0,0,0);
        }
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(s+keyList.get(position));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                   if(s.equalsIgnoreCase("RecipeLike/"))
                                    recipeLike.setText(""+dataSnapshot.getChildrenCount()+"");
                                   else
                                       recipeLike.setText("Report : "+dataSnapshot.getChildrenCount()+"");

                                }
                                else
                                {
                                    recipeLike.setText("0");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

        Glide.with(context).load(recipeList.get(position).getImageURL()).into(imageView);

        return convertView;
    }
}
