package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.firebasev1.DummyData.keyList;
import static com.example.firebasev1.DummyData.recipeList;


public class MainActivity extends AppCompatActivity {

    //
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    GridView gridView, gridViewBest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        gridView = (GridView) findViewById(R.id.GridView_New);
        gridViewBest = (GridView) findViewById(R.id.GridView_Best);
        Intent intent = getIntent();
        String s = intent.getStringExtra("name");


        navigationView = findViewById(R.id.navmenu);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        navUsername.setText(s);
        drawerLayout = findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent5);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu_home:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "Search Recipe", Toast.LENGTH_SHORT).show();
                        Intent intent16 = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent16);
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_Catgory:
                        Toast.makeText(getApplicationContext(), "Catgory", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), CategoryActivity.class);
                        startActivity(intent1);
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_Add:
                        Toast.makeText(getApplicationContext(), "Add Recipe", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
                        startActivity(intent);
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_profile:
                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent3);
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logout:
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        finish();
                        // drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_reported_recipes:
                        startActivity(new Intent(getApplicationContext(), ReportedRecipes.class));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        getDataNew();
        getDataBest();
    }

    customGridAdapter adapterNew;
    customGridAdapterBest adapterBest;

    void getDataNew() {
        DummyData.recipeList.clear();
        DummyData.keyList.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");
        Query query = databaseReference.limitToLast(3);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {

                    String country = recipeSnapshot.child("country").getValue(String.class);
                    String recipeName = recipeSnapshot.child("recipeName").getValue(String.class);
                    String author = recipeSnapshot.child("author").getValue(String.class);
                    String description = recipeSnapshot.child("recipeDescription").getValue(String.class);
                    String recipeHowTo = recipeSnapshot.child("recipeHowTo").getValue(String.class);
                    String imageUrl = recipeSnapshot.child("imageURL").getValue(String.class);
                    String ingredientListStr = recipeSnapshot.child("ingredientListStr").getValue(String.class);

                    DummyData dummyData = new DummyData(recipeName, author, description, recipeHowTo, country, imageUrl, ingredientListStr);
                    DummyData.recipeList.add(dummyData);
                    final String key = recipeSnapshot.getKey();
                    //get like count
                    //
                    DummyData.keyList.add(key);
                }

                Collections.reverse(recipeList);
                Collections.reverse(keyList);

                adapterNew = new customGridAdapter(getApplicationContext(), DummyData.recipeList, DummyData.keyList);
                gridView.setAdapter(adapterNew);
                adapterNew.notifyDataSetChanged();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapterNew.notifyDataSetChanged();
                        sendDataToDisplay(position, recipeList, keyList);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void sendDataToDisplay(int position, ArrayList<DummyData> recipeList, ArrayList<String> keyList) {
        Intent intent = new Intent(getApplicationContext(), RecipeDisplayActivity.class);
        intent.putExtra("key", keyList.get(position));

        intent.putExtra("recipeName", recipeList.get(position).getRecipeName());
        intent.putExtra("author", recipeList.get(position).getAuthor());
        intent.putExtra("description", recipeList.get(position).getRecipeDescription());
        intent.putExtra("howTo", recipeList.get(position).getRecipeHowTo());
        intent.putExtra("imageURL", recipeList.get(position).getImageURL());
        intent.putExtra("ingredientListStr", recipeList.get(position).ingredientListStr);
        intent.putExtra("country", recipeList.get(position).getCountry());
        intent.putExtra("transition", "MainActivity");
        startActivity(intent);
        finish();
    }


    ArrayList<Integer> arrayListlike = new ArrayList<>();
    ArrayList<DummyData> arrayListBest = new ArrayList<>();
    ArrayList<String> arrayListKey = new ArrayList<>();
    void getDataBest() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RecipeLike");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListKey.clear();
                arrayListlike.clear();
                arrayListBest.clear();
                for (DataSnapshot recipeLikeSnapShot : dataSnapshot.getChildren()) {

                    arrayListKey.add(recipeLikeSnapShot.getKey());
                    arrayListlike.add((int) recipeLikeSnapShot.getChildrenCount());
                }

                for (int i = 0; i < arrayListKey.size() - 1; i++) {
                    for (int j = i + 1; j < arrayListKey.size(); j++)
                        if (arrayListlike.get(i) < arrayListlike.get(j)) {
                            int t = arrayListlike.get(i);
                            arrayListlike.set(i, arrayListlike.get(j));
                            arrayListlike.set(j, t);

                            String temp = arrayListKey.get(i);
                            arrayListKey.set(i, arrayListKey.get(j));
                            arrayListKey.set(j, temp);
                        }
                }
                for (int i = 3; i < arrayListKey.size(); i++) {
                    arrayListKey.remove(i);
                    arrayListlike.remove(i);
                }

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Recipe");
                for (int i = 0; i < arrayListKey.size(); i++) {
                    databaseReference1.child(arrayListKey.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String country = dataSnapshot.child("country").getValue(String.class);
                            String recipeName = dataSnapshot.child("recipeName").getValue(String.class);
                            String author = dataSnapshot.child("author").getValue(String.class);
                            String description = dataSnapshot.child("recipeDescription").getValue(String.class);
                            String recipeHowTo = dataSnapshot.child("recipeHowTo").getValue(String.class);
                            String imageUrl = dataSnapshot.child("imageURL").getValue(String.class);
                            String ingredientListStr = dataSnapshot.child("ingredientListStr").getValue(String.class);

                            DummyData dummyData = new DummyData(recipeName, author, description, recipeHowTo, country, imageUrl, ingredientListStr);
                            arrayListBest.add(dummyData);
                            adapterBest = new customGridAdapterBest(getApplicationContext(), arrayListBest, arrayListKey);
                            gridViewBest.setAdapter(adapterBest);
                            adapterBest.notifyDataSetChanged();


                            gridViewBest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    adapterBest.notifyDataSetChanged();
                                    sendDataToDisplay(position, arrayListBest, arrayListKey);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        boolean drawerOpen = drawerLayout.isDrawerOpen(navigationView);
//        super.onPrepareOptionsMenu(menu);
//
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String mobileno = sh.getString("mobileno", "0");
//        Toast.makeText(getApplicationContext(), "m" + mobileno, Toast.LENGTH_LONG).show();
//        if ((mobileno.equalsIgnoreCase("+918830788743")) || (mobileno.equalsIgnoreCase("+918208179396")))
//            navigationView.getMenu().findItem(R.id.menu_reported_recipes).setVisible(true);
//        else
//            navigationView.getMenu().findItem(R.id.menu_reported_recipes).setVisible(false);
//
//        return true;
//
//    }
}

