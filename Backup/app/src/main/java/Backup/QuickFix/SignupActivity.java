package Backup.QuickFix;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Backup.QuickFix.model.Mechanic;
import Backup.QuickFix.model.User;

public class SignupActivity extends AppCompatActivity {
    private EditText Signup_name, Signup_email, Signup_username, Signup_password;
    private TextView Loginredirect;
    private RadioButton RadioUser, RadioMechanic;
    private Button Signup_button;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // views
        Signup_name = findViewById(R.id.name);
        Signup_email = findViewById(R.id.signup_email);
        Signup_username = findViewById(R.id.signup_username);
        Signup_password = findViewById(R.id.signup_password);
        Signup_button = findViewById(R.id.signup_button);
        RadioUser = findViewById(R.id.radioUser);
        RadioMechanic = findViewById(R.id.radioMechanic);
        Loginredirect = findViewById(R.id.loginRedirectText);

        // toggle
        Signup_password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (Signup_password.getRight() - Signup_password.getCompoundDrawables()[2].getBounds().width())) {
                    Signup_password.setInputType(Signup_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ?
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD :
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    return true;
                }
            }
            return false;
        });

        // Redirect
        Loginredirect.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, login.class));
            finish();
        });

        // Signup button logic
        Signup_button.setOnClickListener(v -> {
            String name = Signup_name.getText().toString().trim();
            String email = Signup_email.getText().toString().trim();
            String username = Signup_username.getText().toString().trim();
            String password = Signup_password.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            String role = RadioMechanic.isChecked() ? "Mechanic" : "User";
                            DatabaseReference userRef = databaseReference.child(role.toLowerCase() + "s").child(userId);

                            if (role.equals("Mechanic")) {

                                Mechanic mechanic = new Mechanic(userId, name, username, email, role);
                                userRef.setValue(mechanic).addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        Toast.makeText(this, "Mechanic account created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(this, Dataform.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Failed to save mechanic data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                User user = new User(name, username, email, role);
                                userRef.setValue(user).addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        Toast.makeText(this, "User account created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(this, "Signup Failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}
