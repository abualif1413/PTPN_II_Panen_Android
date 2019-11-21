package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.SQLiteHandler;

public class DetailKehadiranActivity extends AppCompatActivity {

    String idPekerja;
    String untukTanggal;

    Pemanen pemanen;
    SQLiteHandler sqLiteHandler;

    TextView txtNamaPekerja, txtKodeAbsen, txtUntukTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kehadiran);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Data Absensi");
        setSupportActionBar(toolbar);

        idPekerja = getIntent().getStringExtra("ID_PEKERJA");
        untukTanggal = getIntent().getStringExtra("TANGGAL");

        sqLiteHandler = new SQLiteHandler(this);
        pemanen = sqLiteHandler.getPemanen(Integer.parseInt(idPekerja));

        txtNamaPekerja = findViewById(R.id.detailKehadiran_tvNamaPemanen);
        txtKodeAbsen = findViewById(R.id.detailKehadiran_tvQRCode);
        txtUntukTanggal = findViewById(R.id.detailKehadiran_tvUntukTanggal);

        txtNamaPekerja.setText(pemanen.getNamaPemanen());
        txtKodeAbsen.setText(pemanen.getBarcode());
        txtUntukTanggal.setText(AppCommon.ubahFormatTanggal(untukTanggal + " 00:00:00", FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL));
    }
}
