package Backup.QuickFix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome extends AppCompatActivity {

    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        loadingProgressBar = findViewById(R.id.loadingProgressBar);


        new Handler().postDelayed(() -> {

            loadingProgressBar.setVisibility(View.GONE);

            Intent intent = new Intent(welcome.this, login.class);
            startActivity(intent);
            finish();
        }, 4000); //
    }
}