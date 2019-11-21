package com.ptpn.panen.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ptpn.panen.R;
import com.ptpn.panen.entity.ListViewAdapterPanen;
import com.ptpn.panen.entity.ListViewAdapterTrip;

import java.util.List;

public class AdapterTrip implements ListAdapter {
    private List<ListViewAdapterTrip> trips;
    private Context context;

    public AdapterTrip(Context context, List<ListViewAdapterTrip> trips) {
        this.trips = trips;
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
        return this.trips.size();
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
        ListViewAdapterTrip trip = this.trips.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_trip, null);

            TextView txtNoSptbs = convertView.findViewById(R.id.trip_txtNoSptbs);

            TextView txtBlok1 = convertView.findViewById(R.id.trip_txtBlok1);
            TextView txtJanjang1 = convertView.findViewById(R.id.trip_txtJanjang1);
            TextView txtBrondolan1 = convertView.findViewById(R.id.trip_txtBrondolan1);

            TextView txtBlok2 = convertView.findViewById(R.id.trip_txtBlok2);
            TextView txtJanjang2 = convertView.findViewById(R.id.trip_txtJanjang2);
            TextView txtBrondolan2 = convertView.findViewById(R.id.trip_txtBrondolan2);

            TextView txtBlok3 = convertView.findViewById(R.id.trip_txtBlok3);
            TextView txtJanjang3 = convertView.findViewById(R.id.trip_txtJanjang3);
            TextView txtBrondolan3 = convertView.findViewById(R.id.trip_txtBrondolan3);

            TextView txtStatusUpload = convertView.findViewById(R.id.trip_txtStatusUpload);

            txtNoSptbs.setText("SPTBS : " + trip.getSptbs() + " (" + trip.getNomor_polisi_trek() + ")");

            txtBlok1.setText(trip.getNama_blok_1() + " (TT : " + trip.getThn_tanam_1() + ")");
            txtJanjang1.setText(trip.getJumlah_janjang_1());
            txtBrondolan1.setText(trip.getJumlah_brondolan_1() + " Kg");

            txtBlok2.setText(trip.getNama_blok_2() + " (TT : " + trip.getThn_tanam_2() + ")");
            txtJanjang2.setText(trip.getJumlah_janjang_2());
            txtBrondolan2.setText(trip.getJumlah_brondolan_2() + " Kg");

            txtBlok3.setText(trip.getNama_blok_3() + " (TT : " + trip.getThn_tanam_3() + ")");
            txtJanjang3.setText(trip.getJumlah_janjang_3());
            txtBrondolan3.setText(trip.getJumlah_brondolan_3() + " Kg");

            txtStatusUpload.setText(trip.getStatusUpload());
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return this.trips.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
