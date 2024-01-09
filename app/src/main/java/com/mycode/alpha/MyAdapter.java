package com.mycode.alpha;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<Alpha> alphaList ;

    public MyAdapter(Context context, List<Alpha> alphaList) {
        this.context = context;
        this.alphaList = alphaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_object,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      Alpha currentAlpha = alphaList.get(position);
      holder.userName.setText(currentAlpha.getUserName());
      holder.caption.setText(currentAlpha.getCaption());
      String profileImageUrl = currentAlpha.getProfileImageUrl();
      String imageUrl = currentAlpha.getImageUrl();
      String timeAgo = (String) DateUtils.getRelativeTimeSpanString(currentAlpha
              .getTimeAdded().getSeconds()*1000);
      holder.timestamp.setText(timeAgo);

      Glide.with(context)
                      .load(profileImageUrl)
                              .fitCenter()
                                      .into(holder.profileImage);


        //glide lib to display the image
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.userImage);


    }

    @Override
    public int getItemCount() {
        return alphaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        ImageView userImage;
        ImageView profileImage;
        TextView timestamp;
        TextView caption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userImage = itemView.findViewById(R.id.userImage);
            profileImage = itemView.findViewById(R.id.profileImage);
            timestamp = itemView.findViewById(R.id.timestamp);
            caption = itemView.findViewById(R.id.caption);

        }
    }
}
