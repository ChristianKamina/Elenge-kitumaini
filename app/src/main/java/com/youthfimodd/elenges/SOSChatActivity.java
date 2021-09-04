package com.youthfimodd.elenges;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.youthfimodd.elenges.custom.chat_model.Chat;
import com.youthfimodd.elenges.custom.chat_model.Data;
import com.youthfimodd.elenges.custom.chat_model.MessageAdapter;
import com.youthfimodd.elenges.custom.chat_model.Sender;
import com.youthfimodd.elenges.custom.chat_model.Token;
import com.youthfimodd.elenges.custom.chat_model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SOSChatActivity extends AppCompatActivity {
    //
    private CircleImageView profil_image;
    private TextView username, userStatut;
    private ImageView btnSend;
    private ImageView emojis;
    private EditText txtSend;
    private RecyclerView recyclerView;
    //
    private MessageAdapter messageAdapter;
    private List<Chat> mChat;
    //volley request  queue notification
    private RequestQueue requestQueue;
    private  boolean notify = false;
    //
   private String userid ;
   private Intent intent;

    private DatabaseReference reference;
    private ValueEventListener eventListener;
    private DatabaseReference mUserDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soschat);
        //toolbar
        /*Toolbar toolbar = findViewById(R.id.toolbar1);
        getSupportActionBar().setTitle("SOS Messagerie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //
        recyclerView = findViewById(R.id.Chat_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        //
        profil_image = findViewById(R.id.toolbarimage);
        username = findViewById(R.id.usersname);
        userStatut = findViewById(R.id.UserStatut);
        btnSend = findViewById(R.id.btn_send);
        txtSend = findViewById(R.id.txt_message);
        emojis = findViewById(R.id.btn_emoji);

        findViewById(R.id.tchat_back).setOnClickListener(v -> finish());

        //fire base
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("ChatsSOS");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //
        intent = getIntent();
        userid = intent.getStringExtra("SOS_user_id");/*vient de MapsActivity*/
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        //recuperer le nom et l'image
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getName());
                /* check typing statut*/
                String typingStatus = (String) dataSnapshot.child("typingTo").getValue();

                if (typingStatus.equals(firebaseUser.getUid())){
                    userStatut.setText("écrit...");
                }else {
                    /*ajout time offline*/
                    String Statut = (String) dataSnapshot.child("timeStatus").getValue();

                    if (Statut.equals("En ligne")){
                        userStatut.setText(user.getTimeStatus());
                    }else {
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(Statut));
                        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
                        String Time = DateFormat.format("hh:mm ss", cal).toString();
                        userStatut.setText("Vu le "+ date + " à " + Time);
                    }
                    /*end ajout*/
                }
                //
                if (user.getImage().equals("default")){
                    profil_image.setImageResource(R.mipmap.ic_launcher_profil2);
                }else {
                    //add this
                    Glide.with(getApplicationContext()).load(user.getImage()).into(profil_image);
                }
                readMessage(firebaseUser.getUid(), userid, user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //button send message
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                notify = true;

                String meg = txtSend.getText().toString();
                if (!meg.equals("")){
                    SendMessage(firebaseUser.getUid(), userid, meg);
                }else {
                    Toast.makeText(SOSChatActivity.this, "Tu ne peux pas envoyer un message vide!", Toast.LENGTH_SHORT).show();
                }
                txtSend.setText("");
            }
        });
        seeMessage(userid);
    }

    private void seeMessage(final String usersid){
        //
        reference = FirebaseDatabase.getInstance().getReference("ChatsSOS");
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(usersid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Issoon", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SendMessage(String sender, final String receiver, final String message){
        //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Object datatime = ServerValue.TIMESTAMP;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("datatime", datatime);
        hashMap.put("type", "text");
        hashMap.put("Issoon", false);
        //
        reference.child("ChatsSOS").push().setValue(hashMap);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                Users users = dataSnapshot.getValue(Users.class);
                if (notify){
                    sendNotification(receiver, users.getName(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(),
                            R.mipmap.ic_icon_eleng,
                            username + "\n"+message,
                            " SOS Message ",
                            userid,
                            "MessageSOS");

                    Sender sender = new Sender(data, token.getToken());

                    //fcm json object request
                    try {
                        JSONObject senderJsonObject = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //response of the request
                                        Log.d("JSON_RESPONSE", "onResponse: "+response.toString());
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: "+error.toString());

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                //put params
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAA6-_uMK8:APA91bEdce3idooVxM0JWPhs5fhsS6NVMv4lhndKV8HQIe65nqbeIbARz3cslJioGukBB1CvQY1Hgf-sL6HkIDwLEnY62VvBbkoaVzIW-rGbj40Wl-Pu-rqJoMpbPbc410iEv9_nfSTd");

                                return headers;
                            }
                        };
                        //add this request to queue
                        requestQueue.add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readMessage(final String myid, final String userid, final String imageurl) {
        //
        mChat = new ArrayList<>();
        //
        reference = FirebaseDatabase.getInstance().getReference("ChatsSOS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                //
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {

                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(SOSChatActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
