package com.example.firebasev1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;


import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;


public class AddRecipeActivity extends AppCompatActivity {
    ImageView imgview;

    TextView txtDescription;
    EditText edtRecipeName, edtRecipeDescription;
    Button btnNext;
    FloatingActionButton fabCamera;
    //variable for camera
    int Image_Request_Code = 7;
    Uri FilePathUri;
    Spinner spinCountryName;
    String CountryNames[];
    ProgressDialog progressDialog;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        imgview = (ImageView) findViewById(R.id.image_view);

        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        txtDescription = (TextView) findViewById(R.id.txt_description);
        progressDialog = new ProgressDialog(AddRecipeActivity.this);
        edtRecipeName = (EditText) findViewById(R.id.edt_recipe_name);
        edtRecipeDescription = (EditText) findViewById(R.id.edtRecipeDescription);
        btnNext = (Button) findViewById(R.id.btn_next);
        spinCountryName = (Spinner) findViewById(R.id.spinner_country_name);

        CountrySelection();

        storageReference = FirebaseStorage.getInstance().getReference("Recipe");

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadData();
            }
        });
    }

    void CountrySelection() {
        CountryNames = new String[]{"India", "Korea", "USA", "Russia", "Italy"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, CountryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCountryName.setAdapter(arrayAdapter);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void UploadData() {

        if (FilePathUri != null) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            if ((!TextUtils.isEmpty(edtRecipeName.getText().toString())) || (!TextUtils.isEmpty(edtRecipeDescription.getText().toString()))) {


                final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                storageReference2.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageURL = uri.toString();

                                        Intent intent = new Intent(getApplicationContext(), AddIngredientListActivity.class);

                                        String recipeName=edtRecipeName.getText().toString().toLowerCase().trim();

                                        intent.putExtra("RecipeName",recipeName.substring(0, 1).toUpperCase() + recipeName.substring(1));
                                        intent.putExtra("RecipeDescription", edtRecipeDescription.getText().toString().trim());
                                        intent.putExtra("Country", spinCountryName.getSelectedItem().toString().trim());
                                        intent.putExtra("ImageURL", imageURL);


                                        progressDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(),imageURL,Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
//                            @SuppressWarnings("VisibleForTests")
//
                            }
                        });

            } else {

                Toast.makeText(getApplicationContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(getApplicationContext(), "Please Enter All fieldsaa.", Toast.LENGTH_LONG).show();
        }


    }


}
