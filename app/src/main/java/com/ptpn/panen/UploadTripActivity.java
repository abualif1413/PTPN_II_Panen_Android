package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ptpn.panen.entity.JsonPostUmum;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Trip;
import com.ptpn.panen.entity.Trip020;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.Calendar;
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
    List<Trip020> trip020List;
    Button btnProses;
    ProgressBar progressBarProses;
    DatePickerDialog datePickerDialog;
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
        srcTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        srcTanggal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String tglCari = srcTanggal.getQuery().toString();
                srcTanggal.clearFocus();
                trip020List = sqLiteHandler.getTrip020Table(tglCari);
                if(trip020List.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Tidak ada data trip pada tanggal yang dicari", Toast.LENGTH_SHORT).show();
                } else {
                    counter = 0;
                    txtJumlahData.setText("Data ditemukan / diupload : " + trip020List.size() + " / " + counter);
                    txtJumlahData.setVisibility(View.VISIBLE);
                    btnProses.setVisibility(View.VISIBLE);

                    Gson gson = new Gson();
                    String datanya = gson.toJson(trip020List);

                    Log.d("Trip020_data", datanya);
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
                Gson gson = new Gson();
                progressBarProses.setVisibility(View.VISIBLE);
                for (final Trip020 trip : trip020List) {
                    String data = gson.toJson(trip);
                    RetrofitApiInterface retrofitApiInt = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                    Call<JsonPostUmum> jsonPostUmumCall = retrofitApiInt.getPostTrip020(data);
                    jsonPostUmumCall.enqueue(new Callback<JsonPostUmum>() {
                        @Override
                        public void onResponse(Call<JsonPostUmum> call, Response<JsonPostUmum> response) {
                            JsonPostUmum res = response.body();
                            counter++;
                            sqLiteHandler.pushStatusUploadTrip(trip.getSptbs() + "", trip.getTanggal(), "Telah upload");
                            txtJumlahData.setText("Data ditemukan / diupload : " + trip020List.size() + " / " + counter);
                            if(counter == trip020List.size()) {
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

    private void showCalendar() {
        //Toast.makeText(getApplicationContext(), "Kalender keluar", Toast.LENGTH_SHORT).show();
        Calendar newCalendar = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                srcTanggal.setQuery(new java.text.SimpleDateFormat(
                        "yyyy-MM-dd").format(newDate.getTime()), true);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(UploadTripActivity.this, KcsPanel.class);
        startActivity(intent);
    }
}
