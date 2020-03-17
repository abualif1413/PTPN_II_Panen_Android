package com.ptpn.panen.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ptpn.panen.R;
import com.ptpn.panen.entity.AppCommon;
import com.ptpn.panen.entity.FORMAT_TANGGAL;
import com.ptpn.panen.entity.ListViewAdapterKehadiranPekerja;

import java.util.List;

public class AdapterKehadiranPekerja implements ListAdapter {
    private List<ListViewAdapterKehadiranPekerja> kehadiranPekerja;
    private Context context;

    public AdapterKehadiranPekerja(Context context, List<ListViewAdapterKehadiranPekerja> kehadiranPekerja) {
        this.kehadiranPekerja = kehadiranPekerja;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return this.kehadiranPekerja.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewAdapterKehadiranPekerja objKehadiranPekerja = kehadiranPekerja.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_kehadiran_pekerja_02, null);
            TextView txtNamaPekerja = convertView.findViewById(R.id.kehadiranPekerja_txtNamaPekerja);
            TextView txtJenisKehadiran = convertView.findViewById(R.id.kehadiranPekerja_txtJenisKehadiran);
            TextView txtWaktuKehadiran = convertView.findViewById(R.id.kehadiranPekerja_txtWaktuKehadiran);
            TextView txtKeterangan = convertView.findViewById(R.id.kehadiranPekerja_txtKeterangan);
            TextView txtStatusUpload = convertView.findViewById(R.id.kehadiranPekerja_txtStatusUpload);

            txtStatusUpload.setText(objKehadiranPekerja.getStatusUpload());
            if(objKehadiranPekerja.getStatusKehadiran().equalsIgnoreCase("H")) {
                txtNamaPekerja.setText(objKehadiranPekerja.getNamaPemanen());
                txtJenisKehadiran.setText("Hadir (Panen)");
                txtWaktuKehadiran.setText(AppCommon.ubahFormatTanggal(objKehadiranPekerja.getTanggal(), FORMAT_TANGGAL.INDONESIA_KOMPLIT_TANPA_DETIK));
                txtKeterangan.setText("Hadir (Panen)");

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtJenisKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtWaktuKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtKeterangan.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
            } else if(objKehadiranPekerja.getStatusKehadiran().equalsIgnoreCase("HT")) {
                txtNamaPekerja.setText(objKehadiranPekerja.getNamaPemanen());
                txtJenisKehadiran.setText("Hadir (Tunasan)");
                txtWaktuKehadiran.setText(AppCommon.ubahFormatTanggal(objKehadiranPekerja.getTanggal(), FORMAT_TANGGAL.INDONESIA_KOMPLIT_TANPA_DETIK));
                txtKeterangan.setText("Hadir (Tunasan)");

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtJenisKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtWaktuKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtKeterangan.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
            } else if(objKehadiranPekerja.getStatusKehadiran().equalsIgnoreCase("")) {
                txtNamaPekerja.setText(objKehadiranPekerja.getNamaPemanen());
                txtJenisKehadiran.setText("Alpa / mangkir");
                txtWaktuKehadiran.setText("---");
                txtKeterangan.setText("");

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtJenisKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtWaktuKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtKeterangan.setTextColor(convertView.getResources().getColor(R.color.colorError));
            } else {
                String statusKehadiran = objKehadiranPekerja.getStatusKehadiran();
                String[] listIjinSimple = convertView.getResources().getStringArray(R.array.jenis_ijin);
                String[] listIjinLengkap = convertView.getResources().getStringArray(R.array.jenis_ijin_lengkap);

                int index = 0;
                for (int i=0; i<listIjinSimple.length; i++) {
                    if(listIjinSimple[i].equalsIgnoreCase(statusKehadiran)) {
                        index = i;
                    }
                }

                String statusKehadiranLengkap = listIjinLengkap[index];

                txtNamaPekerja.setText(objKehadiranPekerja.getNamaPemanen());
                txtJenisKehadiran.setText(statusKehadiranLengkap);
                txtWaktuKehadiran.setText(AppCommon.ubahFormatTanggal(objKehadiranPekerja.getTanggal(), FORMAT_TANGGAL.INDONESIA_KOMPLIT_TANPA_DETIK));
                txtKeterangan.setText(objKehadiranPekerja.getKeterangan());

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorWarning));
                txtJenisKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorWarning));
                txtWaktuKehadiran.setTextColor(convertView.getResources().getColor(R.color.colorWarning));
                txtKeterangan.setTextColor(convertView.getResources().getColor(R.color.colorWarning));
            }
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return this.kehadiranPekerja.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
