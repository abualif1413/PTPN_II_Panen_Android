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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;

import com.google.gson.Gson;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.BlokAdaPanenBelumDiantar;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProsesTrip020Activity extends AppCompatActivity {

    TextView txtInfoKcs;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    List<BlokAdaPanenBelumDiantar> blokListTemp;
    List<Blok> blokList;
    List<String> stringBlokList;
    EditText txtJlhJanjang, txtRestanAwal, txtTglRestan, txtJlhBrondolan, txtNoSPTBS, txtNoPolisi;
    Spinner spinnerBlok;
    Button btnPush, btnSimpanSptbs;
    ListView lvwTripDetail;
    DatePickerDialog datePickerDialog;

    String tglSekarang;

    List<HashMap<String, String>> listDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_trip020);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Proses Trip 020");
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

        blokList = sqLiteHandler.getListBlok();
        Gson gson = new Gson();
        String jsonuji = gson.toJson(blokList);
        Log.d("BlokAdaPanen", jsonuji);

        stringBlokList = new ArrayList<String>();
        stringBlokList.add("- Pilih Blok  -");
        for (Blok blk : blokList) {
            stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
        }

        ArrayAdapter<String> dataAdapterBlok = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringBlokList);
        dataAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBlok = findViewById(R.id.listBlok);
        spinnerBlok.setAdapter(dataAdapterBlok);
        /*spinnerBlok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    int selectedIndex = position - 1;
                    int jmlhJanjang = (int)blokList.get(selectedIndex).getJmlh_janjang();
                    txtJlhJanjang.setText(jmlhJanjang + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtJlhJanjang.setText("");
            }
        });*/

        txtJlhJanjang = findViewById(R.id.txtJlhJanjang);
        txtRestanAwal = findViewById(R.id.txtRestanAwal);
        txtTglRestan = findViewById(R.id.txtTglRestan);
        txtJlhBrondolan = findViewById(R.id.txtJlhBrondolan);
        txtNoSPTBS = findViewById(R.id.txtNoSPTBS);
        txtNoPolisi = findViewById(R.id.txtNoPolisi);

        txtTglRestan.setFocusable(false);
        txtTglRestan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        txtTglRestan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtTglRestan.setText("");
                return true;
            }
        });

        btnPush = findViewById(R.id.btnPush);
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jlh_janjang = txtJlhJanjang.getText().toString() + "";
                String restan_awal = txtRestanAwal.getText().toString() + "";
                String tgl_restan = txtTglRestan.getText().toString() + "";
                int positionBlok = spinnerBlok.getSelectedItemPosition();
                String id_blok = (positionBlok == 0 ? "0" : blokList.get(positionBlok - 1).getId());



                if(jlh_janjang.equals("") || restan_awal.equals("") || id_blok.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Harap isi semua input. Jika tidak ada janjang hari ini atau restan, maka isi dengan 0", Toast.LENGTH_SHORT).show();
                } else {
                    if(!restan_awal.equals("0")) {
                        // Berarti dia ada restan
                        if(tgl_restan.equals("")) {
                            Toast.makeText(getApplicationContext(), "Tgl restan harus diisi jika ada buah restan yang diangkut", Toast.LENGTH_SHORT).show();
                        } else {
                            // Validasi supaya dia tidak bisa input janjang lebih besar dari yg dia panen
                            double dblJanjangInput = Double.parseDouble(jlh_janjang);
                            double dblSeharusnya = 1E30;

                            sqLiteHandler.insertTrip020Detail(0, Integer.parseInt(id_blok), Integer.parseInt(jlh_janjang), Integer.parseInt(restan_awal), txtTglRestan.getText().toString() + "");
                            loadDetail();
                            kosongDetail();
                            blokList = sqLiteHandler.getListBlok();
                            stringBlokList = new ArrayList<String>();
                            stringBlokList.add("- Pilih Blok  -");
                            for (Blok blk : blokList) {
                                stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
                            }
                        }
                    } else {
                        // Berarti dia tidak ada restan
                        // Validasi supaya dia tidak bisa input janjang lebih besar dari yg dia panen
                        double dblJanjangInput = Double.parseDouble(jlh_janjang);
                        double dblSeharusnya = 1E30;

                        sqLiteHandler.insertTrip020Detail(0, Integer.parseInt(id_blok), Integer.parseInt(jlh_janjang), Integer.parseInt(restan_awal), txtTglRestan.getText().toString() + "");
                        loadDetail();
                        kosongDetail();
                        blokList = sqLiteHandler.getListBlok();
                        stringBlokList = new ArrayList<String>();
                        stringBlokList.add("- Pilih Blok  -");
                        for (Blok blk : blokList) {
                            stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
                        }
                    }
                }
            }
        });

        btnSimpanSptbs = findViewById(R.id.btnSimpanSptbs);
        btnSimpanSptbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHandler.insertTrip020(txtNoSPTBS.getText().toString() + "", txtNoPolisi.getText().toString() + "", Integer.parseInt(txtJlhBrondolan.getText().toString()));
                Intent intent = new Intent(ProsesTrip020Activity.this, TripActivity.class);
                startActivity(intent);
            }
        });

        lvwTripDetail = findViewById(R.id.lvwTripDetail);
        lvwTripDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String id_detail = listDetail.get(position).get("id");
                sqLiteHandler.deleteTrip020Detail(Integer.parseInt(id_detail));
                loadDetail();
                blokList = sqLiteHandler.getListBlok();
                stringBlokList = new ArrayList<String>();
                stringBlokList.add("- Pilih Blok  -");
                for (Blok blk : blokList) {
                    stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
                }

                return true;
            }
        });
        loadDetail();
    }

    void kosongDetail() {
        spinnerBlok.setSelection(0);
        txtJlhJanjang.setText("");
        txtRestanAwal.setText("");
        txtTglRestan.setText("");
    }

    void loadDetail() {
        //Toast.makeText(getApplicationContext(), "List diload", Toast.LENGTH_SHORT).show();
        listDetail = sqLiteHandler.listTrip020Detail(0);
        SimpleAdapter adapter = new SimpleAdapter(this, listDetail, R.layout.list_item_001, new String[] {"main", "sub"}, new int[] {R.id.listItem001MainText, R.id.listItem001SubText});
        lvwTripDetail.setAdapter(adapter);

        ListAdapter listAdapter = lvwTripDetail.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvwTripDetail);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvwTripDetail.getLayoutParams();
        params.height = totalHeight + (lvwTripDetail.getDividerHeight() * (listAdapter.getCount() - 1));
        lvwTripDetail.setLayoutParams(params);
        lvwTripDetail.requestLayout();
    }

    private void showCalendar() {
        //Toast.makeText(getApplicationContext(), "Kalender keluar", Toast.LENGTH_SHORT).show();
        Calendar newCalendar = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                txtTglRestan.setText(new java.text.SimpleDateFormat(
                        "yyyy-MM-dd").format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void hideCalendar() {
        Toast.makeText(getApplicationContext(), "Kalender hilang", Toast.LENGTH_SHORT).show();
    }
}
