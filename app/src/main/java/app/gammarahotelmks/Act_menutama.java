package app.gammarahotelmks;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import app.gammarahotelmks.bantuan.Act_set_get;

public class Act_menutama extends Activity implements View.OnClickListener{
    Button bt_atten, bt_earl, bt_sett, bt_sifn, bt_help, bt_abot;
    Intent a;
    TextView t_namaut;

    private AdView JadVi;
    private InterstitialAd intersialADDDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_menutama);

        bt_atten = (findViewById(R.id.tx_atendz));
        bt_atten.setOnClickListener(this);
        bt_earl = (findViewById(R.id.tx_earlyz));
        bt_earl.setOnClickListener(this);
        bt_sett = (findViewById(R.id.tx_setz));
        bt_sett.setOnClickListener(this);
        bt_sifn = (findViewById(R.id.tx_signofz));
        bt_sifn.setOnClickListener(this);
        bt_help = (findViewById(R.id.tx_helpz));
        bt_help.setOnClickListener(this);
        bt_abot = (findViewById(R.id.tx_aboutz));
        bt_abot.setOnClickListener(this);
        t_namaut = findViewById(R.id.tx_namautama);
        Act_set_get a = new Act_set_get();
        t_namaut.setText(a.getnama());
        //all iklan
        JadVi = findViewById(R.id.adview_);
        JadVi.loadAd(new AdRequest.Builder().build());

//menyiapkan iklan untuk intersial ID
        intersialADDDD = new InterstitialAd(Act_menutama.this);
//masukkan id iklan
        intersialADDDD.setAdUnitId(getString(R.string.id_iklanyaaa));
        AdRequest adreq = new AdRequest.Builder().build();

        //muat iklan interstiasal
        intersialADDDD.loadAd(adreq);

        //persiapan iklan interstial
        intersialADDDD.setAdListener(new AdListener(){
            public void onAdLoaded(){
                displayI();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == bt_atten){
            a = new Intent(this, Act_lihat_finger.class);
            finish(); startActivity(a);
        }
        else if (v == bt_earl){
            a = new Intent(this, Act_early.class);
            finish(); startActivity(a);
        }
        else if (v == bt_sett){
            a = new Intent(this, Act_setting.class);
            finish(); startActivity(a);
        }
        else if (v == bt_sifn){
            onBackPressed();
        }
        else if (v == bt_help){
            Uri uri = Uri.parse("https://t.me/InfoTechManager"); // missing 'http://' will cause crashed
            a = new Intent(Intent.ACTION_VIEW, uri); startActivity(a);
        }
        else if (v == bt_abot){
            a =  new Intent(this, Act_about.class);
            finish(); startActivity(a);
        }



    }
    public void onBackPressed(){
        DialogInterface.OnClickListener dd_dialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dd_kembali();

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("No", dd_dialog).setNegativeButton("Yes", dd_dialog).show();
    }
    public void dd_kembali(){
        Intent n = new Intent(Act_menutama.this, Act_login.class);
        finish();
        startActivity(n);
    }
    public void displayI(){
        if (intersialADDDD.isLoaded()){
            intersialADDDD.show();
        }
        else{
            intersialADDDD.show();
        }
    }
}