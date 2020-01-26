package com.ptpn.panen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.ptpn.panen.adapter.AdapterPanen;
import com.ptpn.panen.adapter.AdapterTrip;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.ListViewAdapterPanen;
import com.ptpn.panen.entity.ListViewAdapterTrip;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TripActivity extends AppCompatActivity {

    ListView lvwDaftarTrip;
    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    String tglSekarang;

    List<ListViewAdapterTrip> trips;

    List<HashMap<String, String>> listDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Trip");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss").format(calendar.getTime());

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " +
                keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" +
                "Panen tgl. " + AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL) + ".\n" +
                "Klik icon tambah dikanan atas untuk memulai input data trip");

        lvwDaftarTrip = findViewById(R.id.lvwDaftarTrip);
        loadTrip();
        lvwDaftarTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TripActivity.this, EditTrip020Activity.class);
                intent.putExtra("id_trip", listDetail.get(position).get("id"));
                startActivity(intent);
            }
        });
    }

    private void loadTrip() {
        /*trips = sqLiteHandler.getListTrip(AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.MYSQL_HANYA_TANGGAL));

        if(trips.size() > 0) {
            AdapterTrip adapterTrip = new AdapterTrip(this, trips);
            lvwDaftarTrip.setAdapter(adapterTrip);
        }*/
        listDetail = sqLiteHandler.listTrip020(AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.MYSQL_HANYA_TANGGAL));
        SimpleAdapter adapter = new SimpleAdapter(this, listDetail, R.layout.list_item_001, new String[] {"main", "sub"}, new int[] {R.id.listItem001MainText, R.id.listItem001SubText});
        lvwDaftarTrip.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_005, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item005TambahPanen :
                Intent intent = new Intent(TripActivity.this, ProsesTrip020Activity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(TripActivity.this, KcsPanel.class);
        startActivity(intent);
    }
}
