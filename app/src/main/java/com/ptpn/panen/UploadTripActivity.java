package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.JsonPostUmum;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Trip;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadTripActivity extends AppCompatActivity {

    TextView txtInfoMandor, txtJumlahData;
    KeraniKcs keraniKcs;
    SQLiteHandler sqLiteHandler;
    ListViewAdapterKebunAfdeling dataKebun;
    SearchView srcTanggal;
    List<Trip> tripList;
    Button btnProses;
    ProgressBar progressBarProses;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_trip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upload Data Trip");
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
                tripList = sqLiteHandler.getTripTable(tglCari);
                if(tripList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Tidak ada data trip pada tanggal yang dicari", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 0;
                    txtJumlahData.setText("Data ditemukan / diupload : " + tripList.size() + " / " + counter);
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
                for (final Trip trip : tripList) {
                    /*
                    @Field("no_sptbs"),
                    @Field("id_kerani_askep"),
                    @Field("id_kerani_kcs"),
                    @Field("id_kebun"),
                    @Field("id_afdeling"),
                    @Field("id_blok_1"),
                    @Field("id_blok_2"),
                    @Field("id_blok_3"),
                    @Field("jumlah_janjang_1"),
                    @Field("jumlah_janjang_2"),
                    @Field("jumlah_janjang_3"),
                    @Field("jumlah_brondolan_1"),
                    @Field("jumlah_brondolan_2"),
                    @Field("jumlah_brondolan_3"),
                    @Field("nomor_polisi_trek"),
                    @Field("tanggal"),
                    @Field("status"),
                    @Field("device")
                    * */
                    KeraniKcs objKeraniKcs = sqLiteHandler.getKeraniKcs(Integer.parseInt(trip.getId_kerani_kcs()));
                    RetrofitApiInterface retrofitApiInterfaceAlatPanen = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                    Call<JsonPostUmum> jsonPostUmumCall = retrofitApiInterfaceAlatPanen.getPostTrip(
                            trip.getSptbs(), (objKeraniKcs.getIdKeraniAskep() + ""), (objKeraniKcs.getToken() + ""), (dataKebun.getIdKebun() + ""), (trip.getId_afdeling() + ""),
                            (trip.getId_blok_1() + ""), (trip.getId_blok_2() + ""), (trip.getId_blok_3() + ""),
                            (trip.getJumlah_janjang_1() + ""), (trip.getJumlah_janjang_2() + ""), (trip.getJumlah_janjang_3() + ""),
                            (trip.getJumlah_brondolan_1() + ""), (trip.getJumlah_brondolan_2() + ""), (trip.getJumlah_brondolan_3() + ""),
                            trip.getNomor_polisi_trek(), trip.getTanggal(), "N", "ANDROID"
                    );

                    final Trip tripToUpload = trip;
                    jsonPostUmumCall.enqueue(new Callback<JsonPostUmum>() {
                        @Override
                        public void onResponse(Call<JsonPostUmum> call, Response<JsonPostUmum> response) {
                            counter++;
                            txtJumlahData.setText("Data ditemukan / diupload : " + tripList.size() + " / " + counter);
                            sqLiteHandler.pushStatusUploadTrip(tripToUpload.getSptbs(), tripToUpload.getTanggal(), "Telah diupload");
                            if(counter == tripList.size()) {
                                progressBarProses.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Data trip telah selesai diupload", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(UploadTripActivity.this, KcsPanel.class);
        startActivity(intent);
    }
}
