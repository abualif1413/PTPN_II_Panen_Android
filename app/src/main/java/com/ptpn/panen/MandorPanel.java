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

import com.ptpn.panen.entity.AppPreferenceConstant;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MandorPanel extends AppCompatActivity {

    ListView lvwMenuMandor;
    TextView txtInfoMandor;
    Mandor mandor;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandor_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mandor");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        txtInfoMandor = findViewById(R.id.txtInfoMandor);
        mandor = sqLiteHandler.getDataMandorAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoMandor.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Mandor : " + mandor.getNamaLengkap() + "\n" + "Email : " + mandor.getEmail() + "\n" + "Pilih menu dibawah untuk pengelolaan lebih lanjut");

        lvwMenuMandor = findViewById(R.id.lvwMenuMandor);
        List<HashMap<String, String>> menuMandor = new ArrayList<HashMap<String, String>>();
        // Menu Absensi
        HashMap<String, String> menuAbsensi = new HashMap<String, String>();
        menuAbsensi.put("menu", "Absensi");
        menuAbsensi.put("keterangan", "Menu absensi adalah menu dimana mandor mengelola data kehadiran dan ijin dari setiap pemanen pada hari ini");

        HashMap<String, String> menuUploadAbsensi = new HashMap<String, String>();
        menuUploadAbsensi.put("menu", "Upload Absensi");
        menuUploadAbsensi.put("keterangan", "Menu upload absensi adalah menu dimana mandor meng-upload data absensi yang telah dikerjakan melalui apps pada HP ke database online");

        menuMandor.add(menuAbsensi);
        menuMandor.add(menuUploadAbsensi);

        SimpleAdapter adapter = new SimpleAdapter(this, menuMandor, R.layout.list_item_menu_kcs, new String[] {"menu", "keterangan"}, new int[] {R.id.listItemMenuKcsMenu, R.id.listItemMenuKcsKeterangan});
        lvwMenuMandor.setAdapter(adapter);

        lvwMenuMandor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        // Dia milih absensi
                        Intent intentAbsensi = new Intent(MandorPanel.this, KehadiranActivity.class);
                        startActivity(intentAbsensi);
                        break;
                    case 1:
                        // Dia milih upload
                        Intent intentUpload = new Intent(MandorPanel.this, UploadKehadiranActivity.class);
                        startActivity(intentUpload);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_004, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item004Logout :
                sqLiteHandler.setAppPreference(AppPreferenceConstant.ID_USER_LOGIN, "0");
                sqLiteHandler.setAppPreference(AppPreferenceConstant.TIPE_USER_LOGIN, "");
                Intent intent = new Intent(MandorPanel.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "Untuk logout, silahkan klik tombol logout pada sudut kanan atas", Toast.LENGTH_LONG).show();
    }
}
