package com.example.godee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.godee.Driver.Driver.ModelClass.DriveSession;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<DriveSession> driveSessionList;

    // Constructor
    public HistoryAdapter(List<DriveSession> driveSessionList) {
        this.driveSessionList = driveSessionList;
    }

    // ViewHolder inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionId, tvDriverId, tvUserId, tvStartLocation, tvEndLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSessionId = itemView.findViewById(R.id.tvSessionId);
            tvDriverId = itemView.findViewById(R.id.tvDriverId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvStartLocation = itemView.findViewById(R.id.tvStartLocation);
            tvEndLocation = itemView.findViewById(R.id.tvEndLocation);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriveSession session = driveSessionList.get(position);
        holder.tvSessionId.setText(session.getSessionID());
        holder.tvDriverId.setText(session.getDriverID());
        holder.tvUserId.setText(session.getUserID());
        holder.tvStartLocation.setText("Lat: " + session.getStartLocationLatitude() + ", Long: " + session.getStartLocationLongitude());
        holder.tvEndLocation.setText("Lat: " + session.getEndLocationLatitude() + ", Long: " + session.getEndLocationLongitude());
    }

    @Override
    public int getItemCount() {
        return driveSessionList.size();
    }

}
