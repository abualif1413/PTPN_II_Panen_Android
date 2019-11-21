package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.Absen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.SQLiteHandler;

public class TidakHadirActivity extends AppCompatActivity {

    String idPekerja;
    String untukTanggal;

    Pemanen pemanen;
    SQLiteHandler sqLiteHandler;

    TextView txtNamaPekerja, txtKodeAbsen, txtUntukTanggal;
    Spinner listJenisIjin;
    EditText txtKeteranganIjin;
    Button btnSimpanIjin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tidak_hadir);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Status Ijin");
        setSupportActionBar(toolbar);

        idPekerja = getIntent().getStringExtra("ID_PEKERJA");
        untukTanggal = getIntent().getStringExtra("TANGGAL");

        sqLiteHandler = new SQLiteHandler(this);
        pemanen = sqLiteHandler.getPemanen(Integer.parseInt(idPekerja));

        txtNamaPekerja = findViewById(R.id.detailKehadiran_tvNamaPemanen);
        txtKodeAbsen = findViewById(R.id.detailKehadiran_tvQRCode);
        txtUntukTanggal = findViewById(R.id.detailKehadiran_tvUntukTanggal);

        listJenisIjin = findViewById(R.id.listJenisIjin);
        txtKeteranganIjin = findViewById(R.id.txtKeteranganIjin);
        btnSimpanIjin = findViewById(R.id.btnSimpanIjin);

        txtNamaPekerja.setText(pemanen.getNamaPemanen());
        txtKodeAbsen.setText(pemanen.getBarcode());
        txtUntukTanggal.setText(AppCommon.ubahFormatTanggal(untukTanggal + " 00:00:00", FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL));

        btnSimpanIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexIjin = listJenisIjin.getSelectedItemPosition();
                String keterangan = txtKeteranganIjin.getText().toString();
                String[] ijinSingkat = v.getResources().getStringArray(R.array.jenis_ijin);
                String ijinnya = ijinSingkat[indexIjin];

                if(indexIjin == 0 || keterangan.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Jenis ijin dan keterangan harus diisi", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Absen absen = new Absen();
                        absen.setId_absen(pemanen.getBarcode());
                        absen.setId_mandor(sqLiteHandler.getDataMandorAsPreference().getToken());
                        absen.setKehadiran(ijinnya);
                        absen.setKeterangan(keterangan);
                        int bisaAbsen = sqLiteHandler.serviceSimpanAbsen(absen);
                        if(bisaAbsen == 0) {
                            Toast.makeText(getApplicationContext(), "Nomor barcode ini tidak ditemukan pada afdeling yang dikelola", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(TidakHadirActivity.this, KehadiranActivity.class);
                            startActivity(intent);
                        }
                    } catch(Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
