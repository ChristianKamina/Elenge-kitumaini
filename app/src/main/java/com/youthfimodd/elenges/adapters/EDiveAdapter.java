package com.youthfimodd.elenges.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.MagazineActivity;
import com.youthfimodd.elenges.QRActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.models.EDive;

import java.util.ArrayList;

public class EDiveAdapter extends RecyclerView.Adapter<EDiveAdapter.MyViewHolder> {

    ArrayList<EDive> mData;
    Context mContext;

    public EDiveAdapter(ArrayList<EDive> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.layout_edive,parent,false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.txtEdive.setText(mData.get(i).getTitle());
        Picasso.with(mContext).load(mData.get(i).getImageBg()).into(holder.imgBg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mData.get(i).getTitle().equals("Questions/Réponses")){
                    mContext.startActivity(new Intent(mContext, QRActivity.class));

                }else if (mData.get(i).getTitle().equals("Multimedias")){
                    //mContext.startActivity(new Intent(mContext, MultimediaActivity.class));
                    Toast.makeText(mContext, "Coming Soon!", Toast.LENGTH_SHORT).show();

                }else if (mData.get(i).getTitle().equals("Magazines Ados & Jeunes")){
                    mContext.startActivity(new Intent(mContext, MagazineActivity.class));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtEdive;
        ImageView imgBg;
        RelativeLayout expandable_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEdive= itemView.findViewById(R.id.txtEdive);
            imgBg = itemView.findViewById(R.id.imgBg);

        }
    }
}


