package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.custom.forum_models.Cmnt;
import com.youthfimodd.elenges.custom.forum_models.CmntAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogcmntActivity extends AppCompatActivity  {

    TextView txtDesc, txtdatepost, txtname, txtsendCmnt, txt_number_like, txt_number_comment, txt_user_status;
    ImageView Imgpost;
    CircleImageView Imguser;
    RecyclerView CmntView;
    EditText txtCmnt;
    private boolean isHideToolbarView = false;

    CmntAdapter cmntAdapter;
    List<Cmnt> mCmnt;
    String posKey;

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseDatabase firebaseDatabase;

    private DatabaseReference mDatabaseUsers;

    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogcmnt);
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar1);
        getSupportActionBar().setTitle("Commentaires");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        //
        Imgpost = findViewById(R.id.blogCmnt_img);
        txtDesc = findViewById(R.id.blog_descrip);
        txtdatepost = findViewById(R.id.txt_user_time);
        txtname = findViewById(R.id.blog_nameUpost);
        Imguser = findViewById(R.id.blog_img_user);
        txtsendCmnt = findViewById(R.id.blogCmnt_Add);
        txtCmnt = findViewById(R.id.editCmnt);
        txt_number_like = findViewById(R.id.txt_number_all);
        txt_number_comment = findViewById(R.id.txt_number_comment);
        txt_user_status = findViewById(R.id.txt_user_status);

        txtCmnt.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0){
                    //showSendButtom();
                    txtsendCmnt.setTextColor(getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    //offSendButtom();
                    txtsendCmnt.setTextColor(getColor(R.color.tabIndicatorGray));
                }
            }
        });


        txtsendCmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtsendCmnt.setVisibility(View.INVISIBLE);
                //
                DatabaseReference commentference = firebaseDatabase.getReference("BlogComnt").child(posKey);

                final DatabaseReference newPost = commentference.push();

                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Object timestamps = ServerValue.TIMESTAMP;
                        String commentaire = txtCmnt.getText().toString().trim();

                        if (TextUtils.isEmpty(commentaire)){

                            txtCmnt.setError("Champ vide!!!");
                            txtsendCmnt.setVisibility(View.VISIBLE);

                        }else if (!TextUtils.isEmpty(commentaire)) {

                            newPost.child("Comment").setValue(txtCmnt.getText().toString());
                            newPost.child("descrip").setValue(txtDesc.getText().toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("timestamps").setValue(timestamps);
                            newPost.child("userimg").setValue(dataSnapshot.child("image").getValue());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue());

                            //reaction apres avoir commenter
                            //showMessage(" Commentaire ajouté...");
                            txtCmnt.setText("");
                            txtsendCmnt.setVisibility(View.VISIBLE);
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        //prendre les details sur le post venant de forum
        String postStatus = getIntent().getExtras().getString("Status");
        txt_user_status.setText(postStatus);

        if (txt_user_status.equals("text")){
            Imgpost.setVisibility(View.GONE);
        } else if (txt_user_status.equals("textimage")){
            Imgpost.setVisibility(View.GONE);
        }

        String postImage = getIntent().getExtras().getString("PostImage");
        Picasso.with(this).load(postImage).into(Imgpost);

        String postDesc = getIntent().getExtras().getString("Descrip");
        txtDesc.setText(postDesc);

        String nameUpost = getIntent().getExtras().getString("UserName");
        txtname.setText(nameUpost);

        String ImgUser = getIntent().getExtras().getString("UserImg");
        Glide.with(this).load(ImgUser).into(Imguser);

        posKey = getIntent().getExtras().getString("postKey");
        /*times*/
        String date = timestampstoString(getIntent().getExtras().getLong("timestamp"));
        txtdatepost.setText(date);
        //
        CmntView = findViewById(R.id.recyclerView2);
        CmntView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        CmntView.setLayoutManager(layoutManager);

        /*-Number comment-*/
        DatabaseReference referenceCmnt = FirebaseDatabase.getInstance().getReference("BlogComnt").child(posKey);

        referenceCmnt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_number_comment.setText(dataSnapshot.getChildrenCount() + " Commentaire(s)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*-Number like-*/
        DatabaseReference referenceLike = FirebaseDatabase.getInstance().getReference().child("Likes").child(posKey);
        referenceLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_number_like.setText(dataSnapshot.getChildrenCount() + " j'aime(s)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*--*/
        /*Init recycleview*/
        initRvCmnt();
    }

    /*private void offSendButtom() {
        sendCmnt.setClickable(false);
        sendCmnt.setVisibility(View.GONE);
        SendComntVoice.setVisibility(View.VISIBLE);
    }

    private void showSendButtom() {
        sendCmnt.setClickable(true);
        sendCmnt.setVisibility(View.VISIBLE);
        SendComntVoice.setVisibility(View.GONE);
    }*/

    private void initRvCmnt() {

        CmntView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference cmmentRef = firebaseDatabase.getReference("BlogComnt").child(posKey);
        cmmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mCmnt = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Cmnt comment = snapshot.getValue(Cmnt.class);
                    mCmnt.add(comment);
                }

                cmntAdapter = new CmntAdapter(getApplicationContext(), mCmnt);
                CmntView.setAdapter(cmntAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //
    private String timestampstoString(long times){
        //
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(times);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
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
}
