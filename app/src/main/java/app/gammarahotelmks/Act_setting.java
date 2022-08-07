package app.gammarahotelmks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.gammarahotelmks.bantuan.Act_set_get;
import app.gammarahotelmks.bantuan.ConnectionDetector;
import app.gammarahotelmks.bantuan.JSONP2;

public class Act_setting extends Activity implements View.OnClickListener {
    Button b_kmb, b_prbr;
    EditText e_ps_skg, e_ps_baru, e_u_ps;
    TextView tx_nama, tx_id, tx_dep;
    Act_set_get stg2 = new Act_set_get();
    JSONP2 jParser = new JSONP2();
    ProgressDialog damz_dialog;
    Boolean isInternetPresent = false;
    private AdView JadVi;
    private InterstitialAd intersialADDDD;
    String var_ps_skg, var_ps_baru, var_ul_psbaru, var_id;
    ConnectionDetector cd;
    private TextView txtStatus;
    private String url_sel_user = "https://ridercadel.com/damz/damz_login.php";
    private String url_upd_user = "https://ridercadel.com/damz/damz_upd_pass.php";
    AlertDialog dyam_dialog;
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_setting);

        b_prbr =  findViewById(R.id.bt_proses2);

        e_ps_skg =  findViewById(R.id.edit_pssekarang);
        e_ps_baru =  findViewById(R.id.edit_psbaru);
        e_u_ps =  findViewById(R.id.edit_psulang);

        txtStatus =  findViewById(R.id.txt_alert2);

        b_prbr.setOnClickListener(this);
        tx_id = (findViewById(R.id.tx_finger2));
        tx_id.setText(stg2.getusnme());
        tx_nama = (findViewById(R.id.tx_nama2));
        tx_nama.setText(stg2.getnama());
        tx_dep = (findViewById(R.id.tx_dept2));
        tx_dep.setText(stg2.getdept());
        //all iklan
        JadVi = findViewById(R.id.adview_);
        JadVi.loadAd(new AdRequest.Builder().build());

//menyiapkan iklan untuk intersial ID
        intersialADDDD = new InterstitialAd(Act_setting.this);
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
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        var_ps_baru = e_ps_baru.getText().toString();
        var_ul_psbaru = e_u_ps.getText().toString();
        var_ps_skg = e_ps_skg.getText().toString();
        var_id = tx_id.getText().toString();
        try {
            if (v == b_prbr) {
                if ((var_ps_baru.length() > 0) && (var_ps_skg.length() > 0)&& (var_ul_psbaru.length() > 0)) {
                    if (!(e_u_ps.getText().toString().equals(e_ps_baru.getText().toString()))) {
                        tidak_sama();
                    }
                    else{
                        if (isInternetPresent) {
                            readWebpage(v);
                        }
                        else{
                            inet_mati();
                        }
                    }
                } else {
                    try {
                        field_kosong();
                    } catch (Exception e) {
                        Toast.makeText(Act_setting.this, "Ini errornya: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            } else if (v == b_kmb) {
                onBackPressed();
            }
        }
        catch(Exception e){
            Toast.makeText(Act_setting.this, "Errornya: " + e, Toast.LENGTH_LONG).show();
        }
    }public void onBackPressed(){
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
                        kosong();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You sure?").setPositiveButton("Tidak", dd_dialog).setNegativeButton("Ya", dd_dialog).show();
    }
    public void dd_kembali(){
        Intent i = new Intent(Act_setting.this,Act_menutama.class);
        finish();
        startActivity(i);
    }
    private void kosong() {
        e_ps_baru.setText("");
        e_ps_skg.setText("");
        e_u_ps.setText("");
        e_ps_skg.requestFocus();
    }
    private void tidak_sama(){
        dyam_dialog = new AlertDialog.Builder(Act_setting.this).create();
        dyam_dialog.setTitle("Warning");
        dyam_dialog.setIcon(R.drawable.warning);
        dyam_dialog.setMessage("Field password baru dan field Ulangi password harus sesuai");
        dyam_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dyam_dialog.show();
    }
    private void field_kosong(){
        AlertDialog dyam_dialog = new AlertDialog.Builder(Act_setting.this).create();
        dyam_dialog.setTitle("Warning");
        dyam_dialog.setIcon(R.drawable.warning);
        dyam_dialog.setMessage("Please Complete the field");
        dyam_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dyam_dialog.show();
    }

    private class CallWebPageTask extends AsyncTask< String, Void, String > {
        private ProgressDialog dialog;
        protected Context applicationContext;@
                Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(applicationContext, "Updating", "Please Wait...", true);
        }

        @
                Override
        protected String doInBackground(String...urls) {
            String response = "";
            response = getRequest(urls[0]);
            return response;

        }

        @
                Override
        protected void onPostExecute(String result) {
            this.dialog.cancel();
            txtStatus.setText(result);
            String u = txtStatus.getText().toString();
            if (u.substring(27, 29).trim().equals("ok")) {
                new input().execute();
            } else {
                AlertDialog dyam_dialog = new AlertDialog.Builder(Act_setting.this).create();
                dyam_dialog.setTitle("Warning");
                dyam_dialog.setIcon(R.drawable.warning);
                dyam_dialog.setMessage("Password is Wrong...");
                dyam_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dyam_dialog.show();
                e_ps_skg.requestFocus();
            }
        }
    }
    public void readWebpage(View view) {
        CallWebPageTask task = new CallWebPageTask();
        task.applicationContext = Act_setting.this;
        String url = url_sel_user + "?usname=" + var_id + "&psword=" + var_ps_skg;
        task.execute(new String[] {
                url
        });
    }
    public String getRequest(String Url) {
        String sret;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Url);
        try {
            HttpResponse response = client.execute(request);
            sret = request(response);
        } catch (Exception ex) {
            sret = "Failed Connect to server!";
        }
        return sret;
    }

    public static String request(HttpResponse response) {
        String result = "";
        try {
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader( in ));
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            } in .close();
            result = str.toString();
        } catch (Exception ex) {
            result = "Error";
        }
        return result;
    }
    public class input extends AsyncTask < String, String, String > {
        String success;@
                Override
        protected void onPreExecute() {
            super.onPreExecute();
            damz_dialog = new ProgressDialog(Act_setting.this);
            damz_dialog.setMessage("Updating");
            damz_dialog.setIndeterminate(false);
            damz_dialog.show();
        }

        @
                Override
        protected String doInBackground(String...arg0) {
            try {
                List<NameValuePair> params = new ArrayList< NameValuePair >();
                params.add((NameValuePair) new BasicNameValuePair("ID", var_id));
                params.add((NameValuePair) new BasicNameValuePair("PASSWORD", var_ps_baru));
                url_upd_user = url_upd_user+ "?ID=" + var_id;
                JSONObject json = jParser.makeHttpRequest(url_upd_user, "POST", params);
                success = json.getString("success");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Errornya: " + e, Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            damz_dialog.dismiss();
            if (success.equals("1")) {
                Toast.makeText(getApplicationContext(), "Update password is Successfull", Toast.LENGTH_LONG).show();
                kosong();
                dd_kembali();
            } else {
                //   Toast.makeText(getApplicationContext(), "Password Gagal diupdate", Toast.LENGTH_LONG).show();

            }
        }
    }
    private void inet_mati() {
        AlertDialog dd_dialog = new AlertDialog.Builder(Act_setting.this).create();
        dd_dialog.setTitle("Warning");
        dd_dialog.setIcon(R.drawable.warning);
        dd_dialog.setMessage("Please Activate Your Connection Internet...");
        dd_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dd_dialog.show();
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