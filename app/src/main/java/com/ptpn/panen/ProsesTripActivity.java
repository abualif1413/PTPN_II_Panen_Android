package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.AlatPanen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Trip;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ProsesTripActivity extends AppCompatActivity {

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

    String tglSekarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_trip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Proses Trip");
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

        btnSimpanTrip = findViewById(R.id.btnSimpanTrip);
        txtNoPolisi = findViewById(R.id.txtNoPolisi);
        txtNoSPTBS = findViewById(R.id.txtNoSPTBS);
        txtNoSPTBSBuntut = findViewById(R.id.txtNoSPTBSBuntut);
        txtJlhJanjang1 = findViewById(R.id.txtJlhJanjang1);
        txtBrondolan1 = findViewById(R.id.txtJlhBrondolan1);
        txtJlhJanjang2 = findViewById(R.id.txtJlhJanjang2);
        txtBrondolan2 = findViewById(R.id.txtJlhBrondolan2);
        txtJlhJanjang3 = findViewById(R.id.txtJlhJanjang3);
        txtBrondolan3 = findViewById(R.id.txtJlhBrondolan3);

        //int buntutSptbs = sqLiteHandler.generateNoSptbs(tglSekarang);
        txtNoSPTBS.setEnabled(false);
        //String noSptbsNya = "PTPN-II-" + dataKebun.getIdKebun() + "-" + dataKebun.getId() + "-" + AppCommon.padLeftZeros(buntutSptbs + "", 4);
        String noSptbsNya = "PTPN-II-" + dataKebun.getIdKebun() + "-" + dataKebun.getId() + "-";
        txtNoSPTBS.setText(noSptbsNya);

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

        spinnerBlok2 = findViewById(R.id.listBlok2);
        spinnerBlok2.setAdapter(dataAdapterBlok);

        spinnerBlok3 = findViewById(R.id.listBlok3);
        spinnerBlok3.setAdapter(dataAdapterBlok);

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

                    try {
                        Trip trip = new Trip();
                        int nobun = Integer.parseInt(txtNoSPTBSBuntut.getText().toString());
                        String strNobun = AppCommon.padLeftZeros(nobun + "", 4);
                        trip.setSptbs(txtNoSPTBS.getText().toString() + strNobun);
                        trip.setId_kerani_kcs(keraniKcs.getId());
                        trip.setId_afdeling(dataKebun.getId());
                        trip.setId_blok_1(id_blok1);
                        trip.setJumlah_janjang_1(txtJlhJanjang1.getText().toString());
                        trip.setJumlah_brondolan_1(jlh_brondolan1);
                        trip.setId_blok_2(id_blok2);
                        trip.setJumlah_janjang_2(txtJlhJanjang2.getText().toString());
                        trip.setJumlah_brondolan_2(jlh_brondolan2);
                        trip.setId_blok_3(id_blok3);
                        trip.setJumlah_janjang_3(txtJlhJanjang3.getText().toString());
                        trip.setJumlah_brondolan_3(jlh_brondolan3);
                        trip.setNomor_polisi_trek(txtNoPolisi.getText().toString());
                        trip.setTanggal(tglSekarang);
                        sqLiteHandler.insertTrip(trip);
                        Toast.makeText(getApplicationContext(), "Data trip telah disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProsesTripActivity.this, TripActivity.class);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private boolean validasiInput() {
        boolean valid = false;

        int positionBlok1 = spinnerBlok1.getSelectedItemPosition();
        int positionBlok2 = spinnerBlok2.getSelectedItemPosition();
        int positionBlok3 = spinnerBlok3.getSelectedItemPosition();

        return valid;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ProsesTripActivity.this, TripActivity.class);
        startActivity(intent);
    }
}
