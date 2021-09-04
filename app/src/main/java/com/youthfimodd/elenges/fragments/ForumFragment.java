package com.youthfimodd.elenges.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.BlogcmntActivity;
import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.NewPostTextActivity;
import com.youthfimodd.elenges.NewsPostActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.custom.forum_models.Blogzone;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    FloatingActionButton add_post_image_btn, add_post_text_btn;
    //
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLikes;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;

    private boolean mProcessLikes = false;

    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Nom dans Action Bar
        ((Main2Activity) requireActivity()).SetActionBarTitle("Je m'exprime");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        //
        mProgress = new ProgressDialog(getContext());

        add_post_text_btn = view.findViewById(R.id.add_post_text_btn);
        add_post_image_btn = view.findViewById(R.id.add_post_btn);

        mBlogList = (RecyclerView)view.findViewById(R.id.blog_listview);

       LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
       layoutManager.setReverseLayout(true);
       layoutManager.setStackFromEnd(true);

       mBlogList.setHasFixedSize(true);
       mBlogList.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();

        mDatabase.keepSynced(true);
        mDatabaseLikes.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        //
        add_post_text_btn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), NewPostTextActivity.class));
        });
        add_post_image_btn.setOnClickListener(v -> {
            //
            Intent intent = new Intent(getContext(), NewsPostActivity.class);
            startActivity(intent);
        });
        //databaseSante.keepSynced(true);
        mProgress.setMessage("Chargement...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        //
        return view;
    }
    //
    @Override
    public void onStart() {

        super.onStart();

        //mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Blogzone, BlogzoneViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogzone, BlogzoneViewHolder>(

                Blogzone.class,
                R.layout.custom_forum,
                BlogzoneViewHolder.class,
                mDatabase

        ) {

            @Override
            protected void populateViewHolder(final BlogzoneViewHolder viewHolder, final Blogzone model, final int position) {

                mProgress.dismiss();

                final String post_key = getRef(position).getKey().toString();

                viewHolder.setIsRecyclable(false);

                if (model.getStatus().equals("text")){

                    viewHolder.forum_image.setVisibility(View.GONE);
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setStatus(model.getStatus());
                    viewHolder.setUserName(model.getUsername());
                    viewHolder.setUserImage(getContext(), model.getUserImg());

                }else if (model.getStatus().equals("textimage")){

                    viewHolder.forum_image.setVisibility(View.VISIBLE);
                    viewHolder.setImageUrl(getContext(), model.getImageUrl());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setUserName(model.getUsername());
                    viewHolder.setUserImage(getContext(), model.getUserImg());
                }

                viewHolder.blogDate.setText(timestampstoString((long)model.getTimestamp()));
                //
                viewHolder.setLikesBtn(post_key);
                viewHolder.CountLike(post_key);
                viewHolder.CountComments(post_key);
                //pour le partage
                viewHolder.blogShare.setOnClickListener(view -> {
                    //
                    String share = viewHolder.setDesc(model.getDesc());
                    String shareimg = viewHolder.setImageUrl(getContext(), model.getImageUrl());

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, share );
                    //intent.putExtra(intent.EXTRA_TEXT, share);
                    intent.putExtra(intent.EXTRA_TEXT, "Partager via Elenge/forum: "+shareimg);
                    intent.setType("text/plain");
                    startActivity(intent);
                    //
                    Toast.makeText(getContext(), "Partage", Toast.LENGTH_SHORT).show();
                });

                //pour le commentaires
                viewHolder.blogCmnt.setOnClickListener(view -> {
                    //
                    //String descrip = viewHolder.setDesc(model.getDesc());

                    Intent intent = new Intent(getContext(), BlogcmntActivity.class);
                    intent.putExtra("postKey", post_key);
                    intent.putExtra("Descrip",  model.getDesc());
                    intent.putExtra("PostImage", model.getImageUrl());
                    intent.putExtra("UserName", model.getUsername());
                    intent.putExtra("UserImg", model.getUserImg());
                    intent.putExtra("Status", model.getStatus());
                    long timestamp = (long) model.getTimestamp();
                    intent.putExtra("timestamp", timestamp);

                    /*long timestamp = (long) model.getTimestamp();
                    intent.putExtra("postData", timestamp);*/

                    startActivity(intent);

                    Toast.makeText(getContext(), "Commentaire", Toast.LENGTH_SHORT).show();
                });
                //click sur l'image
                viewHolder.mView.setOnClickListener(view -> {
                    //
                    Intent intent = new Intent(getContext(), BlogcmntActivity.class);
                    intent.putExtra("postKey", post_key);
                    intent.putExtra("Descrip",  model.getDesc());
                    intent.putExtra("PostImage", model.getImageUrl());
                    intent.putExtra("UserName", model.getUsername());
                    intent.putExtra("Status", model.getStatus());
                    intent.putExtra("UserImg", model.getUserImg());
                    long timestamp = (long) model.getTimestamp();
                    intent.putExtra("timestamp", timestamp);

                    startActivity(intent);
                    //donne id du post
                   // Toast.makeText(getContext(), post_key, Toast.LENGTH_SHORT).show();
                });
                //like post
                viewHolder.mLikes.setOnClickListener(view -> {
                    //
                    mProcessLikes = true;

                        mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (mProcessLikes) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLikes.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                        mProcessLikes = false;

                                    } else {

                                        mDatabaseLikes.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue(true);

                                        mProcessLikes = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                });

            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    private String timestampstoString(long timestamp) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }

    //
    public static class BlogzoneViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView blogDate, like, blog_comnt_count, blog_status;
        private ImageView mLikes;
        private CircleImageView blogUserImage;
        private ImageView blogShare;
        private ImageView blogCmnt;
        private LinearLayout forum_image;

        DatabaseReference mDatabaseLikes;
        FirebaseAuth mAuth;

        public BlogzoneViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            forum_image = itemView.findViewById(R.id.forum_image);

            blog_status = itemView.findViewById(R.id.blog_status);

            blogCmnt = itemView.findViewById(R.id.blogCmont);
            mLikes = itemView.findViewById(R.id.blog_like_btn);

            like = itemView.findViewById(R.id.blog_like_count);
            blog_comnt_count = itemView.findViewById(R.id.blog_comnt_count);

            blogDate = itemView.findViewById(R.id.blog_date);
            blogShare = itemView.findViewById(R.id.blog_share_btn);

            mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLikes.keepSynced(true);
        }
        //count comments post
        public void CountComments(String post_key){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BlogComnt").child(post_key);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    blog_comnt_count.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //count like post
        public void CountLike(final String post_key){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(post_key);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        like.setText(dataSnapshot.getChildrenCount() + " j'aime(s)");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //like post
        public void setLikesBtn(final String post_key){

            mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mLikes.setImageResource(R.drawable.action_like_red);

                    }else {

                        mLikes.setImageResource(R.drawable.ic_baseline_favorite_border);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public String setStatus(String status){

            TextView post_status = mView.findViewById(R.id.blog_status);
            post_status.setText(status);
            //pour le partage
            String share = post_status.getText().toString();
            return share;
        }

        public String setDesc(String desc){

            TextView post_desc = mView.findViewById(R.id.blog_desc);
            post_desc.setText(desc);
            //pour le partage
            String share = post_desc.getText().toString();
            return share;
        }

        public String setImageUrl(final Context ctx, final String imageUrl){

            final ImageView post_image = mView.findViewById(R.id.blog_image);

                                                  /*---CAPACITY OFFLINE---*/
            Picasso.with(ctx).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(imageUrl).into(post_image);

                }
            });

            return imageUrl;
        }

        public void setUserName(String userName){

            TextView postUserName = mView.findViewById(R.id.blog_user_name);
            postUserName.setText(userName);
        }
        public void setUserImage(Context context, String ImgUsers){

            blogUserImage = itemView.findViewById(R.id.blog_user_image);
            Picasso.with(context).load(ImgUsers).into(blogUserImage);
        }
    }
}
