package com.example.githubusersearch.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.githubusersearch.R;
import com.example.githubusersearch.models.User;
import com.example.githubusersearch.utils.Utils;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private Activity context;
    private List<User> data;
    private int size;

    public SearchUserAdapter(Activity context, List<User> data){
        this.context = context;
        this.data = data;
        size = Utils.getDisplayWidth(context) / 7;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_search_user, parent, false);
        return new SearchUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
        User item = data.get(position);

        holder.tvUsersName.setText(item.getLogin());

        Glide.with(context)
                .load(item.getAvatarUrl())
                .apply(new RequestOptions()
                        .override(size, size)
                .placeholder(R.mipmap.ic_launcher_round))
                .into(holder.ivUsersAvatar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsersName;
        ImageView ivUsersAvatar;

        public SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsersName = itemView.findViewById(R.id.tvUsersName);
            ivUsersAvatar = itemView.findViewById(R.id.ivUsersAvatar);
        }
    }
}
