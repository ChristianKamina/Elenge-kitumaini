package com.youthfimodd.elenges.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.UsersActivity;
import com.youthfimodd.elenges.custom.chat_model.ChatList;
import com.youthfimodd.elenges.custom.chat_model.Token;
import com.youthfimodd.elenges.custom.chat_model.Users;
import com.youthfimodd.elenges.custom.chat_model.UsersAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import aglibs.loading.skeleton.layout.SkeletonRelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    //
    private FloatingActionButton contact1;
    private RecyclerView recyclerViewMSG;
    private UsersAdapter usersAdapter;
    private List<Users> mUsers;
    private List<ChatList> usersList;
    //firebase
    FirebaseUser fuser;
    DatabaseReference reference;
    ImageView img_no_message;
    /*Detail contacts*/
    SkeletonRelativeLayout skeletonLoading;
    LinearLayout lienLoading;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity) Objects.requireNonNull(getActivity())).SetActionBarTitle("Plus près de moi");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        //
        skeletonLoading = view.findViewById(R.id.skeletonLoading);
        lienLoading = view.findViewById(R.id.lienLoading);
        //
        contact1 = view.findViewById(R.id.contact1);
        img_no_message = view.findViewById(R.id.no_message);

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(getContext(), UsersActivity.class);
                startActivity(intent);
            }
        });
        /*--*/
        recyclerViewMSG = view.findViewById(R.id.recyclerViewMSG);
        recyclerViewMSG.setHasFixedSize(true);
        recyclerViewMSG.setLayoutManager(new LinearLayoutManager(getContext()));
        //
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatlist = snapshot.getValue(ChatList.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        //
        return view;
    }
    //pour la notification
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
    //
    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    for (ChatList chatlist : usersList){
                        assert users != null;
                        if (users.getId().equals(chatlist.getId())){
                            lienLoading.setVisibility(View.GONE);
                            recyclerViewMSG.setVisibility(View.VISIBLE);
                            mUsers.add(users);
                        }
                    }
                }
                usersAdapter = new UsersAdapter(getContext(), mUsers, true);
                recyclerViewMSG.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        skeletonLoading.startLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        skeletonLoading.stopLoading();
    }
}
