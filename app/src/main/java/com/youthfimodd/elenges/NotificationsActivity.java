package com.youthfimodd.elenges;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.youthfimodd.elenges.custom.chat_model.Chat;
import com.youthfimodd.elenges.custom.chat_model.ChatList;
import com.youthfimodd.elenges.custom.chat_model.Token;
import com.youthfimodd.elenges.custom.notifications.Adapter_Chat_Notification;

import java.util.ArrayList;
import java.util.List;

import aglibs.loading.skeleton.layout.SkeletonRelativeLayout;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerNotif;
    private Adapter_Chat_Notification adapterChatNotification;
    private List<Chat> mNotificationList;
    private List<ChatList> mChatLists;
    //firebase
    FirebaseUser fuser;
    DatabaseReference reference;
    LinearLayout lienLoadNotif;
    SkeletonRelativeLayout Loading_Notification;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        /*-Toolbar-*/
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*--*/
        lienLoadNotif = findViewById(R.id.lienLoadNotif);
        Loading_Notification = findViewById(R.id.Loading_Notification);
        recyclerNotif = findViewById(R.id.notif_recycle);
        recyclerNotif.setHasFixedSize(true);
        recyclerNotif.setLayoutManager(new LinearLayoutManager(this));
        //
        mNotificationList = new ArrayList<>();

        readChatList();
    }

    private void readChatList() {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mChatLists = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatlist = snapshot.getValue(ChatList.class);
                    mChatLists.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }
    //pour la notification
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
    //
    private void chatList() {
        mNotificationList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    for (ChatList chatlist : mChatLists){
                        assert chat != null;
                        if (chat.getSender().equals(chatlist.getId())){
                            lienLoadNotif.setVisibility(View.GONE);
                            recyclerNotif.setVisibility(View.VISIBLE);
                            mNotificationList.add(chat);
                        }
                    }
                }
                adapterChatNotification = new Adapter_Chat_Notification(getApplicationContext(), mNotificationList, true);
                recyclerNotif.setAdapter(adapterChatNotification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Option memu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Loading_Notification.startLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Loading_Notification.stopLoading();
    }
}