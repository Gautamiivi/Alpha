package com.mycode.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    Context context;
    List<Alpha>  alphaList;

    public ProfileAdapter(Context context, List<Alpha> alphaList) {
        this.context = context;
        this.alphaList = alphaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_recycler_object,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Alpha currentAlpha = alphaList.get(position);
        String imageUrl = currentAlpha.getImageUrl();
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return alphaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.P_imageView);
        }
    }
}
