package Backup.QuickFix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText Login_user, Loginpass;
    private Button Logbtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        Login_user = findViewById(R.id.login_username);
        Loginpass = findViewById(R.id.login_password);
        Logbtn = findViewById(R.id.login_button);

        Loginpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0);

        Loginpass.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (Loginpass.getRight() - Loginpass.getCompoundDrawables()[2].getBounds().width())) {
                    Loginpass.setInputType(Loginpass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ?
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD :
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    return true;
                }
            }
            return false;
        });

        TextView signuplink = findViewById(R.id.signupRedirectText);
        signuplink.setOnClickListener(v -> {
            startActivity(new Intent(login.this, SignupActivity.class));
            finish();
        });

        Logbtn.setOnClickListener(v -> {
            String email = Login_user.getText().toString().trim();
            String password = Loginpass.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                checkUserRole(userId);
                            }
                        } else {
                            Toast.makeText(this, "Login failed. Check your email and password.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void checkUserRole(String userId) {
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    navigateToUserProfile();
                } else {
                    databaseReference.child("mechanics").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String mechanicID = userId;
                                navigateToMechanicProfile(mechanicID);
                            } else {
                                Toast.makeText(login.this, "Role not found. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login.this, "Database error. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Database error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToUserProfile() {
        startActivity(new Intent(this, Dashboard.class));
        finish();
    }

    private void navigateToMechanicProfile(String mechanicID) {
        Intent intent = new Intent(this, mechanicprofile.class);
        intent.putExtra("mechanicID", mechanicID); // Pass the mechanicID
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
