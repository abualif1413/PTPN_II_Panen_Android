package com.ptpn.panen.entity;

import java.util.List;

public class Trip020 {
    public int id;
    public String sptbs;
    public int id_kerani_askep;
    public int id_kerani_kcs;
    public int id_kebun;
    public int id_afdeling;
    public double jumlah_brondolan;
    public String nomor_polisi_trek;
    public String tanggal;
    public List<Trip020Detail> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSptbs() {
        return sptbs;
    }

    public void setSptbs(String sptbs) {
        this.sptbs = sptbs;
    }

    public int getId_kerani_askep() {
        return id_kerani_askep;
    }

    public void setId_kerani_askep(int id_kerani_askep) {
        this.id_kerani_askep = id_kerani_askep;
    }

    public int getId_kerani_kcs() {
        return id_kerani_kcs;
    }

    public void setId_kerani_kcs(int id_kerani_kcs) {
        this.id_kerani_kcs = id_kerani_kcs;
    }

    public int getId_kebun() {
        return id_kebun;
    }

    public void setId_kebun(int id_kebun) {
        this.id_kebun = id_kebun;
    }

    public int getId_afdeling() {
        return id_afdeling;
    }

    public void setId_afdeling(int id_afdeling) {
        this.id_afdeling = id_afdeling;
    }

    public double getJumlah_brondolan() {
        return jumlah_brondolan;
    }

    public void setJumlah_brondolan(double jumlah_brondolan) {
        this.jumlah_brondolan = jumlah_brondolan;
    }

    public String getNomor_polisi_trek() {
        return nomor_polisi_trek;
    }

    public void setNomor_polisi_trek(String nomor_polisi_trek) {
        this.nomor_polisi_trek = nomor_polisi_trek;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public List<Trip020Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Trip020Detail> details) {
        this.details = details;
    }
}
