package com.example.chatfirebase_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase_final.Model.User;
import com.example.chatfirebase_final.R;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;


    private List<User> mUser;
    public UserAdapter(Context context, List<User> mUser) {
        this.context = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user=mUser.get(position);
        holder.mUser.setText(user.getUser());
        //set ảnh đại diện
        holder.profile_Image.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mUser;
        public ImageView profile_Image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUser=itemView.findViewById(R.id.UserN);
            profile_Image=itemView.findViewById(R.id.profile_image);
        }
    }
}
