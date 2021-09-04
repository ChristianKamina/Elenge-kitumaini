package com.youthfimodd.elenges.custom.chat_model;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    //
    private static final int MSG_TITLE_LRFT = 0;
    private static final int MSG_TITLE_RIGHT = 1;
    //
    private Context mContext;
    private List<Chat> mChats;
    private String Imageurl;
    //
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChats, String Imageurl){
        //
        this.mContext = mContext;
        this.mChats = mChats;
        this.Imageurl = Imageurl;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TITLE_RIGHT) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(mView);
        }else {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(mView);
        }
    }
    //accuser de reception
    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {
        //
        final Chat chat = mChats.get(position);

        if (Imageurl.equals("default")){
            holder.profil_users.setImageResource(R.mipmap.ic_launcher_profil2);
        }else {
            Glide.with(mContext).load(Imageurl).into(holder.profil_users);
        }
        //
        if (chat.getType().equals("text")){
            //text message
            holder.show_message.setVisibility(View.VISIBLE);
            holder.message_img.setVisibility(View.GONE);
            holder.message_time.setText(timestampstoString((Long) chat.getDatatime()));

            holder.show_message.setText(chat.getMessage());

        }else if (chat.getType().equals("voice")) {
            //voice message
            holder.show_message.setVisibility(View.GONE);
            holder.message_img.setVisibility(View.GONE);
            holder.audioLayout.setVisibility(View.VISIBLE);
            holder.message_time.setText(timestampstoString((Long) chat.getDatatime()));

            //recuperation de l'audio

        }else {
            //image message
            holder.show_message.setVisibility(View.GONE);
            holder.message_img.setVisibility(View.VISIBLE);
            holder.message_time.setText(timestampstoString((Long) chat.getDatatime()));
            Picasso.with(mContext).load(chat.getMessage()).placeholder(R.drawable.ic_image_black).into(holder.message_img);

            /*-Click view image-*/
            holder.message_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater = dialog.getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialog_view_image, null);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.show();

                    ImageView Chat_img_dialog = dialog.findViewById(R.id.Chat_img_dialog);
                    Chat_img_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ImageView view_chat_image = dialog.findViewById(R.id.view_chat_image);
                    Picasso.with(mContext).load(chat.getMessage()).placeholder(R.drawable.ic_image_black).into(view_chat_image);

                    Toast.makeText(mContext, "Image click", Toast.LENGTH_SHORT).show();
                }
            });

        }
    /*delete mesage
    holder.messageLayout.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        PopupMenu menu = new PopupMenu(mContext, holder.messageLayout, Gravity.START);
        menu.getMenuInflater().inflate(R.menu.menu_copdelpast, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {

            int pos = item.getItemId();

            if (pos == R.id.copy){
              Toast.makeText(mContext, "Cpier", Toast.LENGTH_SHORT).show();
            }else if (pos == R.id.coller){
              Toast.makeText(mContext, "Coller", Toast.LENGTH_SHORT).show();
            }else if (pos == R.id.delete){
              deleteMessage(position);
            }
            return true;
          }
        });
        menu.show();
        return false;
      }
    });*/

        if (position == mChats.size()-1){
            if (chat.isIssoon()){
                holder.img_see.setImageResource(R.drawable.skip_icon);
            }else {
                holder.img_see.setImageResource(R.drawable.ic_skip_send);
            }
        }else {
            holder.img_see.setVisibility(View.GONE);
        }
    }
  /*delete message
  private void deleteMessage(int position) {
    final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String msgTimeStamp = (String) mChats.get(position).getDatatime();
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Chats");
    Query query = dbReference.orderByChild("datatime").equalTo(msgTimeStamp);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()){

          String key = ds.getRef().getKey();

          if (ds.child(key).getValue().equals(myUID)){
            //Remove the message from chats
            ds.getRef().removeValue();
            //Set the value of message "This message was delete"
            HashMap<String, Object>  hashMap = new HashMap<>();
            hashMap.put("message", "Ce message est supprimer...");
            ds.getRef().updateChildren(hashMap);

            Toast.makeText(mContext, "Message supprimer...", Toast.LENGTH_SHORT).show();
          }else {
            Toast.makeText(mContext, "Impossible de supprimer ce message...", Toast.LENGTH_SHORT).show();
          }

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }*/

    @Override
    public int getItemCount() {
        return mChats.size();
    }
    //
    class ViewHolder extends RecyclerView.ViewHolder{
        //
        TextView show_message, message_time;
        ImageView profil_users, message_img, message_voicePlay, message_voicePause, img_see;
        SeekBar seekBar;
        LinearLayout audioLayout;
        RelativeLayout messageLayout; /*for click listener to show delete*/
        //
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            show_message = itemView.findViewById(R.id.Chat_txt);
            message_time = itemView.findViewById(R.id.txt_time);
            message_img = itemView.findViewById(R.id.Chat_img);
            profil_users = itemView.findViewById(R.id.Chat_profil_image);
            img_see = itemView.findViewById(R.id.txt_see);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            audioLayout = itemView.findViewById(R.id.lineVoice);
            message_voicePlay = itemView.findViewById(R.id.Chat_voicePlay);
            message_voicePause = itemView.findViewById(R.id.Chat_voicePause);
            seekBar = itemView.findViewById(R.id.Chat_voice);
        }
    }
    //
    private String timestampstoString(Long timestamp) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format(" HH:mm ", calendar).toString();
        return date;
    }
    //
    @Override
    public int getItemViewType(int position) {
        //
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TITLE_RIGHT;
        }else {
            return MSG_TITLE_LRFT;
        }
    }
}
