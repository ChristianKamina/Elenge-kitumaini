package com.youthfimodd.elenges.custom.chat_model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youthfimodd.elenges.ChatActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.StatutActivity;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    //
    private Context mContext;
    private List<Users> mUsers;
    private boolean Ischat;

    String theLastMessage;
    //
    public UsersAdapter(Context mContext, List<Users> mUsers, boolean Ischat){
        //
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.Ischat = Ischat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.users_items, parent, false);
        return new UsersAdapter.ViewHolder(mView);
    }
    //
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //
        final Users users = mUsers.get(position);

        holder.users_name.setText(users.getName());
        holder.last_msg.setText(users.getPhone());
        holder.typeuser.setText(users.getTypeusesr());
        if (users.getImage().equals("default")){
            holder.profil_users.setImageResource(R.mipmap.ic_launcher_profil2);
        }else {
            Glide.with(mContext).load(users.getImage()).into(holder.profil_users);
        }
        //last message
        if (Ischat){
            lastMessage(users.getId(), holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }
        //
        if (Ischat){
            if (users.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", users.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(intent);

            }
        });
        holder.profil_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StatutActivity.class);

                Users user = mUsers.get(position);
                intent.putExtra("Id", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getImage());

                String timestamp = user.getTimeStatus();
                intent.putExtra("timeStatus", timestamp);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                mContext.startActivity(intent);
            }
        });
    }
    //
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    //
    public class ViewHolder extends RecyclerView.ViewHolder{
        //
        private TextView users_name;
        private TextView last_msg;
        private TextView typeuser;
        private ImageView profil_users;
        private ImageView img_on;
        private ImageView img_off;
        //
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            users_name = itemView.findViewById(R.id.users_name);
            profil_users = itemView.findViewById(R.id.profil_image);
            last_msg = itemView.findViewById(R.id.last_msg);
            typeuser = itemView.findViewById(R.id.users_type);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }
    //check for last message
    public void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        if (chat.getType().equals("image")){
                            theLastMessage =  R.drawable.ic_image_black+" Photo" ;
                        }else {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                //
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
