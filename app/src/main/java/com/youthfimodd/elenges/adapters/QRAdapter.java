package com.youthfimodd.elenges.adapters;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.models.QR;
import com.youthfimodd.elenges.services.TextSpeach;

import java.util.ArrayList;

public class QRAdapter extends RecyclerView.Adapter<QRAdapter.MyViewHolder> {

    ArrayList<QR> mData;
    Context mContext;
    TextSpeach speach;
    private boolean change = false;

    public QRAdapter(ArrayList<QR> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        this.speach = new TextSpeach(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.item_layout_paragraphe,parent,false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        QR qr = mData.get(i);
        holder.txtReponse.setText(qr.getReponse());
        holder.txtQuestion.setText(qr.getQuestion());
        boolean isExpanable = mData.get(i).isExpandable();
        holder.expandable_layout.setVisibility(isExpanable ? View.VISIBLE : View.GONE);

        if(holder.expandable_layout.getVisibility()==View.GONE){
            TransitionManager.beginDelayedTransition(holder.cardView,new AutoTransition());
            holder.expandable_button.setBackgroundResource(R.drawable.ic_expand_more);
        }
        else {
            TransitionManager.beginDelayedTransition(holder.cardView,new AutoTransition());
            holder.expandable_button.setBackgroundResource(R.drawable.ic_expand_less);

        }
        holder.txtEnSavoirPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!change){
                    speach.SpeachTexte(mData.get(i).getQuestion() + mData.get(i).getReponse());
                    holder.imgPlay.setImageResource(R.drawable.ic_baseline_volume_off);
                    change = true;
                }else {
                    speach.stopping();
                    holder.imgPlay.setImageResource(R.drawable.ic_volume_down);
                    change = false;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtReponse,txtEnSavoirPlus;
        LinearLayout linear_layout;
        RelativeLayout expandable_layout;
        ImageButton expandable_button;
        ImageView imgPlay;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion= itemView.findViewById(R.id.txtQuestion);
            txtReponse = itemView.findViewById(R.id.txtReponse);
            txtEnSavoirPlus = itemView.findViewById(R.id.txtEnSavoirPlus);
            imgPlay = itemView.findViewById(R.id.imgPlay);
            linear_layout= itemView.findViewById(R.id.linear_layout);
            expandable_layout= itemView.findViewById(R.id.expandable_layout);
            expandable_button= itemView.findViewById(R.id.expandable_button);
            cardView = itemView.findViewById(R.id.cardView);

            expandable_button.setOnClickListener(v -> {

                QR qr = mData.get(getAdapterPosition());
                qr.setExpandable(!qr.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
