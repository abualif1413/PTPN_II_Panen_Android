package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EditTrip020Activity extends AppCompatActivity {

    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    List<Blok> blokList;
    List<String> stringBlokList;
    EditText txtJlhJanjang, txtRestanAwal, txtJlhBrondolan, txtNoSPTBS, txtNoPolisi;
    Spinner spinnerBlok;
    Button btnPush, btnHapusSptbs;
    ListView lvwTripDetail;

    String tglSekarang;
    String id_trip;

    List<HashMap<String, String>> listDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip020);

        id_trip = getIntent().getStringExtra("id_trip");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lihat Trip 020");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" +
                "SIlahkan input data trip / pengangkutan disini");

        HashMap<String, String> dataTrip = sqLiteHandler.ambilTrip020(Integer.parseInt(id_trip));
        txtJlhBrondolan = findViewById(R.id.txtJlhBrondolan);
        txtNoSPTBS = findViewById(R.id.txtNoSPTBS);
        txtNoPolisi = findViewById(R.id.txtNoPolisi);
        txtJlhBrondolan.setText(dataTrip.get("jlh_brondolan"));
        txtNoSPTBS.setText(dataTrip.get("sptbs"));
        txtNoPolisi.setText(dataTrip.get("no_polisi"));



        btnHapusSptbs = findViewById(R.id.btnHapusSptbs);
        btnHapusSptbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHandler.deleteTrip020(Integer.parseInt(id_trip));
                Intent intent = new Intent(EditTrip020Activity.this, TripActivity.class);
                startActivity(intent);
            }
        });

        lvwTripDetail = findViewById(R.id.lvwTripDetail);
        lvwTripDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String id_detail = listDetail.get(position).get("id");
                //sqLiteHandler.deleteTrip020Detail(Integer.parseInt(id_detail));
                //loadDetail();
            }
        });
        loadDetail();
    }

    void kosongDetail() {
        spinnerBlok.setSelection(0);
        txtJlhJanjang.setText("");
        txtRestanAwal.setText("");
    }

    void loadDetail() {
        //Toast.makeText(getApplicationContext(), "List diload", Toast.LENGTH_SHORT).show();
        listDetail = sqLiteHandler.listTrip020Detail(Integer.parseInt(id_trip));
        SimpleAdapter adapter = new SimpleAdapter(this, listDetail, R.layout.list_item_001, new String[] {"main", "sub"}, new int[] {R.id.listItem001MainText, R.id.listItem001SubText});
        lvwTripDetail.setAdapter(adapter);

        ListAdapter listAdapter = lvwTripDetail.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvwTripDetail);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvwTripDetail.getLayoutParams();
        params.height = totalHeight + (lvwTripDetail.getDividerHeight() * (listAdapter.getCount() - 1));
        lvwTripDetail.setLayoutParams(params);
        lvwTripDetail.requestLayout();
    }
}
