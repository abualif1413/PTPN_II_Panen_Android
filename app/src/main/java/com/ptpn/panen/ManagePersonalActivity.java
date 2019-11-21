package com.ptpn.panen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.AppPreferenceConstant;
import com.ptpn.panen.entity.JsonKeraniKcs;
import com.ptpn.panen.entity.JsonMandor;
import com.ptpn.panen.entity.JsonPemanen;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagePersonalActivity extends AppCompatActivity {

    TextView txtKeteranganAfdeling;
    TextView txtInfoPekerja;
    Button btnSimpanPembaharuan;
    ProgressBar progressBar;

    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling kebunAfdeling;
    int progressProcess;

    final int MAX_PROGRESS_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_personal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kelola Pekerja");
        setSupportActionBar(toolbar);

        txtKeteranganAfdeling = findViewById(R.id.managePersonal_txtKeteranganAfdeling);
        txtInfoPekerja = findViewById(R.id.managePersonal_txtInfoPekerja);
        btnSimpanPembaharuan = findViewById(R.id.managePersonal_btnSimpanPembaharuan);
        progressBar = findViewById(R.id.progressBarPekerja);

        sqLiteHandler = new SQLiteHandler(this);
        kebunAfdeling = sqLiteHandler.getAfdelingPreference();
        txtKeteranganAfdeling.setText("Saat ini sedang mengelola\nkebun " + kebunAfdeling.getNamaKebun().toUpperCase() + " - " + kebunAfdeling.getNamaAfdeling().toUpperCase() + ".\n" +
                "Klik tombol refresh di sudut kanan atas untuk sinkronisasi data pekerja");

        tulisanJumlahPekerja();

        btnSimpanPembaharuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHandler.setAppPreference(AppPreferenceConstant.SIMPAN_PEMBAHARUAN, "1");
                Toast.makeText(getApplicationContext(), "Pembaharuan data telah disimpan. Silahkan login kembali", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ManagePersonalActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void tulisanJumlahPekerja(){
        int jlhPemanen = sqLiteHandler.getPemanenList(0).size();
        int jlhMandor = sqLiteHandler.getMandorList(0).size();
        int jlhKeraniKcs = sqLiteHandler.getKeraniKcsList(0).size();


        if(jlhPemanen == 0 || jlhMandor == 0 || jlhKeraniKcs == 0) {
            txtInfoPekerja.setText("Tidak ada data pekerja ditemukan. Silahkan sinkronisasi data untuk memasukkan data pekerja kedalam aplikasi");
            txtInfoPekerja.setTextColor(getResources().getColor(R.color.colorError));
            btnSimpanPembaharuan.setEnabled(false);
            btnSimpanPembaharuan.setBackgroundColor(getResources().getColor(R.color.colorDisabled));

        } else {
            txtInfoPekerja.setText("Data pekerja ditemukan. Tetapi jika perlu memperbaharui data pekerja, silahkan sinkronisasi data untuk memasukkan data pekerja kedalam aplikasi");
            //txtInfoPekerja.setText("Pemanen = " + jlhPemanen + "\nMandor = " + jlhMandor + "\nKerani KCS = " + jlhKeraniKcs);
            txtInfoPekerja.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btnSimpanPembaharuan.setEnabled(true);
            btnSimpanPembaharuan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_001, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item001Refresh :
                progressProcess = 0;
                progressBar.setVisibility(View.VISIBLE);

                // Proses data mandor
                RetrofitApiInterface apiMandor = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonMandor> jsonMandorCall = apiMandor.getJsonMandor();
                jsonMandorCall.enqueue(new Callback<JsonMandor>() {
                    @Override
                    public void onResponse(Call<JsonMandor> call, Response<JsonMandor> response) {
                        JsonMandor res = response.body();
                        sqLiteHandler.truncateMandor();
                        for(Mandor mndr : res.getData()){
                            sqLiteHandler.insertMandor(mndr);
                        }
                        progressProcess++;
                        if(progressProcess == MAX_PROGRESS_COUNT){
                            progressBar.setVisibility(View.INVISIBLE);
                            tulisanJumlahPekerja();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonMandor> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API : " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // Proses data pemanen
                RetrofitApiInterface apiPemanen = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonPemanen> jsonPemanenCall = apiPemanen.getJsonPemanen();
                jsonPemanenCall.enqueue(new Callback<JsonPemanen>() {
                    @Override
                    public void onResponse(Call<JsonPemanen> call, Response<JsonPemanen> response) {
                        JsonPemanen res = response.body();
                        sqLiteHandler.truncatePemanen();
                        for(Pemanen pmn : res.getData()){
                            sqLiteHandler.insertPemanen(pmn);
                        }
                        progressProcess++;
                        if(progressProcess == MAX_PROGRESS_COUNT){
                            progressBar.setVisibility(View.INVISIBLE);
                            tulisanJumlahPekerja();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonPemanen> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API : " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // Proses data KCS
                RetrofitApiInterface apiKeraniKcs = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonKeraniKcs> jsonKeraniKcsCall = apiKeraniKcs.getJsonKeraniKcs();
                jsonKeraniKcsCall.enqueue(new Callback<JsonKeraniKcs>() {
                    @Override
                    public void onResponse(Call<JsonKeraniKcs> call, Response<JsonKeraniKcs> response) {
                        JsonKeraniKcs res = response.body();
                        sqLiteHandler.truncateKeraniKcs();
                        for(KeraniKcs keraniKcs : res.getData()){
                            sqLiteHandler.insertKeraniKcs(keraniKcs);
                        }
                        progressProcess++;
                        if(progressProcess == MAX_PROGRESS_COUNT){
                            progressBar.setVisibility(View.INVISIBLE);
                            tulisanJumlahPekerja();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonKeraniKcs> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API : " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                break;
        }
        return true;
    }
}
