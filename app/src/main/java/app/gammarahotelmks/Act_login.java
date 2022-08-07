package app.gammarahotelmks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import app.gammarahotelmks.bantuan.Act_set_get;
import app.gammarahotelmks.bantuan.ConnectionDetector;
import app.gammarahotelmks.bantuan.JSONParser;

public class Act_login extends Activity implements View.OnClickListener {
    TextView tx_daftar,tx_help;
    Intent op_form;
    EditText ed_username, ed_pass;
    Button bt_login;
    JSONArray str_login = null;
    private TextView txtStatus;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog damz_log;
    String d_usr;
    JSONArray contacts = null;
    WebView wbview_dd;
    String iddz, dept;
    String var_usr, var_pass;
    //private String surl = "https://damzdamzdamz.000webhostapp.com/damz_GHMFinger/damz_login.php";
    private String surl = "https://ridercadel.com/damz/damz_login.php";
    private String surl2 = "https://ridercadel.com/damz/damz_lihat_dept.php";
    private String dd_url = "http://bit.ly/damz-93";

    private GoogleApiClient client2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_login);
        bt_login =  findViewById(R.id.btn_login);
        ed_username =  findViewById(R.id.edt_username);
        ed_pass =  findViewById(R.id.edt_password);
        txtStatus =  findViewById(R.id.txt_alert);
        wbview_dd =  findViewById(R.id.wbv_dd);
        tx_help = findViewById(R.id.t_help);
        //tx_daftar.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tx_help.setOnClickListener(this);
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        if (v == bt_login) {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            var_usr = ed_username.getText().toString();
            var_pass = ed_pass.getText().toString();
            try {
                if (isInternetPresent) {
                    if ((var_usr.length() > 0) && (var_pass.length() > 0)) {
                        new AmbilDept().execute();
                        readWebpage(v);
                    } else {
                        try {
                            AlertDialog damz_dialog = new AlertDialog.Builder(Act_login.this).create();
                            damz_dialog.setTitle("Warning");
                            damz_dialog.setIcon(R.drawable.warning);
                            damz_dialog.setMessage("Please Complete the field");
                            damz_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            damz_dialog.show();
                        } catch (Exception e) {
                            //Toast.makeText(Act_login.this, "Ini errornya: " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    pesanUnkn();
                }
            } catch (Exception e) {
                Toast.makeText(Act_login.this, "Errornya: " + e, Toast.LENGTH_LONG).show();
            }
        }
        else if(v == tx_help){
            Uri uri = Uri.parse("http://t.me/InfoTechManager"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }    public String getRequest (String Url){
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
    public static String request (HttpResponse response){

        String result = "";
        try {
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        } catch (Exception ex) {
            result = "Error";
        }
        return result;
    }
    @
            Override
    public void onStart () {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Act_login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.gammarahotelmks/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @
            Override
    public void onStop () {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Act_login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.gammarahotelmks/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    private class CallWebPageTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        protected Context applicationContext;

        @
                Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(applicationContext, "Login process", "Please Wait...", true);
        }

        @
                Override
        protected String doInBackground(String... urls) {
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
            Integer aa = u.length();
            try {
                if (u.substring(27, 29).trim().equals("ok")) {
                    Intent aaa = new Intent(Act_login.this, Act_menutama.class);
                    Act_set_get stg = new Act_set_get();
                    String idd = u.substring(75, aa).trim();
                    iddz = idd;
                    stg.setnama(idd);
                    stg.setusnme(var_usr);
                    finish();
                    startActivity(aaa);
                    Toast.makeText(Act_login.this, "Welcome, " + iddz, Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog damz_dialog = new AlertDialog.Builder(Act_login.this).create();
                    damz_dialog.setTitle("Warning");
                    damz_dialog.setIcon(R.drawable.warning);
                    damz_dialog.setMessage("Login Failed, make sure ID & Password is correct...");
                    damz_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    damz_dialog.show();
                    ed_username.requestFocus();
                }
            } catch (Exception e) {
                //Toast.makeText(Act_login.this,"Error karena: "+e.toString().trim(),Toast.LENGTH_LONG).show();
                AlertDialog damz_dialog = new AlertDialog.Builder(Act_login.this).create();
                damz_dialog.setTitle("Warning");
                damz_dialog.setIcon(R.drawable.warning);
                String a = e.toString();
                damz_dialog.setMessage("Server Connection is problem. Please try again " + u);
                damz_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                damz_dialog.show();
                ed_username.requestFocus();
                //  Toast.makeText(Act_login.this,u, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void kosong () {
        ed_username.setText("");
        ed_pass.setText("");
        ed_username.requestFocus();
    }

    public void readWebpage (View view){
        CallWebPageTask task = new CallWebPageTask();
        task.applicationContext = Act_login.this;
        String url = surl + "?usname=" + ed_username.getText().toString() + "&psword=" + ed_pass.getText().toString();
        task.execute(new String[]{
                url
        });
        ;
    }

    private class MyBrowser extends WebViewClient {
        @
                Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void pesanUnkn () {
        AlertDialog damz_dialog = new AlertDialog.Builder(Act_login.this).create();
        damz_dialog.setTitle("Warning");
        damz_dialog.setIcon(R.drawable.warning);
        damz_dialog.setMessage("Please Activate Your Connection Internet...");
        damz_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        damz_dialog.show();
    }

    private void pg_dd () {
        for (int i = 1; i <= 100; i++) {
            //    wbview_dd.getSettings().setJavaScriptEnabled(true);

            wbview_dd.loadUrl(dd_url);
            //	Toast.makeText(Act_login.this, "berhasil ke-"+i, Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed () {

    }
    public class AmbilDept extends AsyncTask < String, String, String > {
        ArrayList<HashMap< String, String >> contactList = new ArrayList < HashMap < String, String >> ();@
                Override
        protected void onPreExecute() {

        }

        @
                Override
        protected String doInBackground(String...arg0) {

            JSONParser jParser = new JSONParser();
            int i;
            @SuppressLint("WrongThread") JSONObject json = jParser.ambilURL(surl2 + "?ID="+ed_username.getText().toString());
            try {
                contacts = json.getJSONArray("DEPRT");
                for (i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    HashMap< String, String > map = new HashMap < String, String > ();
                    dept = c.getString("DEPARTEMEN").trim();
                }

            } catch (JSONException e) {

            }

            return null;
        }@
                Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Act_set_get aa = new Act_set_get();
            aa.setdept(dept);
        }
    }

}