package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ptpn.panen.entity.Absen;
import com.ptpn.panen.entity.JsonPostUmum;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPanenActivity extends AppCompatActivity {

    TextView txtInfoMandor, txtJumlahData;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    SearchView srcTanggal;
    List<Panen> panenList;
    Button btnProses;
    ProgressBar progressBarProses;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_panen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upload Data Panen");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        txtInfoMandor = findViewById(R.id.txtInfoMandor);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoMandor.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" + "Pilih menu dibawah untuk pengelolaan lebih lanjut");

        txtJumlahData = findViewById(R.id.txtJumlahData);
        btnProses = findViewById(R.id.btnProses);
        progressBarProses = findViewById(R.id.progressBarProses);

        srcTanggal = findViewById(R.id.srcTanggal);
        srcTanggal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String tglCari = srcTanggal.getQuery().toString();
                srcTanggal.clearFocus();
                panenList = sqLiteHandler.getPanenTable(tglCari);
                if(panenList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Tidak ada data panen pada tanggal yang dicari", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 0;
                    txtJumlahData.setText("Data ditemukan / diupload : " + panenList.size() + " / " + counter);
                    txtJumlahData.setVisibility(View.VISIBLE);
                    btnProses.setVisibility(View.VISIBLE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtJumlahData.setVisibility(View.INVISIBLE);
                btnProses.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarProses.setVisibility(View.VISIBLE);
                for (final Panen panen : panenList) {
                    KeraniKcs kkcs = sqLiteHandler.getKeraniKcs(Integer.parseInt(panen.getId_kerani_kcs()));
                    RetrofitApiInterface retrofitApiInterfaceAlatPanen = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                    Call<JsonPostUmum> jsonPostUmumCall = retrofitApiInterfaceAlatPanen.getPostPanen(
                            panen.getId_kerani_askep() + "",
                            kkcs.getToken() + "",
                            panen.getId_kebun() + "",
                            panen.getId_afdeling() + "",
                            panen.getId_pemanen() + "",
                            panen.getTph() + "",
                            panen.getBlok() + "",
                            panen.getJmlh_panen() + "",
                            panen.getJmlh_brondolan() + "",
                            panen.getId_alat() + "",
                            panen.getTanggal() + "",
                            "N",
                            "N",
                            "N",
                            "ANDROID");
                    jsonPostUmumCall.enqueue(new Callback<JsonPostUmum>() {
                        @Override
                        public void onResponse(Call<JsonPostUmum> call, Response<JsonPostUmum> response) {
                            JsonPostUmum res = response.body();
                            counter++;
                            sqLiteHandler.pushStatusUploadPanen(panen.getId_pemanen() + "", panen.getTanggal(), "Telah upload");
                            txtJumlahData.setText("Data ditemukan / diupload : " + panenList.size() + " / " + counter);
                            if(counter == panenList.size()) {
                                progressBarProses.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Data panen telah selesai diupload", Toast.LENGTH_LONG).show();
                                counter = 0;
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonPostUmum> call, Throwable t) {
                            progressBarProses.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(UploadPanenActivity.this, KcsPanel.class);
        startActivity(intent);
    }
}