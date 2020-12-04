package com.example.firebasev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddIngredientListActivity extends AppCompatActivity {

    public static String Str;
    ListView listViewIngredient;
    ArrayList<String> ingredientList;
    Button btnAdd, btnNext;
    EditText edtIngredient;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_list);
        TextView textView = (TextView) findViewById(R.id.text_view);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnNext = (Button) findViewById(R.id.btn_nextIngredientlist);
        edtIngredient = (EditText) findViewById(R.id.edtIngredient);
        listViewIngredient = (ListView) findViewById(R.id.listviewIngredients);
        ingredientList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ingredientList);
        listViewIngredient.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtIngredient.getText().toString())) {
                    if (!ingredientList.contains(edtIngredient.getText().toString()))
                        ingredientList.add(edtIngredient.getText().toString().toLowerCase().trim());
                    else
                        Toast.makeText(getApplicationContext(), "Duplicate", Toast.LENGTH_SHORT).show();

                    ((EditText) findViewById(R.id.edtIngredient)).getText().clear();


                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Add Ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddRecipeSubmitActivity.class);

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    String S = ingredientList.toString();
                    Str = S.substring(1, S.length() - 1);

                    intent.putExtras(bundle);
                }
                startActivity(intent);

            }
        });


        listViewIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingredientList.remove(position);
//                listViewIngredient.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
