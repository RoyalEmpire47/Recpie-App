package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class    DetailsActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    TextInputLayout firstName, lastName, email;
    Button saveBtn;
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    String userID;
    ImageView userphoto;
    int takeimagecode = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.emailAddress);
        saveBtn = findViewById(R.id.saveBtn);
        userphoto = findViewById(R.id.userimg);
        fAuth = FirebaseAuth.getInstance();
        // fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getEditText().getText().toString().isEmpty() || lastName.getEditText().getText().toString().isEmpty() || email.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill the required Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
                // DocumentReference docRef = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("first", firstName.getEditText().getText().toString());
                user.put("last", lastName.getEditText().getText().toString());
                user.put("email", email.getEditText().getText().toString());

                databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //aditya
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                        // Creating an Editor object
                        // to edit(write to the file)
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        // Storing the key and its value
                        // as the data fetched from edittext
                        String a = firstName.getEditText().getText().toString() + " " + lastName.getEditText().getText().toString();
                        myEdit.putString("name", a);
                        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
                        // Once the changes have been made,
                        // we need to commit to apply those changes made,
                        // otherwise, it will throw an error
                        myEdit.commit();
                        //
                        Log.d(TAG, "onSuccess: User Profile Created." + userID);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to Create User " + e.toString());
                    }
                });

            }
        });


        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent2, takeimagecode);

                }
            }


        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == takeimagecode) {
            switch (resultCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    userphoto.setImageBitmap(bitmap);
                    handleuploda(bitmap);
            }
        }


    }

    private void handleuploda(Bitmap bitmap) {

        ByteArrayOutputStream baso = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baso);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("ProfileImage").child(uid + ".jpeg");

        reference.putBytes(baso.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getdowmloadurl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {


                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.e(TAG,"onFailure",e.getCause());
                        Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getdowmloadurl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Log.d(TAG,"omSuccess"+uri);
                Toast.makeText(getApplicationContext(), "omSuccess" + uri, Toast.LENGTH_SHORT).show();
                setuserprofileuri(uri);
            }
        });
    }


    private void setuserprofileuri(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "updated sucessfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Profile image fail", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}
