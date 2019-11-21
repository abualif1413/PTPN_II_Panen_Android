package com.ptpn.panen;

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
import com.ptpn.panen.adapter.AdapterPanen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.ListViewAdapterPanen;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.Calendar;
import java.util.List;

public class PanenActivity extends AppCompatActivity {

    ListView lvwDaftarPanen;
    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    String tglSekarang;
    List<ListViewAdapterPanen> panens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Panen");
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
                "Klik icon tambah dikanan atas untuk memulai input data panen");

        lvwDaftarPanen = findViewById(R.id.lvwDaftarPanen);
        loadPanen();

        lvwDaftarPanen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewAdapterPanen panen = panens.get(position);
                if(panen.getId_panen().equals("0")) {
                    String id_pemanen = panen.getId_pemanen();
                    Pemanen pemanen = sqLiteHandler.getPemanen(Integer.parseInt(id_pemanen));
                    Intent intent = new Intent(PanenActivity.this, ProsesPanenActivity.class);
                    intent.putExtra("barcode", pemanen.getBarcode());
                    startActivity(intent);
                }
            }
        });
    }

    private void loadPanen() {
        panens = sqLiteHandler.getDataPanenHarian(AppCommon.ubahFormatTanggal(tglSekarang, FORMAT_TANGGAL.MYSQL_HANYA_TANGGAL));
        AdapterPanen adapterPanen = new AdapterPanen(this, panens);
        lvwDaftarPanen.setAdapter(adapterPanen);
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
                //Toast.makeText(getApplicationContext(), "Mulai scan untuk panen", Toast.LENGTH_SHORT).show();
                IntentIntegrator integrator = new IntentIntegrator(PanenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.addExtra("PROMPT_MESSAGE", "Scan kartu absensi di sini untuk mencatat hasil panen");
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(PanenActivity.this, KcsPanel.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Barcode tidak terdeteksi", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getBaseContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                String qrcode = result.getContents();
                Pemanen pemanen = sqLiteHandler.getPemanenViaQRCode(qrcode);
                if(pemanen == null) {
                    Toast.makeText(getBaseContext(), "Data pemanen dengan barcode ini tidak ditemukan", Toast.LENGTH_SHORT).show();
                } else {
                    ListViewAdapterKebunAfdeling dataAfdeling = sqLiteHandler.getAfdelingPreference();
                    int id_afdeling = dataAfdeling.getId();
                    if(pemanen.getIdAfdeling().equalsIgnoreCase(Integer.toString(id_afdeling))) {
                        //Toast.makeText(getBaseContext(), "Boleh panen", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PanenActivity.this, ProsesPanenActivity.class);
                        intent.putExtra("barcode", result.getContents());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getBaseContext(), "Data pemanen dengan barcode ini tidak ditemukan pada afdeling yang dikelola", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
