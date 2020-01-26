package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.ptpn.panen.entity.AlatPanen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.ListViewAdapterPanen;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProsesPanen020Activity extends AppCompatActivity {

    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    List<Blok> blokList;
    List<AlatPanen> alatPanenList;
    List<String> stringBlokList;
    List<String> stringAlatPanenList;
    Spinner spinnerBlok;
    Spinner spinnerAlatPanen;
    Button btnSimpanPanen;
    List<ListViewAdapterPanen> panens;

    EditText txtTph;
    EditText txtJlhJanjang;
    EditText txtBrondolan;
    ListView lvwPanenPerOrang;

    String barcode;
    Pemanen ygPanen;
    String tglSekarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_panen020);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Proses Panen");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        barcode = getIntent().getStringExtra("barcode");
        ygPanen = sqLiteHandler.getPemanenViaQRCode(barcode);

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" +
                "Nama Pemanen : " + ygPanen.getNamaPemanen() + " (" + ygPanen.getBarcode() + ")");

        txtTph = findViewById(R.id.txtTph);
        txtJlhJanjang = findViewById(R.id.txtJlhJanjang);
        txtBrondolan = findViewById(R.id.txtJlhBrondolan);

        blokList = sqLiteHandler.getListBlok();
        alatPanenList = sqLiteHandler.getListAlatPanen();

        stringBlokList = new ArrayList<String>();
        stringBlokList.add("- Pilih Blok  -");
        for (Blok blk : blokList) {
            stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
        }

        stringAlatPanenList = new ArrayList<String>();
        stringAlatPanenList.add("- Pilih Alat Panen -");
        for(AlatPanen alt : alatPanenList) {
            stringAlatPanenList.add(alt.getNamaAlat());
        }

        spinnerBlok = findViewById(R.id.listBlok);
        ArrayAdapter<String> dataAdapterBlok = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringBlokList);
        dataAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlok.setAdapter(dataAdapterBlok);

        spinnerAlatPanen = findViewById(R.id.listAlatPanen);
        ArrayAdapter<String> dataAdapterAlatPanen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringAlatPanenList);
        dataAdapterAlatPanen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlatPanen.setAdapter(dataAdapterAlatPanen);

        btnSimpanPanen = findViewById(R.id.btnSimpanPanen);
        btnSimpanPanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int positionBlok = spinnerBlok.getSelectedItemPosition();
                int positionAlat = spinnerAlatPanen.getSelectedItemPosition();

                if(txtJlhJanjang.getText().toString().equals("") || txtBrondolan.getText().toString().equals("") ||
                        txtTph.getText().toString().equals("") || positionBlok == 0 || positionAlat == 0) {
                    Toast.makeText(getApplicationContext(), "harap isikan semua data panen", Toast.LENGTH_LONG).show();
                } else {
                    Panen panen = new Panen();

                    panen.setId_kerani_askep(Integer.parseInt(keraniKcs.getIdKeraniAskep()));
                    panen.setId_kerani_kcs(keraniKcs.getId());
                    panen.setId_kebun(dataKebun.getIdKebun());

                    panen.setId_afdeling(dataKebun.getId());
                    panen.setId_pemanen(Integer.parseInt(ygPanen.getId()));
                    panen.setTph(Integer.parseInt(txtTph.getText().toString()));

                    panen.setBlok(Integer.parseInt(blokList.get(positionBlok - 1).getId()));
                    panen.setJmlh_panen(txtJlhJanjang.getText().toString());
                    panen.setJmlh_brondolan(txtBrondolan.getText().toString());

                    panen.setId_alat(Integer.parseInt(alatPanenList.get(positionAlat - 1).getId()));
                    panen.setTanggal(tglSekarang);
                    panen.setStatus("N");

                    panen.setApprove("N");
                    panen.setKode("");
                    panen.setCreat_att("");

                    sqLiteHandler.insertPanen(panen);
                    loadPanenPerOrang();
                    clearInput();
                }
            }
        });

        lvwPanenPerOrang = findViewById(R.id.lvwPanenPerOrang);
        loadPanenPerOrang();
        lvwPanenPerOrang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int idPanen = Integer.parseInt(panens.get(position).getId_panen());
                sqLiteHandler.deletePanen(idPanen);
                loadPanenPerOrang();

                return true;
            }
        });
    }

    private void clearInput() {
        txtJlhJanjang.setText("");
        txtBrondolan.setText("");
        txtTph.setText("");
        spinnerBlok.setSelection(0);
        spinnerAlatPanen.setSelection(0);
    }

    private void loadPanenPerOrang() {
        panens = sqLiteHandler.getDataPanenHarianPerOrang(tglSekarang, Integer.parseInt(ygPanen.getId()));
        Gson gson = new Gson();
        String uji = gson.toJson(panens);
        Log.d("panenPerOrang", uji);

        List<HashMap<String, String>> kembali = new ArrayList<>();
        for (ListViewAdapterPanen panen : panens) {
            HashMap<String, String> mapTemp = new HashMap<>();
            mapTemp.put("main", "Blok : " + panen.getBlok());
            mapTemp.put("sub", "Jlh Janjang : " + panen.getJanjang() + "\n" + "Jlh Brondolan : " + panen.getBrondolan());
            kembali.add(mapTemp);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, kembali, R.layout.list_item_001, new String[] {"main", "sub"}, new int[] {R.id.listItem001MainText, R.id.listItem001SubText});
        lvwPanenPerOrang.setAdapter(adapter);

        ListAdapter listAdapter = lvwPanenPerOrang.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvwPanenPerOrang);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvwPanenPerOrang.getLayoutParams();
        params.height = totalHeight + (lvwPanenPerOrang.getDividerHeight() * (listAdapter.getCount() - 1));
        lvwPanenPerOrang.setLayoutParams(params);
        lvwPanenPerOrang.requestLayout();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProsesPanen020Activity.this, PanenActivity.class);
        startActivity(intent);
    }
}
