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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    Button btnSimpanPanen, btnCetakPanen;
    List<ListViewAdapterPanen> panens;

    EditText txtTph;
    EditText txtJlhJanjang;
    EditText txtBrondolan;
    ListView lvwPanenPerOrang;

    String barcode;
    Pemanen ygPanen;
    String tglSekarang;

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

    String yangMauDiCetak;
    /* End of kebutuhan bluetooth */

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

        btnCetakPanen = findViewById(R.id.btnCetakPanen);
        btnCetakPanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String jamSekarang = new java.text.SimpleDateFormat(
                        "HH:mm:ss").format(calendar.getTime());
                String waktuPanen = tglSekarang + " " + jamSekarang;

                // Mencari rincian panennya
                String rincian_panennya = "";
                for (ListViewAdapterPanen panen : panens) {
                    rincian_panennya += "Blok : " + panen.getBlok() + "\n";
                    rincian_panennya += "Jlh Janjang : " + panen.getJanjang() + "\n";
                    rincian_panennya += "Jlh Brondolan :" + panen.getBrondolan() + "\n";
                    rincian_panennya += "\n";
                }

                yangMauDiCetak = "\n\nPTPN II\nKebun " + dataKebun.getNamaKebun() + " - " + dataKebun.getNamaAfdeling() + "\n" +
                        "- TANDA TERIMA PANEN TBS -\n" +
                        "======================\n" +
                        "Tgl\t\t: " + AppCommon.ubahFormatTanggal(waktuPanen, FORMAT_TANGGAL.INDONESIA_HANYA_TANGGAL) + "\n" +
                        "Pemanen\t\t: " + ygPanen.getNamaPemanen() + "\n" +
                        "KCS\t\t: " + keraniKcs.getNamaLengkap() + "\n\n" +
                        rincian_panennya +
                        "HARAP SIMPAN STRUK INI SEBAGAI BUKTI SERAH TERIMA PANEN\n" +
                        "======================\n";
                Log.d("cetakPanen", yangMauDiCetak);

                // Finding bluetooth device
                findBluetoothDevice();
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
            outputStream.write(yangMauDiCetak.getBytes());
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
