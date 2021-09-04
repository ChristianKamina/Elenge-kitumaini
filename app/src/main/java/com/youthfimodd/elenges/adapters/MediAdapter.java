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
import com.youthfimodd.elenges.Detail_VideoActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.models.Media;

import java.util.List;

public class MediAdapter extends RecyclerView.Adapter<MediAdapter.MyViewHolder>{

    private Context context;
    private List<Media> mediaList;

    public MediAdapter(Context context, List<Media> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public MediAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_medias, parent, false);
        return new MediAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediAdapter.MyViewHolder holder, int position) {

        Media media = mediaList.get(position);

        Picasso.with(context).load(media.getImage_media()).into(holder.imageMedia);
        holder.titreMedia.setText(media.getTitle_media());
        holder.urlMedia.setText(media.getUrl_media());

        holder.imageMedia.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detail_VideoActivity.class);
            intent.putExtra("media_id", media.getId_media());
            intent.putExtra("media_title", media.getTitle_media());
            intent.putExtra("media_image", media.getImage_media());
            intent.putExtra("media_url", media.getUrl_media());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageMedia;
        private TextView titreMedia;
        private TextView urlMedia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMedia = itemView.findViewById(R.id.img_media);
            titreMedia = itemView.findViewById(R.id.name_media);
            urlMedia = itemView.findViewById(R.id.url_media);
        }
    }
}
