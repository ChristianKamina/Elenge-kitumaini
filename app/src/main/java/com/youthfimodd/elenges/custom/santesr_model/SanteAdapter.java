package com.youthfimodd.elenges.custom.santesr_model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youthfimodd.elenges.DetailSsrActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanteAdapter extends RecyclerView.Adapter<SanteAdapter.ViewHolder> {
    //
    private Context context;
    private List<SanteSr> santeSrs;
    //DateBase SQLite
    private DatabaseHelper myDb;

    public SanteAdapter(Context context, List<SanteSr> santeSrs) {
        this.context = context;
        this.santeSrs = santeSrs;
    }

    @NonNull
    @Override
    public SanteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_ssr, parent, false);
        return new SanteAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SanteAdapter.ViewHolder holder, final int position) {
        //SQLite
        myDb = new DatabaseHelper(context);
        //
        Picasso.with(context).load(santeSrs.get(position).getImg()).into(holder.imageSante);
        holder.textTitre.setText(santeSrs.get(position).getTitre());
        holder.textApercu.setText(santeSrs.get(position).getApercu());

        //Click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(context, DetailSsrActivity.class);
                intent.putExtra("apercu", santeSrs.get(position).getApercu());
                intent.putExtra("titre", santeSrs.get(position).getTitre());
                intent.putExtra("url", santeSrs.get(position).getUrl());

                context.startActivity(intent);
                //
                Toast.makeText(context, "Detail!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return santeSrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //
        private ImageView imageSante;
        private TextView textTitre;
        private TextView textApercu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSante = itemView.findViewById(R.id.logo);
            textTitre = itemView.findViewById(R.id.find_name);
            textApercu = itemView.findViewById(R.id.find_desc);
        }
    }

}
