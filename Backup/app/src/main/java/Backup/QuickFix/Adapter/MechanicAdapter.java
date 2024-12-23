package Backup.QuickFix.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

import Backup.QuickFix.Dataform;
import Backup.QuickFix.ProfileActivity;
import Backup.QuickFix.R;
import Backup.QuickFix.model.Mechanic;

/*public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MechanicViewHolder> {

    private List<Mechanic> mechanicList;

    public MechanicAdapter(List<Mechanic> mechanicList) {
        this.mechanicList = mechanicList;
    }

    @NonNull
    @Override
    public MechanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mechanic, parent, false);
        return new MechanicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MechanicViewHolder holder, int position) {
        Mechanic mechanic = mechanicList.get(position);
        holder.userNameTextView.setText(mechanic.getUsername() != null ? mechanic.getUsername() : "No Name");
    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }

    public void updateMechanicList(List<Mechanic> mechanics) {
        this.mechanicList.clear();
        this.mechanicList.addAll(mechanics);
        notifyDataSetChanged();
    }

    public static class MechanicViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        public MechanicViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.user_name);
        }
    }
}*/

public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MechanicViewHolder> {

    private List<Mechanic> mechanicList;

    public MechanicAdapter(List<Mechanic> mechanicList) {
        this.mechanicList = mechanicList;
    }

    @NonNull
    @Override
    public MechanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mechanic, parent, false);
        return new MechanicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MechanicViewHolder holder, int position) {
        Mechanic mechanic = mechanicList.get(position);
        holder.userNameTextView.setText(mechanic.getUsername() != null ? mechanic.getUsername() : "No Name");

        // Handle View Details button click
        holder.viewDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra("mechanicID", mechanic.getMechanicID());  // Assuming `mechanicID` is available
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }

    public static class MechanicViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        Button viewDetailsButton;

        public MechanicViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.user_name);
            viewDetailsButton = itemView.findViewById(R.id.profile);  // Reference to the View Details button
        }
    }
}


