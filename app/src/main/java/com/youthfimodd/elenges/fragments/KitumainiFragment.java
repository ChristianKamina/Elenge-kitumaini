package com.youthfimodd.elenges.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class KitumainiFragment extends Fragment {

    private TextView txt_name, txt_date, txt_address, txt_phone;
    private ImageView img_add_profile;
    private Button btn_save;
    Bitmap bmp, scalebtmp;
    int pageWith = 1200;
    Date dateObjt;
    DateFormat dateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity) requireActivity()).SetActionBarTitle("Kitumaini");

        View view = inflater.inflate(R.layout.fragment_kitumaini, container, false);

        img_add_profile = view.findViewById(R.id.kitu_add_profile);
        txt_name = view.findViewById(R.id.id_kitu_nom);
        txt_date = view.findViewById(R.id.id_kitu_date);
        txt_address = view.findViewById(R.id.id_kitu_address);
        txt_phone = view.findViewById(R.id.id_kitu_Phone);
        btn_save = view.findViewById(R.id.kitu_btn_save);

        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_profil2);
        scalebtmp = Bitmap.createScaledBitmap(bmp, 500, 510, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPdf();
        
        return view;
    }

    private void createPdf() {
        btn_save.setOnClickListener(v -> {

            dateObjt = new Date();

            PdfDocument myPdfDocument = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();

            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 2).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo);
            Canvas canvas = myPage1.getCanvas();

            canvas.drawBitmap(scalebtmp, 0,0, myPaint);

            /*titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(70);
            canvas.drawText("Dimond Pizza", pageWith/2, 270, titlePaint);*/

            myPaint.setColor(Color.rgb(0, 113, 188));
            myPaint.setTextSize(30f);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Powered by Elenge", 1160, 40, myPaint);
            canvas.drawText("youthfim", 1160, 80, myPaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(25);
            canvas.drawText("Code Pin No.: " +"$*"+txt_phone.getText()+"@!-", pageWith-20, 390, myPaint);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(55);
            canvas.drawText("LES INFORMATIONS DE BASE SUR L'ELEVE", pageWith/2, 600, titlePaint);

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(35f);
            myPaint.setColor(Color.BLACK);
            canvas.drawText("Nom: "+txt_name.getText(), 20, 660, myPaint);
            canvas.drawText("Adresse: "+txt_address.getText(), 20, 700, myPaint);
            canvas.drawText("Date Naissance: "+txt_date.getText(), 20, 740, myPaint);
            canvas.drawText("Téléphone: "+txt_phone.getText(), 20, 780, myPaint);
            /*canvas.drawText("Age: "+txt_age.getText(), 20, 820, myPaint);
            canvas.drawText("Téléphone: "+txt_phone.getText(), 20, 863, myPaint);*/


            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            canvas.drawText("Date: "+dateFormat.format(dateObjt), pageWith-300, 660, myPaint);

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            canvas.drawText("Time: "+dateFormat.format(dateObjt), pageWith-260, 700, myPaint);

            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeMiter(2);
            canvas.drawRect(20, 990, pageWith-20, 900, myPaint); /*contour*/

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setStyle(Paint.Style.FILL);
            canvas.drawText("No.", 40, 950, myPaint);
            canvas.drawText("Motif", 200, 950, myPaint);
            canvas.drawText("Date de menarche:",400, 950, myPaint);
            canvas.drawText("Date D.R..", 800, 950, myPaint);
            canvas.drawText("Durée ", 1050, 950, myPaint);

            canvas.drawLine(180, 900, 180, 990, myPaint);
            canvas.drawLine(180, 900, 180, 990, myPaint);
            canvas.drawLine(180, 900, 180, 990, myPaint);
            canvas.drawLine(180, 900, 180, 990, myPaint);

            myPdfDocument.finishPage(myPage1);

            File file = new File(Environment.getExternalStorageDirectory(), "/Hello.pdf");

            try {
                myPdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            myPdfDocument.close();
        });
    }
}