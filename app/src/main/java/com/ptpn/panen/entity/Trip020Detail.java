package com.ptpn.panen.entity;

public class Trip020Detail {
    public int id;
    public int id_trip;
    public int id_blok;
    public double jumlah_janjang;
    public double jumlah_restan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_trip() {
        return id_trip;
    }

    public void setId_trip(int id_trip) {
        this.id_trip = id_trip;
    }

    public int getId_blok() {
        return id_blok;
    }

    public void setId_blok(int id_blok) {
        this.id_blok = id_blok;
    }

    public double getJumlah_janjang() {
        return jumlah_janjang;
    }

    public void setJumlah_janjang(double jumlah_janjang) {
        this.jumlah_janjang = jumlah_janjang;
    }

    public double getJumlah_restan() {
        return jumlah_restan;
    }

    public void setJumlah_restan(double jumlah_restan) {
        this.jumlah_restan = jumlah_restan;
    }
}
