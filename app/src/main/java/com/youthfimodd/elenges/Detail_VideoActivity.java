package com.youthfimodd.elenges;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Detail_VideoActivity extends AppCompatActivity {

    private TextView txt_title_series, txt_type_series, txt_description_series;
    private ImageView img_series;
    private Spinner spinner_series_saisons;
    private RecyclerView recyclerSeries;
    String id_media, title_media, image_media, url_media;

    List<String> saisonsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);

        id_media = getIntent().getStringExtra("media_id");
        title_media = getIntent().getStringExtra("media_title");
        image_media = getIntent().getStringExtra("media_image");
        url_media = getIntent().getStringExtra("media_url");

        saisonsList = new ArrayList<>();
        saisonsList.add("Saison 1");
        saisonsList.add("Saison 2");
        saisonsList.add("Saison 3");

        recyclerSeries = findViewById(R.id.recycleSeries);
        spinner_series_saisons = findViewById(R.id.spinner_series_saisons);
        txt_title_series = findViewById(R.id.txt_series_title);
        txt_title_series.setText(title_media);
        txt_type_series = findViewById(R.id.txt_series_genre);
        txt_type_series.setText(id_media);
        txt_description_series = findViewById(R.id.txt_series_descrip);
        txt_description_series.setText(url_media);
        img_series = findViewById(R.id.img_series);
        Picasso.with(getApplicationContext()).load(image_media).into(img_series);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                saisonsList);
        spinner_series_saisons.setAdapter(adapter);

        findViewById(R.id.Video_back).setOnClickListener(v -> finish());
        findViewById(R.id.img_series_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Detail_VideoActivity.this, "Comment!", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.img_series_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Detail_VideoActivity.this, "Share!", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.img_series_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Detail_VideoActivity.this, "Like!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}