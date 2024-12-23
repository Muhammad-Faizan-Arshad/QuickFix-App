package Backup.QuickFix;

import android.os.Bundle;
import android.util.Log;
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

public class mechanicprofile extends AppCompatActivity {

    private TextView usernameTextView, cityTextView, experienceTextView, aboutTextView, user_name, fullNameTextView, emailTextView, mobileTextView;
    private RatingBar ratingBar;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String mechanicID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofile);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
        } else {
            Toast.makeText(this, "Mechanic ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
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

                    if (mechanic.getRating() != null) {
                        ratingBar.setRating(mechanic.getRating());
                    }

                    calculateAverageRating(snapshot.child("reviews"));
                } else {
                    Toast.makeText(mechanicprofile.this, "Mechanic data not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileActivity", "Error loading mechanic data: " + error.getMessage());
                Toast.makeText(mechanicprofile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
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
}
