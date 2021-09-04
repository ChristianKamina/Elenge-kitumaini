package com.youthfimodd.elenges.custom.notifications;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youthfimodd.elenges.ChatActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.custom.chat_model.Chat;
import com.youthfimodd.elenges.custom.chat_model.Users;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Chat_Notification extends RecyclerView.Adapter<Adapter_Chat_Notification.ViewHolder>{

    private Context mContext;
    private List<Chat> mNotificationList; /*-utiliser Chat-*/
    private boolean Issee;

    public Adapter_Chat_Notification(Context mContext, List<Chat> mNotificationList, boolean Issee) {
        this.mContext = mContext;
        this.mNotificationList = mNotificationList;
        this.Issee = Issee;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new Adapter_Chat_Notification.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Chat notification = mNotificationList.get(position);
        DatabaseReference reference;

        holder.txt_dateTime.setText(timestampstoString((Long) notification.getDatatime()));

        final String name_user = notification.getSender();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(name_user);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                holder.txt_Username.setText(user.getName());
                if (user.getImage().equals("default")){
                    holder.img_UserImage.setImageResource(R.mipmap.ic_launcher_profil2);
                }else {
                    //add this
                    Glide.with(mContext).load(user.getImage()).into(holder.img_UserImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtMessage.setText(notification.getMessage());

        holder.Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", name_user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(intent);
            }
        });

        /*if (position == mNotificationList.size()-1){
            if (notification.isIssoon()){
                holder.img_on.setImageResource(R.drawable.skip_icon);
            }else {
                holder.img_on.setImageResource(R.drawable.ic_skip_send);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout Notification;
        private TextView txt_dateTime, txt_Username, txtMessage;
        private CircleImageView img_UserImage, img_on;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Notification = itemView.findViewById(R.id.Notification);
            txt_dateTime = itemView.findViewById(R.id.notification_time);
            txt_Username = itemView.findViewById(R.id.notif_username);
            txtMessage = itemView.findViewById(R.id.notif_message);
            img_on = itemView.findViewById(R.id.img_on);
            img_UserImage = itemView.findViewById(R.id.notif_img);
        }
    } //
    private String timestampstoString(Long timestamp) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyy à HH:mm ", calendar).toString();
        return date;
    }
}
