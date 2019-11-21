package com.ptpn.panen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ptpn.panen.entity.Afdeling;
import com.ptpn.panen.entity.AlatPanen;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.Distrik;
import com.ptpn.panen.entity.JsonAfdeling;
import com.ptpn.panen.entity.JsonAlatPanen;
import com.ptpn.panen.entity.JsonBlok;
import com.ptpn.panen.entity.JsonDistrik;
import com.ptpn.panen.entity.JsonKebun;
import com.ptpn.panen.entity.Kebun;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.handler.RetrofitApiInterface;
import com.ptpn.panen.handler.RetrofitHandler;
import com.ptpn.panen.handler.SQLiteHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView lvwKebunAfdeling;
    ProgressBar progressBar;

    List<ListViewAdapterKebunAfdeling> listViewAdapterKebunAfdelings;
    SQLiteHandler sqLiteHandler;
    int sudahBerapaProgress;

    final int TOTAL_PROGRESS_API = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kelola Afdeling");
        setSupportActionBar(toolbar);

        lvwKebunAfdeling = findViewById(R.id.lvwKebunAfdeling);
        progressBar = findViewById(R.id.progressBarKebunAfdeling);

        sqLiteHandler = new SQLiteHandler(this);

        loadDataKebunAfdeling();

        lvwKebunAfdeling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listViewAdapterKebunAfdelings != null) {
                    try {
                        sqLiteHandler.setAfdelingPreference(listViewAdapterKebunAfdelings.get(position).getId());
                        Intent intent = new Intent(MainActivity.this, ManagePersonalActivity.class);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Error : " + ex.getMessage(),Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Data belum selesai dimuat. Harap tunggu beberapa saat",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loadDataKebunAfdeling(){
        listViewAdapterKebunAfdelings = sqLiteHandler.getListViewAdapterKebunAfdeling();
        List<HashMap<String, String>> dataKebunAfdeling = new ArrayList<HashMap<String, String>>();
        for(ListViewAdapterKebunAfdeling lst : listViewAdapterKebunAfdelings){
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("main", lst.getNamaKebun() + " - " + lst.getNamaAfdeling() + " (" + lst.getJlhBlok() + " Blok)");
            temp.put("sub", "Distrik : " + lst.getNamaDistrik());
            dataKebunAfdeling.add(temp);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, dataKebunAfdeling, R.layout.list_item_001, new String[] {"main", "sub"}, new int[] {R.id.listItem001MainText, R.id.listItem001SubText});
        lvwKebunAfdeling.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_001, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item001Refresh :
                /* Jika dia klik refresh, maka dia refresh data kebun, afdeling, dan distrik dari API */
                progressBar.setVisibility(View.VISIBLE);
                sudahBerapaProgress = 0;
                lvwKebunAfdeling.setAdapter(null);

                // Sinkron kebun
                RetrofitApiInterface retrofitApiInterfaceKebun = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonKebun> jsonKebunCall = retrofitApiInterfaceKebun.getJsonKebun();
                jsonKebunCall.enqueue(new Callback<JsonKebun>() {
                    @Override
                    public void onResponse(Call<JsonKebun> call, Response<JsonKebun> response) {
                        JsonKebun res = response.body();
                        sqLiteHandler.truncateKebun();
                        for (Kebun lst: res.getData()) {
                            sqLiteHandler.insertKebun(lst);
                        }
                        sudahBerapaProgress++;
                        Toast.makeText(getApplicationContext(), "Sinkron data kebun selesai (" + sudahBerapaProgress + ")",Toast.LENGTH_LONG).show();

                        if(sudahBerapaProgress == TOTAL_PROGRESS_API) {
                            progressBar.setVisibility(View.INVISIBLE);
                            loadDataKebunAfdeling();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonKebun> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // Sinkron Afdeling
                RetrofitApiInterface retrofitApiInterfaceAfdeling = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonAfdeling> jsonAfdelingCall = retrofitApiInterfaceAfdeling.getJsonAfdeling();
                jsonAfdelingCall.enqueue(new Callback<JsonAfdeling>() {
                    @Override
                    public void onResponse(Call<JsonAfdeling> call, Response<JsonAfdeling> response) {
                        JsonAfdeling res = response.body();
                        sqLiteHandler.truncateAfdeling();
                        for (Afdeling afd : res.getData()) {
                            sqLiteHandler.insertAfdeling(afd);
                        }
                        sudahBerapaProgress++;
                        Toast.makeText(getApplicationContext(), "Sinkron data afdeling selesai (" + sudahBerapaProgress + ")",Toast.LENGTH_LONG).show();

                        if(sudahBerapaProgress == TOTAL_PROGRESS_API) {
                            progressBar.setVisibility(View.INVISIBLE);
                            loadDataKebunAfdeling();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonAfdeling> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // Sinkron Distrik
                RetrofitApiInterface retrofitApiInterfaceDistrik = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonDistrik> jsonDistrikCall = retrofitApiInterfaceDistrik.getJsonDistrik();
                jsonDistrikCall.enqueue(new Callback<JsonDistrik>() {
                    @Override
                    public void onResponse(Call<JsonDistrik> call, Response<JsonDistrik> response) {
                        JsonDistrik res = response.body();
                        sqLiteHandler.truncateDistrik();
                        for(Distrik dist : res.getData()) {
                            sqLiteHandler.insertDistrik(dist);
                        }
                        sudahBerapaProgress++;
                        Toast.makeText(getApplicationContext(), "Sinkron data distrik selesai (" + sudahBerapaProgress + ")",Toast.LENGTH_LONG).show();

                        if(sudahBerapaProgress == TOTAL_PROGRESS_API) {
                            progressBar.setVisibility(View.INVISIBLE);
                            loadDataKebunAfdeling();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonDistrik> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // Sinkron blok
                RetrofitApiInterface retrofitApiInterfaceBlok = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonBlok> jsonBlokCall = retrofitApiInterfaceBlok.getJsonBlok();
                jsonBlokCall.enqueue(new Callback<JsonBlok>() {
                    @Override
                    public void onResponse(Call<JsonBlok> call, Response<JsonBlok> response) {
                        JsonBlok res = response.body();
                        sqLiteHandler.truncateBlok();
                        for (Blok blk : res.getData()) {
                            sqLiteHandler.insertBlok(blk);
                        }

                        sudahBerapaProgress++;
                        Toast.makeText(getApplicationContext(), "Sinkron data blok selesai (" + sudahBerapaProgress + ")",Toast.LENGTH_LONG).show();

                        if(sudahBerapaProgress == TOTAL_PROGRESS_API) {
                            progressBar.setVisibility(View.INVISIBLE);
                            loadDataKebunAfdeling();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonBlok> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                // sinkron alat panen
                RetrofitApiInterface retrofitApiInterfaceAlatPanen = RetrofitHandler.getRetrofit().create(RetrofitApiInterface.class);
                Call<JsonAlatPanen> jsonAlatPanenCall = retrofitApiInterfaceBlok.getAlatPanen();
                jsonAlatPanenCall.enqueue(new Callback<JsonAlatPanen>() {
                    @Override
                    public void onResponse(Call<JsonAlatPanen> call, Response<JsonAlatPanen> response) {
                        JsonAlatPanen res = response.body();
                        sqLiteHandler.truncateAlatPanen();
                        for (AlatPanen ap : res.getData()) {
                            sqLiteHandler.insertAlatPanen(ap);
                        }

                        sudahBerapaProgress++;
                        Toast.makeText(getApplicationContext(), "Sinkron data alat panen selesai (" + sudahBerapaProgress + ")",Toast.LENGTH_LONG).show();

                        if(sudahBerapaProgress == TOTAL_PROGRESS_API) {
                            progressBar.setVisibility(View.INVISIBLE);
                            loadDataKebunAfdeling();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonAlatPanen> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Tidak bisa terhubung ke API " + t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
