package Backup.QuickFix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Backup.QuickFix.model.Mechanic;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView, cityTextView, experienceTextView, aboutTextView, user_name, fullNameTextView, emailTextView, mobileTextView;
    private RatingBar ratingBar;
    private Button reviewbtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String mechanicID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        reviewbtn = findViewById(R.id.reviewbtn);
        emailTextView = findViewById(R.id.emailTextView);
        usernameTextView = findViewById(R.id.username_textview);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        cityTextView = findViewById(R.id.city_textview);
        experienceTextView = findViewById(R.id.experience_textview);
        aboutTextView = findViewById(R.id.about_textview);
        ratingBar = findViewById(R.id.rating_bar);
        user_name = findViewById(R.id.user_name);


        mechanicID = getIntent().getStringExtra("mechanicID");

        if (mechanicID != null) {

            loadMechanicDetails(mechanicID);
            checkUserRoleAndReviewStatus();
        } else {
            Toast.makeText(this, "Mechanic ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        reviewbtn.setOnClickListener(v -> {

            Intent intent = new Intent(ProfileActivity.this, ReviewActivity.class);
            intent.putExtra("mechanicID", mechanicID);
            startActivityForResult(intent, 1); // Start with a request code
        });
    }

    private void loadMechanicDetails(String mechanicID) {
        DatabaseReference mechanicRef = databaseReference.child("mechanics").child(mechanicID);

        mechanicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Mechanic mechanic = snapshot.getValue(Mechanic.class);
                if (mechanic != null) {

                    user_name.setText(mechanic.getUsername() != null ? mechanic.getUsername() : "No Name");
                    emailTextView.setText(mechanic.getEmail() != null ? mechanic.getEmail() : "No Email");
                    fullNameTextView.setText(mechanic.getFullname() != null ? mechanic.getFullname() : "No Full Name");
                    usernameTextView.setText(mechanic.getUsername() != null ? mechanic.getUsername() : "No Username");
                    cityTextView.setText(mechanic.getCity() != null ? mechanic.getCity() : "Not specified");
                    experienceTextView.setText(mechanic.getExperience() != null ? mechanic.getExperience() + " years" : "No experience info");
                    aboutTextView.setText(mechanic.getAbout() != null ? mechanic.getAbout() : "No description");
                    mobileTextView.setText(mechanic.getMobileNumber() != null ? mechanic.getMobileNumber() : "No mobile number");

                    // Set the rating if available
                    if (mechanic.getRating() != null) {
                        ratingBar.setRating(mechanic.getRating());
                    }


                    calculateAverageRating(snapshot.child("reviews"));
                } else {
                    Toast.makeText(ProfileActivity.this, "Mechanic data not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileActivity", "Error loading mechanic data: " + error.getMessage());
                Toast.makeText(ProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAverageRating(DataSnapshot reviewsSnapshot) {
        float totalRating = 0;
        int count = 0;

        for (DataSnapshot reviewSnapshot : reviewsSnapshot.getChildren()) {
            Float rating = reviewSnapshot.child("rating").getValue(Float.class);
            if (rating != null) {
                totalRating += rating;
                count++;
            }
        }

        if (count > 0) {
            float averageRating = totalRating / count;
            ratingBar.setRating(averageRating);
        }
    }

    private void checkUserRoleAndReviewStatus() {
        DatabaseReference userRef = databaseReference.child("users").child(userID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    Log.d("ProfileActivity", "User role: " + role);

                    if ("mechanic".equals(role)) {
                        reviewbtn.setVisibility(View.GONE);
                        Log.d("ProfileActivity", "Review button hidden for mechanic");
                    } else {

                        checkIfUserReviewed();
                    }
                } else {
                    Log.d("ProfileActivity", "User data not found in Firebase.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to check user role", Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Firebase error: " + error.getMessage());
            }
        });
    }

    private void checkIfUserReviewed() {
        DatabaseReference reviewRef = databaseReference.child("mechanics").child(mechanicID).child("reviews").child(userID);

        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    reviewbtn.setVisibility(View.GONE);
                } else {
                    reviewbtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to check review status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the submitted rating
            float submittedRating = data.getFloatExtra("rating", 0);
            updateAverageRating(submittedRating);
        }
    }

    private void updateAverageRating(float newRating) {
        loadMechanicDetails(mechanicID);
    }
}
