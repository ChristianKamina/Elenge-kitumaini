package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SecurityCodeActivity extends AppCompatActivity {
  //
  Button mButton;
  TextView txtPinName;
  EditText editText;

  String ValuePairEduc;
  //
  private ProgressDialog mInscripProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_security_code);
    //
    mInscripProgress = new ProgressDialog(this);
    //
    txtPinName = findViewById(R.id.txt_PinName);
    ValuePairEduc = getIntent().getExtras().getString("PairEd");
    txtPinName.setText(ValuePairEduc);

    editText = findViewById(R.id.RecPwde);
    //
    mButton = findViewById(R.id.btnRecP);
    //txtpin = findViewById(R.id.RecPwde);
    //
    mButton.setOnClickListener(v -> {
      //
      String Code = editText.getText().toString().trim();

      if (TextUtils.isEmpty(Code)){

        editText.setError("Champ vide!!!");

      }
      if (ValuePairEduc.equals("Parrain")){

        if (!TextUtils.isEmpty(Code) && Code.equals("150-762-934")){

          mInscripProgress.setTitle("Vérification du Code Parrain");
          mInscripProgress.setMessage("Please wait, while checking this code!");
          mInscripProgress.setCanceledOnTouchOutside(false);
          mInscripProgress.show();
          //
          Intent intent = new Intent(SecurityCodeActivity.this, SinginActivity.class);
          intent.putExtra("PairEd", ValuePairEduc);
          startActivity(intent);
          finish();

          Toast.makeText(SecurityCodeActivity.this, " Good!!!!", Toast.LENGTH_LONG).show();

        }else {Toast.makeText(SecurityCodeActivity.this, " Code erroné!!!!", Toast.LENGTH_LONG).show();}

      } else if (ValuePairEduc.equals("Pair Educateur")){

        if (!TextUtils.isEmpty(Code) && Code.equals("130-652-945")){

          mInscripProgress.setTitle("Vérification du Code Pair Educateur");
          mInscripProgress.setMessage("Please wait, while checking this code!");
          mInscripProgress.setCanceledOnTouchOutside(false);
          mInscripProgress.show();
          //
          Intent intent = new Intent(SecurityCodeActivity.this, SinginActivity.class);
          intent.putExtra("PairEd", ValuePairEduc);
          startActivity(intent);
          finish();

          Toast.makeText(SecurityCodeActivity.this, " Good!!!!", Toast.LENGTH_LONG).show();

        }else {Toast.makeText(SecurityCodeActivity.this, " Code erroné!!!!", Toast.LENGTH_LONG).show();}
      }
    });
  }
}
