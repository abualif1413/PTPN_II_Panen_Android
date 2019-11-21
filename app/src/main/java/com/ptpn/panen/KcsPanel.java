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
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KcsPanel extends AppCompatActivity {

    ListView lvwMenuKcs;
    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kcs_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kerani KCS");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" + "Pilih menu dibawah untuk pengelolaan lebih lanjut");

        lvwMenuKcs = findViewById(R.id.lvwMenuKcs);
        List<HashMap<String, String>> menuKcs = new ArrayList<HashMap<String, String>>();
        // Menu Panen
        HashMap<String, String> menuPanen = new HashMap<String, String>();
        menuPanen.put("menu", "Panen");
        menuPanen.put("keterangan", "Menu panen adalah menu dimana kerani KCS menginputkan data hasil panen dari semua pemanen pada hari ini");

        // Menu Trip
        HashMap<String, String> menuTrip = new HashMap<String, String>();
        menuTrip.put("menu", "Trip");
        menuTrip.put("keterangan", "Menu trip adalah menu dimana kerani KCS menginputkan data trip per masing-masing truk / mobil dalam pengangkutan hasil panen");

        // Menu Upload Panen
        HashMap<String, String> menuUploadPanen = new HashMap<String, String>();
        menuUploadPanen.put("menu", "Upload Panen");
        menuUploadPanen.put("keterangan", "Menu upload panen adalah menu dimana kerani KCS mengupload data panen yang telah dikerjakan melalui apps pada HP ke database online");

        // Menu Upload Trip
        HashMap<String, String> menuUploadTrip = new HashMap<String, String>();
        menuUploadTrip.put("menu", "Upload Trip");
        menuUploadTrip.put("keterangan", "Menu upload panen adalah menu dimana kerani KCS mengupload data trip yang telah dikerjakan melalui apps pada HP ke database online");

        menuKcs.add(menuPanen);
        menuKcs.add(menuTrip);
        menuKcs.add(menuUploadPanen);
        menuKcs.add(menuUploadTrip);

        SimpleAdapter adapter = new SimpleAdapter(this, menuKcs, R.layout.list_item_menu_kcs, new String[] {"menu", "keterangan"}, new int[] {R.id.listItemMenuKcsMenu, R.id.listItemMenuKcsKeterangan});
        lvwMenuKcs.setAdapter(adapter);

        lvwMenuKcs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        // Dia milih panen
                        Intent intentPanen = new Intent(KcsPanel.this, PanenActivity.class);
                        startActivity(intentPanen);
                        break;
                    case 1:
                        // Dia milih trip
                        Intent intentTrip = new Intent(KcsPanel.this, TripActivity.class);
                        startActivity(intentTrip);
                        break;
                    case 2:
                        // Dia milih upload panen
                        Intent intentUploadPanen = new Intent(KcsPanel.this, UploadPanenActivity.class);
                        startActivity(intentUploadPanen);
                        break;
                    case 3 :
                        // Dia milih upload trip
                        Intent intentUploadTrip = new Intent(KcsPanel.this, UploadTripActivity.class);
                        startActivity(intentUploadTrip);
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
                Intent intent = new Intent(KcsPanel.this, LoginActivity.class);
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
