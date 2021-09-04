package com.youthfimodd.elenges.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.models.servicemaps;

import java.util.ArrayList;

public class Custom_serviceMaps extends RecyclerView.Adapter<Custom_serviceMaps.ViewHolder> {

    private Context mContext;
    private ArrayList<servicemaps> mService;
    //String numero;

    public Custom_serviceMaps(Context mContext, ArrayList<servicemaps> mUsers) {
        this.mContext = mContext;
        this.mService = mUsers;
    }

    /*@NonNull JE DOIS EFFACER CETTE CLASSE
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //
        View r = convertView;
        viewHolder viewHolder = null;

        if (r==null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            r = layoutInflater.inflate(R.layout.custom_service, null, true);
            viewHolder = new viewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (viewHolder) r.getTag();
        }

        viewHolder.imgService.setImageResource(imgid[position]);
        viewHolder.service.setText(service[position]);

        return r;

    }*/

    @NonNull
    @Override
    public Custom_serviceMaps.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_maps, parent, false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Custom_serviceMaps.ViewHolder holder, final int position) {

        servicemaps Smaps = mService.get(position);

        holder.service.setText(Smaps.getTitre());
        Glide.with(mContext).load(Smaps.getIcon()).into(holder.imgService);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0){
                    Toast.makeText(mContext, " 96 Boulevard Du 30 Juin, Kinshasa ...", Toast.LENGTH_SHORT).show();
                }
                if (position == 1){
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:+243810203505"));
                    mContext.startActivity(call);
                    Toast.makeText(mContext, "Calling...", Toast.LENGTH_SHORT).show();
                }
                if (position == 2){
                    /*Intent intent = new Intent(mContext, StatutActivity.class);
                    mContext.startActivity(intent);*/
                    showMessage("Message SOS", "Voici le message à envoyer...");
                    Toast.makeText(mContext, "Sending SOS...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mService.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //
        TextView service;
        ImageView imgService;

        public ViewHolder(View view){
            super(view);
            //
            service = view.findViewById(R.id.service);
            imgService = view.findViewById(R.id.ImgService);
        }
    }
    private void showMessage(String titre, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }
}
