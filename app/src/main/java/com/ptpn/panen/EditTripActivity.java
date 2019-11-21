package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Trip;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTripActivity extends AppCompatActivity {

    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    List<Blok> blokList;
    List<String> stringBlokList;


    EditText txtNoPolisi;
    EditText txtNoSPTBS, txtNoSPTBSBuntut;
    Spinner spinnerBlok1;
    EditText txtJlhJanjang1;
    EditText txtBrondolan1;
    Spinner spinnerBlok2;
    EditText txtJlhJanjang2;
    EditText txtBrondolan2;
    Spinner spinnerBlok3;
    EditText txtJlhJanjang3;
    EditText txtBrondolan3;
    Button btnSimpanTrip;
    Button btnHapusTrip;

    String tglSekarang;

    Trip trip;

    String id_trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Trip");
        setSupportActionBar(toolbar);

        id_trip = getIntent().getStringExtra("id_trip");

        sqLiteHandler = new SQLiteHandler(this);

        trip = sqLiteHandler.getTripObject(id_trip);

                Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" +
                "SIlahkan input data trip / pengangkutan disini");

        btnSimpanTrip = findViewById(R.id.btnSimpanTrip);
        btnHapusTrip = findViewById(R.id.btnHapusTrip);
        txtNoPolisi = findViewById(R.id.txtNoPolisi);
        txtNoSPTBS = findViewById(R.id.txtNoSPTBS);
        txtNoSPTBSBuntut = findViewById(R.id.txtNoSPTBSBuntut);
        txtJlhJanjang1 = findViewById(R.id.txtJlhJanjang1);
        txtBrondolan1 = findViewById(R.id.txtJlhBrondolan1);
        txtJlhJanjang2 = findViewById(R.id.txtJlhJanjang2);
        txtBrondolan2 = findViewById(R.id.txtJlhBrondolan2);
        txtJlhJanjang3 = findViewById(R.id.txtJlhJanjang3);
        txtBrondolan3 = findViewById(R.id.txtJlhBrondolan3);

        txtNoPolisi.setText(trip.getNomor_polisi_trek());

        // Memecah no SPTBS supaya dia mecah ujung dan buntut nya
        String noSptbsNya = trip.getSptbs();
        String[] noSptbsNyaSplit = noSptbsNya.split("-");
        String noSptbsNyaKepala = noSptbsNyaSplit[0] + "-" + noSptbsNyaSplit[1] + "-" + noSptbsNyaSplit[2] + "-" + noSptbsNyaSplit[3] + "-";
        String noSptbsNyaBuntut = noSptbsNyaSplit[4];
        txtNoSPTBS.setEnabled(false);
        txtNoSPTBS.setText(noSptbsNyaKepala);
        txtNoSPTBSBuntut.setText(noSptbsNyaBuntut);
        txtJlhJanjang1.setText(trip.getJumlah_janjang_1());
        txtBrondolan1.setText(trip.getJumlah_brondolan_1() + "");
        txtJlhJanjang2.setText(trip.getJumlah_janjang_2());
        txtBrondolan2.setText(trip.getJumlah_brondolan_2() + "");
        txtJlhJanjang3.setText(trip.getJumlah_janjang_3());
        txtBrondolan3.setText(trip.getJumlah_brondolan_3() + "");

        blokList = sqLiteHandler.getListBlok();
        stringBlokList = new ArrayList<String>();
        stringBlokList.add("- Pilih Blok  -");
        for (Blok blk : blokList) {
            stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
        }

        ArrayAdapter<String> dataAdapterBlok = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringBlokList);
        dataAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBlok1 = findViewById(R.id.listBlok1);
        spinnerBlok1.setAdapter(dataAdapterBlok);
        int index_spinner_blok1 = -1;
        for (int i=0; i<blokList.size(); i++) {
            if(trip.getId_blok_1().equals(blokList.get(i).getId())) {
                index_spinner_blok1 = i;
                break;
            }
        }
        spinnerBlok1.setSelection(index_spinner_blok1 + 1);

        spinnerBlok2 = findViewById(R.id.listBlok2);
        spinnerBlok2.setAdapter(dataAdapterBlok);
        int index_spinner_blok2 = -1;
        for (int i=0; i<blokList.size(); i++) {
            if(trip.getId_blok_2().equals(blokList.get(i).getId())) {
                index_spinner_blok2 = i;
                break;
            }
        }
        spinnerBlok2.setSelection(index_spinner_blok2 + 1);

        spinnerBlok3 = findViewById(R.id.listBlok3);
        spinnerBlok3.setAdapter(dataAdapterBlok);
        int index_spinner_blok3 = -1;
        for (int i=0; i<blokList.size(); i++) {
            if(trip.getId_blok_3().equals(blokList.get(i).getId())) {
                index_spinner_blok3 = i;
                break;
            }
        }
        spinnerBlok3.setSelection(index_spinner_blok3 + 1);

        btnSimpanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtJlhJanjang1.getText().toString().equals("") || txtJlhJanjang2.getText().toString().equals("") || txtJlhJanjang3.getText().toString().equals("") || txtNoPolisi.getText().toString().equals("") || txtNoSPTBSBuntut.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Data yang diinput belum lengkap", Toast.LENGTH_LONG).show();
                } else {
                    int positionBlok1 = spinnerBlok1.getSelectedItemPosition();
                    int positionBlok2 = spinnerBlok2.getSelectedItemPosition();
                    int positionBlok3 = spinnerBlok3.getSelectedItemPosition();

                    String id_blok1 = (positionBlok1 == 0 ? "0" : blokList.get(positionBlok1 - 1).getId());
                    String id_blok2 = (positionBlok2 == 0 ? "0" : blokList.get(positionBlok2 - 1).getId());
                    String id_blok3 = (positionBlok3 == 0 ? "0" : blokList.get(positionBlok3 - 1).getId());

                    int jlh_brondolan1 = (txtBrondolan1.getText().toString().equals("") ? 0 : Integer.parseInt(txtBrondolan1.getText().toString()));
                    int jlh_brondolan2 = (txtBrondolan2.getText().toString().equals("") ? 0 : Integer.parseInt(txtBrondolan2.getText().toString()));
                    int jlh_brondolan3 = (txtBrondolan3.getText().toString().equals("") ? 0 : Integer.parseInt(txtBrondolan3.getText().toString()));

                    trip.setId_blok_1(id_blok1);
                    int nobun = Integer.parseInt(txtNoSPTBSBuntut.getText().toString());
                    String strNobun = AppCommon.padLeftZeros(nobun + "", 4);
                    Log.d("Nobun", nobun + "");
                    Log.d("Nobun", strNobun);
                    trip.setSptbs(txtNoSPTBS.getText().toString() + strNobun);
                    trip.setJumlah_janjang_1(txtJlhJanjang1.getText().toString());
                    trip.setJumlah_brondolan_1(jlh_brondolan1);
                    trip.setId_blok_2(id_blok2);
                    trip.setJumlah_janjang_2(txtJlhJanjang2.getText().toString());
                    trip.setJumlah_brondolan_2(jlh_brondolan2);
                    trip.setId_blok_3(id_blok3);
                    trip.setJumlah_janjang_3(txtJlhJanjang3.getText().toString());
                    trip.setJumlah_brondolan_3(jlh_brondolan3);
                    trip.setNomor_polisi_trek(txtNoPolisi.getText().toString());
                    sqLiteHandler.updateTrip(trip);
                    Toast.makeText(getApplicationContext(), "Data trip telah diubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditTripActivity.this, TripActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnHapusTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHandler.deleteTrip(trip);
                Toast.makeText(getApplicationContext(), "Data trip telah dihapus", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditTripActivity.this, TripActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(EditTripActivity.this, TripActivity.class);
        startActivity(intent);
    }
}
