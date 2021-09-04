package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.youthfimodd.elenges.custom.chat_model.Chat;
import com.youthfimodd.elenges.custom.chat_model.Data;
import com.youthfimodd.elenges.custom.chat_model.MessageAdapter;
import com.youthfimodd.elenges.custom.chat_model.Sender;
import com.youthfimodd.elenges.custom.chat_model.Token;
import com.youthfimodd.elenges.custom.chat_model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    //
    private CircleImageView profil_image;
    private TextView username, userStatut;/* tout ce qui concerne userStatut est un ajout*/
    private FloatingActionButton btnSend, btnSendVoice;
    private ImageView mic, emojis;
    private Chronometer msgVoice;
    private EditText txtSend;
    private RecyclerView recyclerView;
    private LinearLayout lineStatut;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference storage;
    private StorageTask uploadTask;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri ImageUrl;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    List<Users> users = new ArrayList<>();

    //volley request  queue notification
    private RequestQueue requestQueue;
    private  boolean notify = false;

    private MediaRecorder recorder;
    private String Filename = null;
    private static final String LOG_TAG = "Record_log";

    String userid ;
    Intent intent;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //toolbar
        /*Toolbar toolbar = findViewById(R.id.toolbar1);
        getSupportActionBar().setTitle("Messagerie");
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.Chat_back).setOnClickListener(v -> finish());
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
        mic = findViewById(R.id.SendMic);
        btnSendVoice = findViewById(R.id.btn_sendVoice);
        txtSend = findViewById(R.id.txt_message);
        msgVoice = findViewById(R.id.msg_voice);
        emojis = findViewById(R.id.btn_emoji);

        Filename = Environment.getDataDirectory().getAbsolutePath();
        Filename += "/messsage_voice.3gp";
        
        //store picture in fire base
        storage = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Chats");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        findViewById(R.id.lineStatut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatActivity.this, "Toucher!", Toast.LENGTH_SHORT).show();
            }
        });
        //emojis
        emojis.setOnClickListener(v -> Toast.makeText(ChatActivity.this, "Emojis", Toast.LENGTH_SHORT).show());
        /*-insert image-*/
        findViewById(R.id.img_send).setOnClickListener(v -> OpenGallery());
        //Send voice
        btnSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "Audio ....", Toast.LENGTH_SHORT).show();
            }
        });
        //
        intent = getIntent();
        userid = intent.getStringExtra("user_id");/*vient de userAdapter. et de la classe MyFirebaseMessaging*/
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        //recuperer le nom et image
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String getCurrentDateTime = sdf.format(c.getTime());
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
                        Calendar cal = Calendar.getInstance(Locale.FRANCE);
                        cal.setTimeInMillis(Long.parseLong(Statut));
                        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
                        String Time = DateFormat.format("HH:mm", cal).toString();/*-poser une condition-*/
                        if (getCurrentDateTime.compareTo(date) == 0){
                            userStatut.setText("vu aujourd'hui à " + Time);
                        }else if(getCurrentDateTime.compareTo(date) == 1){
                            userStatut.setText("vu hier à " + Time);
                        } else {
                            userStatut.setText("vu le "+ date + " à " + Time);
                        }
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
        //buttom send message
        btnSend.setOnClickListener(view -> {
            //
            notify = true;

            String meg = txtSend.getText().toString();
            if (!meg.equals("")){
                SendMessage(firebaseUser.getUid(), userid, meg);
            }else {
                Toast.makeText(ChatActivity.this, "Tu ne peux pas envoyer un message vide!", Toast.LENGTH_SHORT).show();
            }
            txtSend.setText("");
        });
        //check edit text change listener
        txtSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0){
                    btnSend.setVisibility(View.GONE);
                    btnSendVoice.setVisibility(View.VISIBLE);

                    checkTypingStatus("noOne");
                }else {
                    btnSend.setVisibility(View.VISIBLE);
                    btnSendVoice.setVisibility(View.GONE);
                    checkTypingStatus(userid); //Uid of receive
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        seeMessage(userid);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(Filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        uploadVoice();

    }
    //debut code Send image
    //
    private void OpenGallery() {
        //
        Intent gallery_intent = new Intent();
        gallery_intent.setType("image/*");
        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery_intent, "SELECT IMAGE"), GALLERY_REQUEST_CODE);
    }
    //
    private void uploadVoice() {
        notify = true;

        StorageReference filevoice = storage.child("message_audio").child("new_audio.3gp");

        Uri uriVoice = Uri.fromFile(new File(Filename));

        filevoice.putFile(uriVoice).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

            while (!uriTask.isSuccessful()) ;
            String daownloadUri = uriTask.getResult().toString();

            if (!uriTask.isSuccessful()){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Object datatime = ServerValue.TIMESTAMP;

                HashMap<String, Object> map = new HashMap<>();
                map.put("sender", firebaseUser.getUid());
                map.put("receiver", userid);
                map.put("message", daownloadUri);
                map.put("datatime", datatime);
                map.put("type", "voice");
                map.put("Issoon", false);
                //put this to fire base
                databaseReference.child("Chats").push().setValue(map);

                //send notification
                DatabaseReference databas = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                databas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users users = dataSnapshot.getValue(Users.class);
                        if (notify) {

                            sendNotification(userid, users.getName(), "Envoie Audio");
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //add this in chat activity
                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                        .child(firebaseUser.getUid())
                        .child(userid);

                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            chatRef.child("id").setValue(userid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //
    private void uplaodImage(){
        //
        notify = true;

        final ProgressDialog pb= new ProgressDialog(this);
        pb.setMessage("Envoie...");
        pb.setCanceledOnTouchOutside(false);
        pb.show();

        if (ImageUrl != null) {
            final StorageReference filepath = storage.child("message_image").child(ImageUrl.getLastPathSegment());

            filepath.putFile(ImageUrl).addOnSuccessListener(taskSnapshot -> {
                pb.dismiss();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful()) ;
                String daownloadUri = uriTask.getResult().toString();

                if (uriTask.isSuccessful()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Object datatime = ServerValue.TIMESTAMP;

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("sender", firebaseUser.getUid());
                    map.put("receiver", userid);
                    map.put("message", daownloadUri);
                    map.put("datatime", datatime);
                    map.put("type", "image");
                    map.put("Issoon", false);
                    //put this to fire base
                    databaseReference.child("Chats").push().setValue(map);

                    //send notification
                    DatabaseReference databas = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                    databas.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            if (notify) {

                                sendNotification(userid, users.getName(), "Envoie photo");
                            }
                            notify = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //add this in chat activity
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                            .child(firebaseUser.getUid())
                            .child(userid);

                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                chatRef.child("id").setValue(userid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed
                            pb.dismiss();
                        }
                    });
        }else {
            Toast.makeText(ChatActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            ImageUrl = data.getData();

            CropImage.activity(ImageUrl)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(ChatActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uplaodImage();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }
    }
    //fin code Send image
    private void seeMessage(final String usersid){
        //
        reference = FirebaseDatabase.getInstance().getReference("Chats");
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
    //
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
        reference.child("Chats").push().setValue(hashMap);

        //add this in chat activity
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //pour la notification
       // final String msg = message;

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
    //pour la notification
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
                            R.mipmap.logo_elenge, ""+username +" \n "+message,
                            " Nouveau message ",
                            userid,
                            "MessageChat");

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
                                headers.put("Authorization", "key=AAAAaboduLQ:APA91bFq35bKePxH1tK7FD6EwhglwhcXpCYWnfCMZoin7sjIWjuXI_cqh-nlDcFXcP2s3xjM4t5ZYAaEmKFFtGLO1t6XWjWfbBdhx0YVv2u99MHhTnRcCBMDgkhi_2P6iscqueZ_R65A");

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
    //lire les messages
    public void readMessage(final String myid, final String userid, final String imageurl) {
        //
        mChat = new ArrayList<>();
        //
        reference = FirebaseDatabase.getInstance().getReference("Chats");
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

                    messageAdapter = new MessageAdapter(ChatActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PRETS", MODE_PRIVATE).edit();
        editor.putString("currentUser", userid);
        editor.apply();
    }
    //
    public void status(String status){
        //
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        //
        reference.updateChildren(hashMap);
    }
    //
    public void timeStatut(String timestatut){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> TimehashMap = new HashMap<>();
        TimehashMap.put("timeStatus", timestatut);

        dataRef.updateChildren(TimehashMap);
    }
    //
    public void checkTypingStatus(String typing){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> TimehashMap = new HashMap<>();
        TimehashMap.put("typingTo", typing);

        dataRef.updateChildren(TimehashMap);
    }
    //
    /*onStart est un ajout*/
    @Override
    protected void onStart() {
        status("online");
        currentUser(userid);
        //
        timeStatut("En ligne");
        super.onStart();
    }
    //
    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        currentUser("none");
        //get timse tamp est un ajout
        String timestamp = String.valueOf(System.currentTimeMillis());
        //set offline with last times tamps
        timeStatut(timestamp);
        checkTypingStatus("noOne");
        reference.removeEventListener(eventListener);
    }
    //
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
        //
        timeStatut("En ligne");
    }
    //menu
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
}
