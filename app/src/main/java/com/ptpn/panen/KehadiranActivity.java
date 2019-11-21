package com.ptpn.panen;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ptpn.panen.adapter.AdapterKehadiranPekerja;
import com.ptpn.panen.entity.Absen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.AppPreferenceConstant;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.ListViewAdapterKehadiranPekerja;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.List;
import java.util.Calendar;

public class KehadiranActivity extends AppCompatActivity {

    SQLiteHandler sqLiteHandler;
    List<ListViewAdapterKehadiranPekerja> kehadiranPekerja;

    TextView txtInfoAfdeling;
    public static TextView txtHasilQR;
    ListView lvwKehadiran;
    String tglSekarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kelola Absensi");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);
        ListViewAdapterKebunAfdeling infoKebun = sqLiteHandler.getAfdelingPreference();
        Mandor mandorPreference = sqLiteHandler.getDataMandorAsPreference();

        //txtHasilQR = findViewById(R.id.txtHasilQR);
        lvwKehadiran = findViewById(R.id.lvwKehadiranPekerja);
        loadAbsenPekerja();

        txtInfoAfdeling = findViewById(R.id.txtInfoAfdeling);
        txtInfoAfdeling.setText(infoKebun.getNamaKebun() + " - " + infoKebun.getNamaAfdeling() + "\n" +"Mandor : " + mandorPreference.getNamaLengkap() + "\n" + "Email : " + mandorPreference.getEmail() + "\n" +
                "Kelola absensi dan kehadiran tgl. " + AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL));

        lvwKehadiran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewAdapterKehadiranPekerja hadir = kehadiranPekerja.get(position);
                String idPekerja = String.valueOf(hadir.getIdPekerja());
                String tanggal = tglSekarang;

                if(hadir.getStatusKehadiran().equalsIgnoreCase("")) {
                    Intent intentDetail = new Intent(KehadiranActivity.this, TidakHadirActivity.class);
                    intentDetail.putExtra("ID_PEKERJA", idPekerja);
                    intentDetail.putExtra("TANGGAL", tanggal);
                    startActivity(intentDetail);
                }
            }
        });
    }

    private void loadAbsenPekerja() {
        Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

        kehadiranPekerja = sqLiteHandler.getListViewAdapterKehadiranPekerja(AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.MYSQL_HANYA_TANGGAL));
        AdapterKehadiranPekerja adapterKehadiranPekerja = new AdapterKehadiranPekerja(this, kehadiranPekerja);
        lvwKehadiran.setAdapter(adapterKehadiranPekerja);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_003, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item003TambahAbsen :
                //Toast.makeText(getApplicationContext(), "Tambah absensi", Toast.LENGTH_SHORT).show();
                IntentIntegrator integrator = new IntentIntegrator(KehadiranActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.addExtra("PROMPT_MESSAGE", "Scan kartu absensi di sini");
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();

                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Barcode tidak terdeteksi", Toast.LENGTH_SHORT).show();
            } else {
                //txtHasilQR.setText(result.getContents());
                String hasil_barcode = result.getContents();

                Absen absen = new Absen();
                absen.setId_absen(hasil_barcode);
                absen.setId_mandor(sqLiteHandler.getDataMandorAsPreference().getToken());
                absen.setKehadiran("H");
                int bisaAbsen = sqLiteHandler.serviceSimpanAbsen(absen);
                if(bisaAbsen == 0) {
                    Toast.makeText(getApplicationContext(), "Nomor barcode ini tidak ditemukan pada afdeling yang dikelola", Toast.LENGTH_LONG).show();
                }
                loadAbsenPekerja();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(KehadiranActivity.this, MandorPanel.class);
        startActivity(intent);
    }
}
