package app.gammarahotelmks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.gammarahotelmks.bantuan.Act_set_get;
import app.gammarahotelmks.bantuan.ConnectionDetector;
import app.gammarahotelmks.bantuan.JSONParser;

public class Act_early extends Activity implements View.OnClickListener{
    Spinner spn_bulan;
    String bulan, id;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    LinearLayout ln_jtg3;
    ProgressDialog damz_dialog = null;
    TextView tx_nama, tx_id, tx_dep, tx_late, tx_early, t_refresh;
    JSONArray contacts = null;
    Button bt_pros, bt_bck;
    ListView lv;
    private AdView JadVi;
    private InterstitialAd intersialADDDD;
    Act_set_get stg3 = new Act_set_get();
    private String[] sp_bulan = {
            "January 2020",
            "February 2020",
            "March 2020",
            "April 2020",
            "May 2020",
            "June 2020",
            "July 2020",
            "Agust 2020",
            "September 2020",
            "October 2020",
            "November 2020",
            "December 2020"
    };
    private static String url_lihat_hasil = "https://ridercadel.com/damz/damz_hasil_absen.php";
    private static String url_lihat_hasil2 = "https://ridercadel.com/damz/damz_hasil_absen2.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_list3);
        lv = (ListView) findViewById(R.id.list_early);

        bt_bck = (findViewById(R.id.btn_cus3));
        bt_bck.setOnClickListener(this);
        bt_pros = (findViewById(R.id.bt_proses3));
        bt_pros.setOnClickListener(this);
        tx_id = (findViewById(R.id.tx_finger3));
        tx_id.setText(stg3.getusnme());
        t_refresh = findViewById(R.id.tx_refresh3);
        t_refresh.setOnClickListener(this);
        tx_nama = (findViewById(R.id.tx_nama3));
        tx_nama.setText(stg3.getnama());
        tx_dep = (findViewById(R.id.tx_dept3));
        tx_dep.setText(stg3.getdept());
        tx_late = (findViewById(R.id.tx_late));
        ln_jtg3 = (findViewById(R.id.ln_tgl_jam3));
        ln_jtg3.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.INVISIBLE);
        tx_early = (findViewById(R.id.tx_early));
        spn_bulan = (Spinner) findViewById(R.id.spn_bulan);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sp_bulan);

        // mengeset Array Adapter tersebut ke Spinner
        spn_bulan.setAdapter(adapter);

        spn_bulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String aaa = adapter.getItem(position);
                if (aaa.equals("January 2020")){
                    bulan="01";
                }
                else if (aaa.equals("February 2020")){
                    bulan="02";
                }
                else if (aaa.equals("March 2020")){
                    bulan="03";
                }
                else if (aaa.equals("April 2020")){
                    bulan="04";
                }
                else if (aaa.equals("May 2020")){
                    bulan="05";
                }
                else if (aaa.equals("June 2020")){
                    bulan="06";
                }
                else if (aaa.equals("July 2020")){
                    bulan="07";
                }
                else if (aaa.equals("Agust 2020")){
                    bulan="08";
                }
                else if (aaa.equals("September 2020")){
                    bulan="09";
                }
                else if (aaa.equals("October 2020")){
                    bulan="10";
                }
                else if (aaa.equals("November 2020")){
                    bulan="11";
                }
                else {
                    bulan="12";
                }
                //Toast.makeText(Act_early.this, "Selected " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
             //   Toast.makeText(Act_early.this, "Selected " + bulan, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //all iklan
        JadVi = findViewById(R.id.adview_);
        JadVi.loadAd(new AdRequest.Builder().build());

