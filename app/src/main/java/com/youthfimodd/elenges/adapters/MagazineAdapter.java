package com.youthfimodd.elenges.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.DetailSsrActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.models.Magazin;

import java.util.List;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.MyViewHolder>{

    private Context context;
    private List<Magazin> magazinList;

    public MagazineAdapter(Context context, List<Magazin> magazinList) {
        this.context = context;
        this.magazinList = magazinList;
    }

    @NonNull
    @Override
    public MagazineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_magazin, parent, false);
        return new MagazineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MagazineAdapter.MyViewHolder holder, int position) {

        Magazin magazin = magazinList.get(position);

        Picasso.with(context).load(magazin.getImage_mag()).into(holder.imageMag);
        holder.titreMag.setText(magazin.getName_mag());
        holder.urlMag.setText(magazin.getUrl_mag());

        holder.imageMag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailSsrActivity.class);
                intent.putExtra("apercu", magazin.getUrl_mag());
                intent.putExtra("titre", magazin.getName_mag());
                intent.putExtra("url", magazin.getUrl_mag());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return magazinList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageMag;
        private TextView titreMag;
        private TextView urlMag;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageMag = itemView.findViewById(R.id.img_mag);
            titreMag = itemView.findViewById(R.id.name_mag);
            urlMag = itemView.findViewById(R.id.url_mag);
        }
    }
}
