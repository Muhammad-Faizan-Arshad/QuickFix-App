package Backup.QuickFix;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Backup.QuickFix.Adapter.MechanicAdapter;
import Backup.QuickFix.model.Mechanic;

public class Dashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MechanicAdapter mechanicAdapter;
    private List<Mechanic> mechanicList;
    private List<Mechanic> filteredMechanicList;
    private EditText searchEditText;
    private LinearLayout searchLayout;
    private ImageView searchIcon;
    private ImageView searchCloseIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title

        searchLayout = findViewById(R.id.search_layout);
        searchEditText = findViewById(R.id.search_edit_text);
        searchIcon = findViewById(R.id.search_icon);
        searchCloseIcon = findViewById(R.id.search_close_icon);

        ImageView logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Dashboard.this, login.class));
            finish();
        });

        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mechanicList = new ArrayList<>();
        filteredMechanicList = new ArrayList<>();
        mechanicAdapter = new MechanicAdapter(filteredMechanicList);
        recyclerView.setAdapter(mechanicAdapter);

        fetchMechanicsData();

        searchIcon.setOnClickListener(v -> {
            if (searchLayout.getVisibility() == View.GONE) {
                searchLayout.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
            }
        });

        searchCloseIcon.setOnClickListener(v -> {
            searchLayout.setVisibility(View.GONE);
            searchEditText.setText("");
            filteredMechanicList.clear();
            filteredMechanicList.addAll(mechanicList);
            mechanicAdapter.notifyDataSetChanged();
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMechanics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchMechanicsData() {
        DatabaseReference mechanicsRef = FirebaseDatabase.getInstance().getReference("mechanics");
        mechanicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mechanicList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mechanic mechanic = snapshot.getValue(Mechanic.class);
                    if (mechanic != null) {
                        mechanicList.add(mechanic);
                    }
                }
                filteredMechanicList.addAll(mechanicList);
                mechanicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Dashboard", "Database Error: " + error.getMessage());
                Toast.makeText(Dashboard.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMechanics(String text) {
        filteredMechanicList.clear();
        if (text.isEmpty()) {
            filteredMechanicList.addAll(mechanicList);
        } else {
            for (Mechanic mechanic : mechanicList) {
                if (mechanic.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredMechanicList.add(mechanic);
                }
            }
        }
        mechanicAdapter.notifyDataSetChanged();
    }
}
