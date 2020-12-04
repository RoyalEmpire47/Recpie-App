package com.example.firebasev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    String mainauthor;
    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;

    TextView fullName, email, phone;
    String mName, mEmail, mPhone;

    ImageView setomage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.profileFullName);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        setomage = findViewById(R.id.imageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(setomage);

        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Query checkuser = databaseReference.orderByChild(firebaseAuth.getCurrentUser().getUid());
        final String uid = firebaseAuth.getCurrentUser().getUid();

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mName = dataSnapshot.child(uid).child("first").getValue(String.class);
                    String lName = dataSnapshot.child(uid).child("last").getValue(String.class);
                    mEmail = dataSnapshot.child(uid).child("email").getValue(String.class);
                    mPhone = firebaseAuth.getCurrentUser().getPhoneNumber();


                    mainauthor = mName + " " + lName;
                    fullName.setText(mainauthor);
                    email.setText(mEmail);
                    phone.setText(mPhone);
                } else {
                    Log.d(TAG, "Retrieving Data: Profile Data Not Found ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Retrieving Data: Profile Data Not Found ");
            }
        });


    }


}
