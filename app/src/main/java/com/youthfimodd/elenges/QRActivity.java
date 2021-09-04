package com.youthfimodd.elenges;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.youthfimodd.elenges.adapters.QRAdapter;
import com.youthfimodd.elenges.firebase.DatabaseFireStore;
import com.youthfimodd.elenges.models.QR;

import java.util.ArrayList;

public class QRActivity extends AppCompatActivity {

    RecyclerView rv_qr;
    QRAdapter adapter;
    ArrayList<QR> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        //Toolbar
        getSupportActionBar().setTitle("Questions/Reponses");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_qr = findViewById(R.id.rv_qr);
        rv_qr.setHasFixedSize(false);
        rv_qr.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getData();
    }

    private void getData() {
        DatabaseFireStore.get().collection("qr").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentSnapshot document : task.getResult())
                        {
                            QR qr = new QR();
                            qr.setQuestion(document.getString("question"));
                            qr.setReponse(document.getString("reponse"));

                            mList.add(qr);
                            adapter = new QRAdapter(mList,getApplicationContext());
                            rv_qr.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }

                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            break;
        }

        return super.onOptionsItemSelected(item);
    }
}