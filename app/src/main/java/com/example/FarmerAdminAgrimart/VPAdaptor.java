package com.example.FarmerAdminAgrimart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdaptor extends RecyclerView.Adapter<VPAdaptor.ViewHolder> {
    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    public VPAdaptor(ArrayList<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);
        holder.imageView.setImageResource(viewPagerItem.imageId);
        holder.heading.setText(viewPagerItem.heading);
        holder.description.setText(viewPagerItem.description);

        if (position == viewPagerItemArrayList.size() - 1) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT; // Full width
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Adjust height

            holder.imageView.setLayoutParams(layoutParams);
        } else {
            // Reset to normal size for other pages (optional)
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.imageView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView heading, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivimage);
            heading = itemView.findViewById(R.id.tvHeading);
            description = itemView.findViewById(R.id.tvDesc);
        }
    }

}
