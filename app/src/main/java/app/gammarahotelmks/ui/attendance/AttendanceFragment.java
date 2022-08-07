package app.gammarahotelmks.ui.attendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;

import java.text.SimpleDateFormat;

import app.gammarahotelmks.R;
import app.gammarahotelmks.bantuan.Act_set_get;
import app.gammarahotelmks.bantuan.ConnectionDetector;

public class AttendanceFragment extends Fragment {
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

    private AttendanceViewModel attendanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        attendanceViewModel =
                ViewModelProviders.of(this).get(AttendanceViewModel.class);
        View root = inflater.inflate(R.layout.lay_list2, container, false);
        return root;
    }
}