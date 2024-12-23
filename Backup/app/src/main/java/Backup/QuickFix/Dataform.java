package Backup.QuickFix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import Backup.QuickFix.model.Mechanic;

public class Dataform extends AppCompatActivity {

    private EditText fullNameEditText, mobileNumberEditText, aboutEditText;
    private Spinner ageSpinner, experienceSpinner, citySpinner;
    private Button saveButton;
    private DatabaseReference mechanicsRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataform);


        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not logged in. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, login.class));
            finish();
            return;
        }

        mechanicsRef = FirebaseDatabase.getInstance().getReference("mechanics").child(userId);


        fullNameEditText = findViewById(R.id.Full_Name);
        mobileNumberEditText = findViewById(R.id.Mobile_Number);
        aboutEditText = findViewById(R.id.About);
        ageSpinner = findViewById(R.id.Age);
        experienceSpinner = findViewById(R.id.Experience);
        citySpinner = findViewById(R.id.City);
        saveButton = findViewById(R.id.signup_button);


        setupAgeSpinner();
        setupExperienceSpinner();
        setupCitySpinner();


        saveButton.setOnClickListener(v -> saveMechanicData());
    }

    private void setupAgeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
    }

    private void setupExperienceSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.experience_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experienceSpinner.setAdapter(adapter);
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    private void saveMechanicData() {

        String fullName = fullNameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String ageString = ageSpinner.getSelectedItem().toString();
        String experience = experienceSpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();
        String about = aboutEditText.getText().toString().trim();

        // Validate input
        if (fullName.isEmpty() || mobileNumber.isEmpty() || ageString.equals("Select Age") ||
                experience.equals("Select Experience") || city.equals("Select City")) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Long age = Long.parseLong(ageString);


        Map<String, Object> mechanicUpdates = new HashMap<>();
        mechanicUpdates.put("fullName", fullName);
        mechanicUpdates.put("mobileNumber", mobileNumber);
        mechanicUpdates.put("age", age);
        mechanicUpdates.put("experience", experience);
        mechanicUpdates.put("city", city);
        mechanicUpdates.put("about", about);


        mechanicsRef.updateChildren(mechanicUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Dataform.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Dataform.this, mechanicprofile.class);
                intent.putExtra("mechanicID", userId); // Pass the mechanic ID to the profile
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Dataform.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
