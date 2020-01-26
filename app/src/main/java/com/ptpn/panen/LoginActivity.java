package com.ptpn.panen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.AppPreferenceConstant;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.handler.SQLiteHandler;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView infoAfdeling;
    EditText username, password;

    SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        ListViewAdapterKebunAfdeling dataKebun = sqLiteHandler.getAfdelingPreference();
        if(dataKebun.getNamaKebun() == null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        infoAfdeling = findViewById(R.id.txtInfoAfdeling);

        infoAfdeling.setText("Kebun " + dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling());



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hasilLogin = sqLiteHandler.loginAplikasi(username.getText().toString(), password.getText().toString());
                if(hasilLogin.get("hasil").toString() == "0")
                    Toast.makeText(getApplicationContext(), hasilLogin.get("hasil").toString() + " " + hasilLogin.get("pesan").toString(), Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getApplicationContext(), sqLiteHandler.getAppPreference(AppPreferenceConstant.ID_USER_LOGIN) + " " + sqLiteHandler.getAppPreference(AppPreferenceConstant.TIPE_USER_LOGIN), Toast.LENGTH_LONG).show();

                    String jenis_login = sqLiteHandler.getAppPreference(AppPreferenceConstant.TIPE_USER_LOGIN);
                    if(jenis_login.equalsIgnoreCase("mandor")) {
                        Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(LoginActivity.this, KehadiranActivity.class);
                        Intent intent = new Intent(LoginActivity.this, MandorPanel.class);
                        startActivity(intent);
                    } else if(jenis_login.equalsIgnoreCase("kcs")) {
                        Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, KcsPanel.class);
                        startActivity(intent);
                    }
                }
                //Toast.makeText(getApplicationContext(), "Wayoo", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_002, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item002KelolaAfdeling :
                Intent intentAfdeling = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentAfdeling);
                break;
            case R.id.item002KelolaPekerja :
                Intent intentPekerja = new Intent(LoginActivity.this, ManagePersonalActivity.class);
                startActivity(intentPekerja);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
}
