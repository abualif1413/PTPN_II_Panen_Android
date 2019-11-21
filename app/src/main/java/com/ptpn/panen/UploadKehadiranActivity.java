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

import com.ptpn.panen.entity.Absen;
import com.ptpn.panen.entity.JsonPostAbsen;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadKehadiranActivity extends AppCompatActivity {

    TextView txtInfoMandor, txtJumlahData;
    Mandor mandor;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    SearchView srcTanggal;
    List<Absen> absenList;
    Button btnProses;
    ProgressBar progressBarProses;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_kehadiran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upload Data Absensi");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        txtInfoMandor = findViewById(R.id.txtInfoMandor);
        mandor = sqLiteHandler.getDataMandorAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoMandor.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Mandor : " + mandor.getNamaLengkap() + "\n" + "Email : " + mandor.getEmail());

        txtJumlahData = findViewById(R.id.txtJumlahData);
        btnProses = findViewById(R.id.btnProses);
        progressBarProses = findViewById(R.id.progressBarProses);

        srcTanggal = findViewById(R.id.srcTanggal);
        srcTanggal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String tglCari = srcTanggal.getQuery().toString();
                srcTanggal.clearFocus();

                absenList = sqLiteHandler.getAbsenTable(tglCari);
                if(absenList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Tidak ada data absen pada tanggal yang dicari", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 0;
                    txtJumlahData.setText("Data ditemukan / diupload : " + absenList.size() + " / " + counter);
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
                for (Absen absen : absenList) {
                    RetrofitApiInterface retrofitApiInterfaceAlatPanen = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                    String kehadiran = "";
                    switch (absen.getKehadiran().toLowerCase()) {
                        case "h" :
                            kehadiran = "H";
                            break;
                        case "s" :
                            kehadiran = "S";
                            break;
                        case "c" :
                            kehadiran = "C";
                            break;

                            default:
                                kehadiran = "I";
                                break;
                    }
                    Call<JsonPostAbsen> jsonPostAbsenCall = retrofitApiInterfaceAlatPanen.getPostAbsen(
                            absen.getId_mandor(),
                            absen.getId_absen(),
                            kehadiran,
                            absen.getTanggal(),
                            absen.getJam(),
                            "ANDROID");
                    final Absen absenToPush = absen;
                    jsonPostAbsenCall.enqueue(new Callback<JsonPostAbsen>() {
                        @Override
                        public void onResponse(Call<JsonPostAbsen> call, Response<JsonPostAbsen> response) {
                            JsonPostAbsen res = response.body();
                            sqLiteHandler.pushStatusUploadAbsen(absenToPush.getId_absen(), absenToPush.getTanggal(), "Telah diupload");
                            counter++;
                            txtJumlahData.setText("Data ditemukan / diupload : " + absenList.size() + " / " + counter);
                            if(counter == absenList.size()) {
                                progressBarProses.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Data absen telah selesai diupload", Toast.LENGTH_LONG).show();
                                counter = 0;
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonPostAbsen> call, Throwable t) {
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
        Intent intent = new Intent(UploadKehadiranActivity.this, MandorPanel.class);
        startActivity(intent);
    }
}