//menyiapkan iklan untuk intersial ID
        intersialADDDD = new InterstitialAd(Act_early.this);
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
        id = tx_id.getText().toString();
        if (v == bt_pros) {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            try {
                if (isInternetPresent) {

                    new AmbilData().execute();
                    new AmbilData2().execute();
                } else {
                    inet_mati();
                }
            } catch (Exception e) {
                Toast.makeText(Act_early.this, "Errornya: " + e, Toast.LENGTH_LONG).show();
            }
        }
        else if (v == bt_bck){
            onBackPressed();
        }
        else if (v == t_refresh){
            lv.setVisibility(View.INVISIBLE);
            ln_jtg3.setVisibility(View.INVISIBLE);
            tx_early.setText("Total Early ..... Minutes");
            tx_late.setText("Total Late ..... Minutes");
        }
    }
    public class AmbilData extends AsyncTask< String, String, String > {
        String latezz, earlyzz;
        ArrayList<HashMap< String, String >> contactList = new ArrayList < HashMap < String, String >> ();@
                Override
        protected void onPreExecute() {
            super.onPreExecute();
            damz_dialog = new ProgressDialog(Act_early.this);
            damz_dialog.setMessage("Loading Data ...");
            damz_dialog.setIndeterminate(false);
            damz_dialog.setCancelable(false);
            damz_dialog.show();
        }

        @
                Override
        protected String doInBackground(String...arg0) {

            JSONParser jParser = new JSONParser();
            int i;
            Act_set_get a = new Act_set_get();
            @SuppressLint("WrongThread") JSONObject json = jParser.ambilURL(url_lihat_hasil + "?ID="+tx_id.getText().toString()+"&BULAN="+bulan);
            String aa = a.getusnme();
            try {
                contacts = json.getJSONArray("ABSEN");
                for (i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    HashMap< String, String > map = new HashMap < String, String > ();
                    String early = c.getString("early").trim();
                    if (early.equals("null")){
                        early="0";
                    }
                    early = "Total Early "+early+" Minutes";
                    String late = c.getString("late").trim();
                    if (late.equals("null")){
                        late="0";
                    }
                    late = "Total Late "+late+" Minutes";
                    earlyzz = early;
                    latezz = late;
                }

            } catch (JSONException e) {

            }

            return null;
        }@
                Override
        protected void onPostExecute(String result) {
            //String anu=url_lihat_finger + "?ID="+tx_id.getText().toString()+"&BULAN="+bulan;
            super.onPostExecute(result);
            damz_dialog.dismiss();
            tx_late.setText(latezz);
            tx_early.setText(earlyzz);
          //  tx_early.setText(anu);


        }
    }
    public void inet_mati() {
        AlertDialog dd_dialog = new AlertDialog.Builder(Act_early.this).create();
        dd_dialog.setTitle("Warning");
        dd_dialog.setIcon(R.drawable.warning);
        dd_dialog.setMessage("Please activate your connection internet...");
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
        Intent n = new Intent(Act_early.this, Act_menutama.class);
        finish();
        startActivity(n);
    }
    public class AmbilData2 extends AsyncTask < String, String, String > {
        ArrayList< HashMap < String, String >> contactList = new ArrayList < HashMap < String, String >> ();@
                Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @
                Override
        protected String doInBackground(String...arg0) {

            JSONParser jParser = new JSONParser();
            int i;
            Act_set_get a = new Act_set_get();
            String aa = a.getusnme();
            @SuppressLint("WrongThread") JSONObject json = jParser.ambilURL(url_lihat_hasil2 + "?ID="+tx_id.getText().toString()+"&BULAN="+bulan);
            try {
                contacts = json.getJSONArray("ABSEN2");
                for (i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    HashMap< String, String > map = new HashMap < String, String > ();
                    String dateex = c.getString("datee").trim();
                    String on_dutyx = c.getString("on_duty").trim();
                    String clock_inx = c.getString("clock_in").trim();
                    String onduty_clockinx = c.getString("onduty_clockin").trim();
                    String late_ondutyx = c.getString("late_onduty").trim();
                    String hasil_earlyx = c.getString("hasil_early").trim();
                    String hasil_latex = c.getString("hasil_late").trim();
                    map.put("dateexz", dateex);
                    map.put("on_dutyxz", on_dutyx);
                    map.put("clock_inxz", clock_inx);
                    map.put("onduty_clockinxz", onduty_clockinx);
                    map.put("late_ondutyxz", late_ondutyx);
                    map.put("hasil_earlyxz", hasil_earlyx);
                    map.put("hasil_latexz", hasil_latex);
                    contactList.add(map);
                }

            } catch (JSONException e) {

            }

            return null;
        }@
                Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),contactList,
                    R.layout.lay_early, new String[] {"dateexz", "on_dutyxz", "clock_inxz","onduty_clockinxz","late_ondutyxz","hasil_earlyxz","hasil_latexz"},
                    new int[] {R.id.tx_dateear, R.id.tx_onduty, R.id.tx_clin, R.id.tx_ondutyclkn, R.id.tx_late_ondty, R.id.tx_hsilearl, R.id.tx_hasilate});
            ln_jtg3.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            lv.setAdapter(adapter2);

            //Toast.makeText(Act_lihat_finger.this,"Isi id: ",Toast.LENGTH_LONG).show();
        }
    }
}
