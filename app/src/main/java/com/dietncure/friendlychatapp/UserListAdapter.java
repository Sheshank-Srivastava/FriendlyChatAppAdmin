package com.dietncure.friendlychatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    private Context context;
    ArrayList<UserListModel> mData;
    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
    DatabaseReference storageReference,listread;

    public UserListAdapter(Context context, ArrayList<UserListModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_list,parent,false);
        final UserListViewHolder holder = new UserListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        final UserListModel model = mData.get(position);
        Log.d("data at",position+"-"+model.getTimeStamp()+"-"+model.getuid()+"-"+model.getName()+"-"+model.isRead());
        holder.textView.setText(model.getuid() + "");
        if (!model.isRead()) {
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference = firebaseDatabase.getReference().child("overview").child("8").child(model.getuid() + "");
                UserListModel mod = new UserListModel(model.getName(), true,model.getTimeStamp(), model.getuid());
                storageReference.setValue(mod);
                model.setRead(true);
                notifyDataSetChanged();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("ClientId", model.getuid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        View view;
        public UserListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtClientName);
            view = itemView.findViewById(R.id.isRead);
        }
    }
}
