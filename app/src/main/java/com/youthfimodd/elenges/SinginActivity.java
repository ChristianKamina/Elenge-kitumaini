package com.youthfimodd.elenges;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SinginActivity extends AppCompatActivity {
    //
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //
    EditText txtnom, txtphone, txtville, txtemail, txtpwd;
    TextView typeUser, txt_accept_terms;
    RadioGroup radioGroup;
    RadioButton rbtn_sex_man, rbtn_sex_woman;
    Button btninscrip;
    Spinner customspinner;
    ImageView imgCancel;
    CheckBox cb_terms;
    WebView web_view;
    //
    List<String> trancheAge;
    //
    String mode, Sex = "";
    //
    private ProgressDialog mInscripProgress;
    AlertDialog alertDialog;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        /*toolbar
        getSupportActionBar().setTitle("Inscrivez-vous");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTransp)));
        //
        mode = getIntent().getStringExtra("PairEd");
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //Progress dialog
        mInscripProgress = new ProgressDialog(this);
        //String
        txtnom = findViewById(R.id.idnom);
        radioGroup = findViewById(R.id.radio_group);
        rbtn_sex_man = findViewById(R.id.rb_man);
        rbtn_sex_woman = findViewById(R.id.rb_woman);
        RadioButton genreBtn = findViewById(radioGroup.getCheckedRadioButtonId());
        Sex = genreBtn.getText().toString();
        txtphone = findViewById(R.id.idTelphone);
        txtville = findViewById(R.id.idVille);
        txtemail = findViewById(R.id.idEmail);
        txtpwd = findViewById(R.id.idPwd);
        btninscrip = findViewById(R.id.btnInscription);
        //custom Spinner
        customspinner = findViewById(R.id.spinerAge);
        txt_accept_terms = findViewById(R.id.txt_accept_terms);
        cb_terms = findViewById(R.id.cb_terms);
        typeUser = findViewById(R.id.spinnerTyUser);
        typeUser.setText(mode);
        typeUser.setText(mode);
        //Spronner pour l'age
        prepareList();
        ArrayAdapter<String> customSpinAdp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trancheAge);
        //Set the drop down resource with our custom dropdown layouts
        customSpinAdp.setDropDownViewResource(R.layout.spinner_age);
        customspinner.setAdapter(customSpinAdp);
        //
        alertDialog = new AlertDialog.Builder(this).create();
        //
        /*--*/
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //StringBuffer Sexe = new StringBuffer();
            switch (checkedId){
                case R.id.rb_man:
                    Sex = "Homme";
                break;
                case R.id.rb_woman:
                    Sex = "Femme";
                break;
            }
        });
        /*--*/
        txt_accept_terms.setOnClickListener(v -> accepTerms());
        //
        btninscrip.setOnClickListener(v -> {
            //Code for firebase
            String Nom = txtnom.getText().toString().trim();
            String Phone = txtphone.getText().toString().trim();
            String Email = txtemail.getText().toString().trim();
            String Passw = txtpwd.getText().toString().trim();
            String Ville = txtville.getText().toString().trim();
            String TranAge = customspinner.getSelectedItem().toString().trim();
            String TypeUsers = typeUser.getText().toString().trim();

            if (TextUtils.isEmpty(Nom) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Passw) || TextUtils.isEmpty(Ville) || TextUtils.isEmpty(TranAge) || TextUtils.isEmpty(TypeUsers) ){
                //
                txtnom.setError("Ce champ ne peut pas etre vide!");
                txtphone.setError("Ce champ ne peut pas etre vide!");
                txtemail.setError("Ce champ ne peut pas etre vide!");
                txtpwd.setError("Ce champ ne peut pas etre vide!");
                txtville.setError("Ce champ ne peut pas etre vide!");
                typeUser.setError("Ce champ ne peut pas etre vide!");
                //
                Toast.makeText(SinginActivity.this, " Champs vided!!!!", Toast.LENGTH_LONG).show();

            }
            else if (!TextUtils.isEmpty(Nom) || !TextUtils.isEmpty(Phone) || !TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Passw) || !TextUtils.isEmpty(Ville) || !TextUtils.isEmpty(TranAge) || TextUtils.isEmpty(TypeUsers)){
                //
                if (TranAge.equals("Tranche d'age") || !cb_terms.isChecked()){

                    Toast.makeText(SinginActivity.this, getString(R.string.accept_terms_please),
                            Toast.LENGTH_SHORT).show();
                    cb_terms.setError(getString(R.string.mustAcceptTerms));

                    Toast.makeText(SinginActivity.this, " Tranche d'age est vide!!!!", Toast.LENGTH_LONG).show();

                }else {
                    mInscripProgress.setTitle("Registering User");
                    mInscripProgress.setMessage("Please wait, while on create your account!");
                    mInscripProgress.setCanceledOnTouchOutside(false);
                    mInscripProgress.show();

                    //call this method
                    register_user(Nom, Sex, Phone, Email, Passw, Ville, TranAge, TypeUsers);
                }
            }

        });
    }
    //fire base
    private void register_user(final String nom, String sexe, final String phone, final String email, final String passw, final String ville, final String tranAge, final String Usertype){
        //
        mAuth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                //
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                //
                assert current_user != null;
                String uid = current_user.getUid();
                // Write a message to the database
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user.getUid());

                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("id", uid);
                userMap.put("name", nom);
                userMap.put("sexe", sexe);
                userMap.put("email", email);
                userMap.put("phone", phone);
                userMap.put("ville", ville);
                userMap.put("age", tranAge);
                userMap.put("typeusesr", Usertype);
                userMap.put("pwd",passw);
                userMap.put("status", "offline");
                userMap.put("timeStatus", "En linge");
                userMap.put("typingTo", "noOne");
                userMap.put("image", "default");
                userMap.put("thumb_image", "default");
                //
                mDatabase.setValue(userMap).addOnCompleteListener(task1 -> {
                    //
                    if (task1.isSuccessful()){
                        //
                        mInscripProgress.dismiss();

                        Intent intent = new Intent(SinginActivity.this, Main2Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }else {
                mInscripProgress.dismiss();

                Toast.makeText(SinginActivity.this, " Email existe déjà!" +
                        "....................", Toast.LENGTH_LONG).show();
            }
        });
    }
    //
    private void accepTerms() {

        alertDialog.setTitle("");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.accept_terms,null);
        imgCancel = add_menu_layout.findViewById(R.id.imgCancel);
        web_view = add_menu_layout.findViewById(R.id.web_polConf);
        web_view.loadUrl("https://www.privacypolicies.com/live/00078c9f-ca16-4c3e-90fa-3055d2863a8c");
        //***************************************************
        alertDialog.setView(add_menu_layout);
        alertDialog.show();
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    //
    private void prepareList(){
        trancheAge = new ArrayList<>();
        trancheAge.add("Tranche d'age");
        trancheAge.add("12-17");
        trancheAge.add("18-23");
        trancheAge.add("24-29");
        trancheAge.add("30-35");
        trancheAge.add("36-41");
        trancheAge.add("42-plus");
    }
    //menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
