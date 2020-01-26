package com.ptpn.panen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptpn.panen.entity.AlatPanen;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.handler.SQLiteHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EditPanenActivity extends AppCompatActivity {

    String id_panen;
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
    Button btnCetakPanen;

    EditText txtTph;
    EditText txtJlhJanjang;
    EditText txtBrondolan;

    String barcode;
    Panen dataPanen;
    Pemanen ygPanen;
    String tglSekarang;

    String namaBlok, namaAlatPanen;

    /* Kebutuhan bluetooth */
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    /* End of kebutuhan bluetooth */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_panen);

        id_panen = getIntent().getStringExtra("id_panen");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Panen");
        setSupportActionBar(toolbar);

        sqLiteHandler = new SQLiteHandler(this);

        Calendar calendar = Calendar.getInstance();
        tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        barcode = getIntent().getStringExtra("barcode");
        dataPanen = sqLiteHandler.getPanen(id_panen);
        ygPanen = sqLiteHandler.getPemanen(dataPanen.getId_pemanen());

        txtInfoKcs = findViewById(R.id.txtInfoKcs);
        keraniKcs = sqLiteHandler.getDataKeraniKcsAsPreference();
        dataKebun = sqLiteHandler.getAfdelingPreference();
        txtInfoKcs.setText(dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +"Kerani KCS : " + keraniKcs.getNamaLengkap() + "\n" + "Email : " + keraniKcs.getEmail() + "\n" +
                "Nama Pemanen : " + ygPanen.getNamaPemanen() + " (" + ygPanen.getBarcode() + ")");

        txtTph = findViewById(R.id.txtTph);
        txtJlhJanjang = findViewById(R.id.txtJlhJanjang);
        txtBrondolan = findViewById(R.id.txtJlhBrondolan);

        txtTph.setText(dataPanen.getTph() + "");
        txtJlhJanjang.setText(dataPanen.getJmlh_panen() + "");
        txtBrondolan.setText(dataPanen.getJmlh_brondolan() + "");

        blokList = sqLiteHandler.getListBlok();
        alatPanenList = sqLiteHandler.getListAlatPanen();

        stringBlokList = new ArrayList<String>();
        int blokSelectedId = -1;
        int iBlok = 0;
        stringBlokList.add("- Pilih Blok  -");
        for (Blok blk : blokList) {
            stringBlokList.add(blk.getBlok() + " - tahun tanam (" + blk.getTahunTanam() + ")");
            if((dataPanen.getBlok() + "").equals(blk.getId())) {
                blokSelectedId = iBlok;
                namaBlok = blk.getBlok() + " Thn Tnm : " + blk.getTahunTanam();
            }
            iBlok++;
        }

        stringAlatPanenList = new ArrayList<String>();
        int alatPanenSelectedId = -1;
        int iAlatPanen = 0;
        stringAlatPanenList.add("- Pilih Alat Panen -");
        for(AlatPanen alt : alatPanenList) {
            stringAlatPanenList.add(alt.getNamaAlat());
            if((dataPanen.getId_alat() + "").equals(alt.getId())) {
                alatPanenSelectedId = iAlatPanen;
                namaAlatPanen = alt.getNamaAlat();
            }
            iAlatPanen++;
        }

        spinnerBlok = findViewById(R.id.listBlok);
        ArrayAdapter<String> dataAdapterBlok = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringBlokList);
        dataAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlok.setAdapter(dataAdapterBlok);
        spinnerBlok.setSelection(blokSelectedId + 1);

        spinnerAlatPanen = findViewById(R.id.listAlatPanen);
        ArrayAdapter<String> dataAdapterAlatPanen = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringAlatPanenList);
        dataAdapterAlatPanen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlatPanen.setAdapter(dataAdapterAlatPanen);
        spinnerAlatPanen.setSelection(alatPanenSelectedId + 1);

        btnCetakPanen = findViewById(R.id.btnCetakPanen);
        btnCetakPanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finding bluetooth device
                findBluetoothDevice();
                //Toast.makeText(getApplicationContext(), "mencari bluetooth", Toast.LENGTH_SHORT).show();
                try {
                    openBluetoothPrinter();
                    printData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void findBluetoothDevice() {
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getApplicationContext(), "Hidupkan perangkat bluetooth", Toast.LENGTH_SHORT).show();
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            } else {
                Toast.makeText(getApplicationContext(), "Hidupkan perangkat bluetooth dan sambungkan dengan printer", Toast.LENGTH_SHORT).show();
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){
                    Log.w("PanenBT", "Name : " + pairedDev.getName());
                    if(pairedDev.getName().equals("BlueTooth Printer")) {
                        bluetoothDevice = pairedDev;
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    void openBluetoothPrinter() throws IOException {
        try{

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){

        }
    }

    void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.w("PanenBT", "Listen Data : " + data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    void printData() throws  IOException{
        try{
            Calendar calendar = Calendar.getInstance();
            String jamSekarang = new java.text.SimpleDateFormat(
                    "HH:mm:ss").format(calendar.getTime());
            String waktuPanen = dataPanen.getTanggal() + " " + jamSekarang;
            String msg = "\n\n" + dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +
                    "- TANDA TERIMA PANEN TBS -\n" +
                    "======================\n" +
                    "Tgl\t\t: " + AppCommon.ubahFormatTanggal(waktuPanen, FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL) + "\n" +
                    "Pemanen\t\t: " + ygPanen.getNamaPemanen() + "\n" +
                    "KCS\t\t: " + keraniKcs.getNamaLengkap() + "\n" +
                    "Jjg\t\t: " + dataPanen.getJmlh_panen() + "\n" +
                    "Brd\t\t: " + dataPanen.getJmlh_brondolan() + "\n" +
                    "Blok\t\t: " + namaBlok + "\n\n" +
                    "HARAP SIMPAN STRUK INI SEBAGAI BUKTI SERAH TERIMA PANEN\n" +
                    "======================\n";

            msg+="\n\n";
            outputStream.write(msg.getBytes());
            Log.w("PanenBT", "Sedang memprint...");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Disconnect Printer //
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            Log.w("PanenBT", "Printer ditutup");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            disconnectBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(EditPanenActivity.this, PanenActivity.class);
        startActivity(intent);
    }
}
