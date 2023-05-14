package com.example.myatmos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myatmos.entities.Station;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {
    private final List<Station> stations;
    public class StationViewHolder extends RecyclerView.ViewHolder {
        public TextView Address;
        public TextView Pm10;
        public TextView Pm25;

        public StationViewHolder(View itemView) {
            super(itemView);
            Address = itemView.findViewById(R.id.address);
            Pm10 = itemView.findViewById(R.id.pm10);
            Pm25 = itemView.findViewById(R.id.pm25);
        }
    }

    public StationAdapter(List<Station> stations) {
        this.stations = stations;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_item, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.Address.setText(station.getAddress());

        Context context = holder.itemView.getContext();

        if (Float.parseFloat(station.getPm10()) < 20) {
            holder.Pm10.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_success));
            holder.Pm10.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (Float.parseFloat(station.getPm10()) < 50) {
            holder.Pm10.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_warning));
            holder.Pm10.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (Float.parseFloat(station.getPm10()) < 150) {
            holder.Pm10.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_attention));
            holder.Pm10.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.Pm10.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_danger));
            holder.Pm10.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.Pm10.setText(String.format(station.getPm10()));

        if (Float.parseFloat(station.getPm25()) < 10) {
            holder.Pm25.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_success));
            holder.Pm25.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (Float.parseFloat(station.getPm25()) < 25) {
            holder.Pm25.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_warning));
            holder.Pm25.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (Float.parseFloat(station.getPm25()) < 75) {
            holder.Pm25.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_attention));
            holder.Pm25.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.Pm25.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_danger));
            holder.Pm25.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.Pm25.setText(String.format(station.getPm25()));
    }

    @Override
    public int getItemCount() {
        if(stations == null)
            return 0;
        return stations.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Station item);
    }
}
