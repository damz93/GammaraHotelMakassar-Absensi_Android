package app.gammarahotelmks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.ActionMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import app.gammarahotelmks.bantuan.Act_set_get;
import app.gammarahotelmks.bantuan.ConnectionDetector;
import app.gammarahotelmks.bantuan.JSONParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static app.gammarahotelmks.R.id.tag_unhandled_key_event_manager;
import static app.gammarahotelmks.R.id.tx_time;

public class Act_lihat_finger extends Activity implements View.OnClickListener {
    TextView t_id, t_nama,t_dept, t_refresh, t_about;
    String s_id, s_nama, s_tgl1, s_tgl2;
    ListView lv;
    EditText e_tgl1, e_tgl2;
    LinearLayout ln_jtg;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private DatePickerDialog datePickerDialog;
    Button bt_proses, bt_logof;
    private AdView JadVi;
    private InterstitialAd intersialADDDD;
    JSONArray contacts = null;
    Act_set_get aa = new Act_set_get();
    Intent buka;
    ProgressDialog damz_dialog = null;
    //private static String url_lihat_finger = "http://damzdamzdamz.000webhostapp.com/damz_GHMFinger/damz_lihat_finger.php";
    private static String url_lihat_finger = "https://ridercadel.com/damz/damz_lihat_finger.php";
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_list2);
        lv = (ListView) findViewById(R.id.list_absen);
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        e_tgl1 = (findViewById(R.id.ed_tgl1));
        e_tgl2 = (findViewById(R.id.ed_tgl2));
        t_id = (findViewById(R.id.tx_finger));
        t_nama = (findViewById(R.id.tx_nama));
        t_dept = (findViewById(R.id.tx_dept));
        t_dept.setText(aa.getdept());
        t_refresh = (findViewById(R.id.tx_refresh));
        t_id.setText(aa.getusnme());
        t_nama.setText(aa.getnama());
        ln_jtg = (findViewById(R.id.ln_tgl_jam));
        ln_jtg.setVisibility(View.INVISIBLE);
        //all iklan
        JadVi = findViewById(R.id.adview_);
        JadVi.loadAd(new AdRequest.Builder().build());

//menyiapkan iklan untuk intersial ID
        intersialADDDD = new InterstitialAd(Act_lihat_finger.this);
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


        bt_proses = (findViewById(R.id.bt_proses));
        bt_proses.setOnClickListener(this);
        bt_logof = (findViewById(R.id.btn_cus));
        bt_logof.setOnClickListener(this);
        t_refresh.setOnClickListener(this);
        e_tgl1.setOnClickListener(this);
        e_tgl2.setOnClickListener(this);
        t_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        s_tgl1 = e_tgl1.getText().toString();
        s_tgl2 = e_tgl2.getText().toString();
        if (v == e_tgl1){
            showDateDialog1();
        }
        else if (v==e_tgl2) {
            showDateDialog2();
        }
        else if(v == t_refresh){
            e_tgl1.setText("");
            e_tgl2.setText("");
            lv.setVisibility(View.INVISIBLE);
            ln_jtg.setVisibility(View.INVISIBLE);
        }
        else if(v == bt_logof){
           onBackPressed();
        }
        else if(v == bt_proses){
            if ((s_tgl1.length()>0 && (s_tgl2.length()>0))) {
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                try {
                    if (isInternetPresent) {
                        new AmbilData().execute();
                    }
                    else{
                        inet_mati();
                    }
                }
                catch (Exception e) {
                      Toast.makeText(Act_lihat_finger.this, "Errornya: " + e, Toast.LENGTH_LONG).show();
                    }
            }
            else{
                try {
                    AlertDialog damz_dialog = new AlertDialog.Builder(Act_lihat_finger.this).create();
                    damz_dialog.setTitle("Warning");
                    damz_dialog.setIcon(R.drawable.warning);
                    damz_dialog.setMessage("Please complete the field!");
                    damz_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    damz_dialog.show();
                    e_tgl1.requestFocus();
                } catch (Exception e) {
                }
            }

        }
    }
    public class AmbilData extends AsyncTask < String, String, String > {
        ArrayList< HashMap < String, String >> contactList = new ArrayList < HashMap < String, String >> ();@
                Override
        protected void onPreExecute() {
            super.onPreExecute();
            damz_dialog = new ProgressDialog(Act_lihat_finger.this);
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
            String aa = a.getusnme();
            JSONObject json = jParser.ambilURL(url_lihat_finger + "?tgl1="+s_tgl1+"&tgl2="+s_tgl2+"&ID="+aa);
            try {
                contacts = json.getJSONArray("FINGER");
                for (i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    HashMap< String, String > map = new HashMap < String, String > ();
                    String tgl = c.getString("TGL").trim();
                    String jam = c.getString("JAM").trim();
                    String fid = c.getString("FID").trim();
                    String unk = c.getString("UNK").trim();
                    if (fid.equals("0")){
                        fid = "FI";
                    }
                    else{
                        fid = "FO";
                    }
                    if (unk.equals("1")){
                        unk = "J";
                    }
                    else {
                        unk = "F";
                    }
                    map.put("TGLX", tgl);
                    map.put("JAMX", jam);
                    map.put("FIDX", fid);
                    map.put("UNKX", unk);
                    contactList.add(map);
                }

            } catch (JSONException e) {

            }

            return null;
        }@
                Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            damz_dialog.dismiss();
            ListAdapter adapter2 = new SimpleAdapter(getApplicationContext(),contactList,
                    R.layout.lay_lihat_finger, new String[] {"TGLX", "JAMX", "FIDX","UNKX"},
                    new int[] {R.id.tx_date, R.id.tx_time, R.id.tx_chk, R.id.tx_fing});
            lv.setAdapter(adapter2);
            lv.setVisibility(View.VISIBLE);
            ln_jtg.setVisibility(View.VISIBLE);
            //Toast.makeText(Act_lihat_finger.this,"Isi id: ",Toast.LENGTH_LONG).show();
        }
    }
    private void showDateDialog1(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                s_tgl1= dateFormatter.format(newDate.getTime());
                //Toast.makeText(Act_lihat_finger.this,"Isi tanggal : "+s_tgl1,Toast.LENGTH_LONG).show();
                e_tgl1.setText(s_tgl1);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    private void showDateDialog2(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                s_tgl2= dateFormatter.format(newDate.getTime());
                e_tgl2.setText(s_tgl2);
                //Toast.makeText(Act_lihat_finger.this,"Isi tanggal : "+s_tgl2,Toast.LENGTH_LONG).show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    public void inet_mati() {
        AlertDialog dd_dialog = new AlertDialog.Builder(Act_lihat_finger.this).create();
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
        Intent n = new Intent(Act_lihat_finger.this, Act_menutama.class);
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
