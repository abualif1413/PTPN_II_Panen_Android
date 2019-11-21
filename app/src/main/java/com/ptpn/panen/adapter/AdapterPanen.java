package com.ptpn.panen.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ptpn.panen.R;
import com.ptpn.panen.entity.ListViewAdapterKehadiranPekerja;
import com.ptpn.panen.entity.ListViewAdapterPanen;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterPanen implements ListAdapter {
    private List<ListViewAdapterPanen> panens;
    private Context context;

    public AdapterPanen(Context context, List<ListViewAdapterPanen> panens) {
        this.panens = panens;
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
        return this.panens.size();
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
        ListViewAdapterPanen adapterPanen = this.panens.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_panen, null);

            TextView txtNamaPekerja = convertView.findViewById(R.id.panen_txtNamaPekerja);
            TextView txtTph = convertView.findViewById(R.id.panen_txtTph);
            TextView txtBlok = convertView.findViewById(R.id.panen_txtBlok);
            TextView txtJlhJanjang = convertView.findViewById(R.id.panen_txtJlhJanjang);
            TextView txtBrondolan = convertView.findViewById(R.id.panen_txtBrondolan);
            TextView txtAlat = convertView.findViewById(R.id.panen_txtAlat);
            TextView txtStatusUpload = convertView.findViewById(R.id.panen_txtStatusUpload);

            txtStatusUpload.setText(adapterPanen.getStatusUpload());
            if(adapterPanen.getId_panen().equalsIgnoreCase("0")) {
                txtNamaPekerja.setText(adapterPanen.getNama_pemanen());
                txtTph.setText("--");
                txtBlok.setText("--");
                txtJlhJanjang.setText("--");
                txtBrondolan.setText("--");
                txtAlat.setText("--");

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtTph.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtBlok.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtJlhJanjang.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtBrondolan.setTextColor(convertView.getResources().getColor(R.color.colorError));
                txtAlat.setTextColor(convertView.getResources().getColor(R.color.colorError));
            } else {
                txtNamaPekerja.setText(adapterPanen.getNama_pemanen());
                txtTph.setText(adapterPanen.getTph());
                txtBlok.setText(adapterPanen.getBlok());
                txtJlhJanjang.setText(adapterPanen.getJanjang() + " Jjg");
                txtBrondolan.setText(adapterPanen.getBrondolan() + " Kg");
                txtAlat.setText(adapterPanen.getId_alat());

                txtNamaPekerja.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtTph.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtBlok.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtJlhJanjang.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtBrondolan.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
                txtAlat.setTextColor(convertView.getResources().getColor(R.color.colorHadirPekerja));
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
        return this.panens.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
