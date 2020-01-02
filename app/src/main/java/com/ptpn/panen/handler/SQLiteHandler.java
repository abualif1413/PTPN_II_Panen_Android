package com.ptpn.panen.handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ptpn.panen.Panen;
import com.ptpn.panen.entity.Afdeling;
import com.ptpn.panen.entity.AlatPanen;
import com.ptpn.panen.entity.AppPreferenceConstant;
import com.ptpn.panen.entity.Blok;
import com.ptpn.panen.entity.Distrik;
import com.ptpn.panen.entity.Kebun;
import com.ptpn.panen.entity.KeraniKcs;
import com.ptpn.panen.entity.ListViewAdapterKehadiranPekerja;
import com.ptpn.panen.entity.ListViewAdapterPanen;
import com.ptpn.panen.entity.ListViewAdapterTrip;
import com.ptpn.panen.entity.Mandor;
import com.ptpn.panen.entity.ListViewAdapterKebunAfdeling;
import com.ptpn.panen.entity.Pemanen;
import com.ptpn.panen.entity.Absen;
import com.ptpn.panen.entity.Trip;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SQLiteHandler extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "ptpn2.db";
    private static final int DATABASE_VERSION = 5;

    public SQLiteHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            final String SQL_CREATE_TBL_KEBUN = "CREATE TABLE tbl_kebun(" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "nama_kebun TEXT NOT NULL, " +
                    "id_distrik INTEGER NOT NULL " +
                    ")";

            final String SQL_CREATE_TBL_AFDELING = "CREATE TABLE tbl_afdeling(" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "id_kebun INTEGER NOT NULL, " +
                    "nama_afdeling TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_DISTRIK = "CREATE TABLE tbl_distrik( " +
                    "id  INTEGER PRIMARY KEY NOT NULL, " +
                    "nama_lengkap TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "photo TEXT NOT NULL, " +
                    "token TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_PEMANEN = "CREATE TABLE tbl_pemanen( " +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "id_kerani_askep INTEGER NOT NULL, " +
                    "id_kerani_kcs TEXT NOT NULL, " +
                    "id_mandor TEXT NOT NULL, " +
                    "id_kebun INTEGER NOT NULL, " +
                    "id_afdeling INTEGER NOT NULL, " +
                    "nama_pemanen TEXT NOT NULL, " +
                    "photo TEXT NOT NULL, " +
                    "barcode TEXT NOT NULL, " +
                    "img_barcode TEXT NOT NULL, " +
                    "keterangan TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_MANDOR = "CREATE TABLE tbl_mandor( " +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "nama_lengkap TEXT NOT NULL, " +
                    "id_jabatan INTEGER NOT NULL, " +
                    "id_kerani_askep INTEGER NOT NULL, " +
                    "id_kerani_kcs INTEGER NOT NULL, " +
                    "id_kebun TEXT NOT NULL, " +
                    "id_afdeling TEXT NOT NULL, " +
                    "photo TEXT NOT NULL, " +
                    "token TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_KERANI_KCS = "CREATE TABLE tbl_kerani_kcs( " +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "nama_lengkap TEXT NOT NULL, " +
                    "id_jabatan INTEGER NOT NULL, " +
                    "id_kerani_askep INTEGER NOT NULL, " +
                    "id_kebun TEXT NOT NULL, " +
                    "id_afdeling TEXT NOT NULL, " +
                    "photo TEXT NOT NULL, " +
                    "token TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_ABSEN = "CREATE TABLE tbl_absen (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "id_mandor TEXT NOT NULL, " +
                    "id_absen TEXT NOT NULL, " +
                    "kehadiran TEXT NOT NULL," +
                    "keterangan TEXT NOT NULL, " +
                    "tanggal TEXT NOT NULL, " +
                    "jam TEXT NOT NULL, " +
                    "creat_att TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_AFDELING_PREFERENCE = "CREATE TABLE preference_afdeling(" +
                    "id_afdeling INTEGER PRIMARY KEY NOT NULL, " +
                    "tgl_modifikasi TEXT NOT NULL " +
                    ")";

            final String SQL_CREATE_APP_PREFERENCE = "CREATE TABLE preference_app(" +
                    "nama_variable TEXT PRIMARY KEY NOT NULL," +
                    "nilai_variable TEXT NOT NULL" +
                    ")";

            final String SQL_SET_APP_PREFERENCE = "INSERT INTO preference_app(nama_variable, nilai_variable) VALUES" +
                    "('" + AppPreferenceConstant.SIMPAN_PEMBAHARUAN + "', '0')," +
                    "('" + AppPreferenceConstant.ID_USER_LOGIN + "', '0')," +
                    "('" + AppPreferenceConstant.TIPE_USER_LOGIN + "', '')";

            final String SQL_CREATE_PANEN = "CREATE TABLE tbl_panen (\n" +
                    "id  INTEGER PRIMARY KEY NOT NULL,\n" +
                    "id_kerani_askep  INTEGER NOT NULL,\n" +
                    "id_kerani_kcs  TEXT NOT NULL,\n" +
                    "id_kebun  INTEGER NOT NULL,\n" +
                    "id_afdeling  INTEGER NOT NULL,\n" +
                    "id_pemanen  INTEGER NOT NULL,\n" +
                    "tph  INTEGER NOT NULL,\n" +
                    "blok  INTEGER NOT NULL,\n" +
                    "jmlh_panen  TEXT NOT NULL,\n" +
                    "jmlh_brondolan  TEXT NOT NULL,\n" +
                    "id_alat  INTEGER NOT NULL,\n" +
                    "tanggal  TEXT NOT NULL,\n" +
                    "status  TEXT NOT NULL,\n" +
                    "approve  TEXT NOT NULL,\n" +
                    "kode  TEXT NOT NULL,\n" +
                    "creat_att  TEXT NOT NULL\n" +
                    ")";

            final String SQL_CREATE_BLOK = "CREATE TABLE tbl_blok (\n" +
                    "id  INTEGER PRIMARY KEY NOT NULL,\n" +
                    "id_kebun  INTEGER NOT NULL,\n" +
                    "id_afdeling  INTEGER NOT NULL,\n" +
                    "blok  INTEGER NOT NULL,\n" +
                    "bt  INTEGER NOT NULL,\n" +
                    "p0  INTEGER NOT NULL,\n" +
                    "p1  INTEGER NOT NULL,\n" +
                    "p2  INTEGER NOT NULL,\n" +
                    "p3  INTEGER NOT NULL,\n" +
                    "rp_p0  INTEGER NOT NULL,\n" +
                    "rp_p1  INTEGER NOT NULL,\n" +
                    "rp_p2  INTEGER NOT NULL,\n" +
                    "rp_p3  INTEGER NOT NULL,\n" +
                    "tahun_tanam  INTEGER NOT NULL,\n" +
                    "prediksi_komidel  TEXT NOT NULL,\n" +
                    "status  TEXT NOT NULL,\n" +
                    "keterangan  TEXT NOT NULL\n" +
                    ")";

            final String SQL_CREATE_ALAT = "CREATE TABLE tbl_alat (\n" +
                    "id  INTEGER PRIMARY KEY NOT NULL,\n" +
                    "nama_alat  TEXT NOT NULL,\n" +
                    "premi_alat  INTEGER NOT NULL\n" +
                    ")";

            final String SQL_CREATE_TRIP = "CREATE TABLE tbl_trip (\n" +
                    "id INTEGER PRIMARY KEY NOT NULL,\n" +
                    "sptbs TEXT NOT NULL,\n" +
                    "id_kerani_kcs TEXT NOT NULL,\n" +
                    "id_afdeling INTEGER NOT NULL,\n" +
                    "id_blok_1 TEXT NOT NULL,\n" +
                    "jumlah_janjang_1 TEXT NOT NULL,\n" +
                    "jumlah_brondolan_1 INTEGER NOT NULL,\n" +
                    "id_blok_2 TEXT NOT NULL,\n" +
                    "jumlah_janjang_2 TEXT NOT NULL,\n" +
                    "jumlah_brondolan_2 INTEGER NOT NULL,\n" +
                    "id_blok_3 TEXT NOT NULL,\n" +
                    "jumlah_janjang_3 TEXT NOT NULL,\n" +
                    "jumlah_brondolan_3 INTEGER NOT NULL,\n" +
                    "nomor_polisi_trek TEXT NOT NULL,\n" +
                    "tanggal TEXT NOT NULL\n" +
                    ")";

            final String SQL_UPLOAD_INFO_ABSEN = "CREATE TABLE tbl_upload_info_absen(\n" +
                    "\tid INTEGER PRIMARY KEY NOT NULL,\n" +
                    "\tbarcode TEXT NOT NULL,\n" +
                    "\ttanggal TEXT NOT NULL,\n" +
                    "\tresponse TEXT NOT NULL,\n" +
                    "\ttgl_upload TEXT NOT NULL\n" +
                    ")";

            final String SQL_UPLOAD_INFO_TRIP = "CREATE TABLE tbl_upload_info_trip(\n" +
                    "\tid INTEGER PRIMARY KEY NOT NULL,\n" +
                    "\tno_sptbs TEXT NOT NULL,\n" +
                    "\ttanggal TEXT NOT NULL,\n" +
                    "\tresponse TEXT NOT NULL,\n" +
                    "\ttgl_upload TEXT NOT NULL\n" +
                    ")";

            final String SQL_UPLOAD_INFO_PANEN = "CREATE TABLE tbl_upload_info_panen(\n" +
                    "\tid INTEGER PRIMARY KEY NOT NULL,\n" +
                    "\tid_pemanen TEXT NOT NULL,\n" +
                    "\ttanggal TEXT NOT NULL,\n" +
                    "\tresponse TEXT NOT NULL,\n" +
                    "\ttgl_upload TEXT NOT NULL\n" +
                    ")";

            db.execSQL(SQL_CREATE_TBL_KEBUN);
            db.execSQL(SQL_CREATE_TBL_AFDELING);
            db.execSQL(SQL_CREATE_DISTRIK);
            db.execSQL(SQL_CREATE_PEMANEN);
            db.execSQL(SQL_CREATE_MANDOR);
            db.execSQL(SQL_CREATE_KERANI_KCS);
            db.execSQL(SQL_CREATE_ABSEN);
            db.execSQL(SQL_CREATE_AFDELING_PREFERENCE);
            db.execSQL(SQL_CREATE_APP_PREFERENCE);
            db.execSQL(SQL_SET_APP_PREFERENCE);
            db.execSQL(SQL_CREATE_PANEN);
            db.execSQL(SQL_CREATE_BLOK);
            db.execSQL(SQL_CREATE_ALAT);
            db.execSQL(SQL_CREATE_TRIP);
            db.execSQL(SQL_UPLOAD_INFO_ABSEN);
            db.execSQL(SQL_UPLOAD_INFO_TRIP);
            db.execSQL(SQL_UPLOAD_INFO_PANEN);
            //db.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS tbl_kebun");
            db.execSQL("DROP TABLE IF EXISTS tbl_afdeling");
            db.execSQL("DROP TABLE IF EXISTS tbl_distrik");
            db.execSQL("DROP TABLE IF EXISTS tbl_pemanen");
            db.execSQL("DROP TABLE IF EXISTS tbl_mandor");
            db.execSQL("DROP TABLE IF EXISTS tbl_kerani_kcs");
            db.execSQL("DROP TABLE IF EXISTS tbl_absen");
            db.execSQL("DROP TABLE IF EXISTS preference_afdeling");
            db.execSQL("DROP TABLE IF EXISTS preference_app");
            db.execSQL("DROP TABLE IF EXISTS tbl_panen");
            db.execSQL("DROP TABLE IF EXISTS tbl_blok");
            db.execSQL("DROP TABLE IF EXISTS tbl_alat");
            db.execSQL("DROP TABLE IF EXISTS tbl_trip");
            db.execSQL("DROP TABLE IF EXISTS tbl_upload_info_absen");
            db.execSQL("DROP TABLE IF EXISTS tbl_upload_info_trip");
            db.execSQL("DROP TABLE IF EXISTS tbl_upload_info_panen");
            onCreate(db);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void truncateKebun(){
        final String SQL = "DELETE FROM tbl_kebun";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void insertKebun(Kebun kebun){
        final String SQL = "INSERT INTO tbl_kebun(id, nama_kebun, id_distrik) VALUES ('" + kebun.getId() + "', '" + kebun.getNamaKebun() + "', '" + kebun.getIdDistrik() + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void truncateAfdeling(){
        final String SQL = "DELETE FROM tbl_afdeling";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void insertAfdeling(Afdeling afdeling){
        final String SQL = "INSERT INTO tbl_afdeling(id, id_kebun, nama_afdeling) VALUES('" + afdeling.getId() + "', '" + afdeling.getIdKebun() + "', '" + afdeling.getNamaAfdeling() + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void truncateDistrik(){
        final String SQL = "DELETE FROM tbl_distrik";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void insertDistrik(Distrik distrik){
        final String SQL = "INSERT INTO tbl_distrik(id, nama_lengkap, email, photo, token) VALUES('" + distrik.getId() + "', '" + distrik.getNamaLengkap() + "', '" + distrik.getEmail() + "', '" + distrik.getPhoto() + "', '" + distrik.getToken() + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
        //db.close();
    }

    public void truncatePemanen(){
        final String SQL = "DELETE FROM tbl_pemanen";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void insertPemanen(Pemanen pemanen){
        final String SQL = "INSERT INTO tbl_pemanen (" +
                "id, id_kerani_askep, id_kerani_kcs, " +
                "id_mandor, id_kebun, id_afdeling, " +
                "nama_pemanen, photo, barcode, " +
                "img_barcode, keterangan" +
                ") VALUES(" +
                "'" + pemanen.getId() + "', '" + pemanen.getIdKeraniAskep() + "', '" + pemanen.getIdKeraniKcs() + "'," +
                "'" + pemanen.getIdMandor() + "', '" + pemanen.getIdKebun() + "', '" + pemanen.getIdAfdeling() + "'," +
                "'" + pemanen.getNamaPemanen() + "', '" + pemanen.getPhoto() + "', '" + pemanen.getBarcode() + "'," +
                "'" + pemanen.getImgBarcode() + "', '" + pemanen.getKeterangan() + "'" +
                ")";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public Pemanen getPemanen(int idPemanen) {
        Pemanen pemanen = new Pemanen();
        final String SQL = "SELECT id, id_kerani_askep, id_kerani_kcs, " +
                "id_mandor, id_kebun, id_afdeling, " +
                "nama_pemanen, photo, barcode, " +
                "img_barcode, keterangan FROM tbl_pemanen WHERE id='" + idPemanen + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                pemanen.setId(cursor.getString(0));
                pemanen.setIdKeraniAskep(cursor.getString(1));
                pemanen.setIdKeraniKcs(cursor.getString(2));
                pemanen.setIdMandor(cursor.getString(3));
                pemanen.setIdKebun(cursor.getString(4));
                pemanen.setIdAfdeling(cursor.getString(5));
                pemanen.setNamaPemanen(cursor.getString(6));
                pemanen.setPhoto(cursor.getString(7));
                pemanen.setBarcode(cursor.getString(8));
                pemanen.setImgBarcode(cursor.getString(9));
                pemanen.setKeterangan(cursor.getString(10));
            } while (cursor.moveToNext());
        }

        return pemanen;
    }

    public Pemanen getPemanenViaQRCode(String qrcode) {
        Pemanen pemanen = null;
        final String SQL = "SELECT id, id_kerani_askep, id_kerani_kcs, " +
                "id_mandor, id_kebun, id_afdeling, " +
                "nama_pemanen, photo, barcode, " +
                "img_barcode, keterangan FROM tbl_pemanen WHERE barcode='" + qrcode + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                pemanen = new Pemanen();
                pemanen.setId(cursor.getString(0));
                pemanen.setIdKeraniAskep(cursor.getString(1));
                pemanen.setIdKeraniKcs(cursor.getString(2));
                pemanen.setIdMandor(cursor.getString(3));
                pemanen.setIdKebun(cursor.getString(4));
                pemanen.setIdAfdeling(cursor.getString(5));
                pemanen.setNamaPemanen(cursor.getString(6));
                pemanen.setPhoto(cursor.getString(7));
                pemanen.setBarcode(cursor.getString(8));
                pemanen.setImgBarcode(cursor.getString(9));
                pemanen.setKeterangan(cursor.getString(10));
            } while (cursor.moveToNext());
        }

        return pemanen;
    }

    public void truncateMandor(){
        final String SQL = "DELETE FROM tbl_mandor";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void insertMandor(Mandor mandor){
        final String SQL = "INSERT INTO tbl_mandor(" +
                "id, email, nama_lengkap, id_jabatan, id_kerani_askep, id_kerani_kcs, id_kebun, id_afdeling, photo, token" +
                ") VALUES (" +
                "'" + mandor.getId() + "', '" + mandor.getEmail() + "', '" + mandor.getNamaLengkap() + "', '" + mandor.getIdJabatan() + "', '" + mandor.getIdKeraniAskep() + "', '" + mandor.getIdKeraniKcs() + "'," +
                "'" + mandor.getIdKebun() + "', '" + mandor.getIdAfdeling() + "', '" + mandor.getPhoto() + "', '" + mandor.getToken() + "'" +
                ")";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public Mandor getDataMandorAsPreference() {
        Mandor mandor = new Mandor();

        String pref_id_mandor = getAppPreference(AppPreferenceConstant.ID_USER_LOGIN);

        final String SQL = "SELECT " +
                "id, email, nama_lengkap, " +
                "id_jabatan, id_kerani_askep, id_kerani_kcs, " +
                "id_kebun, id_afdeling, photo, " +
                "token " +
                "FROM " +
                "tbl_mandor " +
                "WHERE id = '" + pref_id_mandor + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                mandor.setId(cursor.getString(0));
                mandor.setEmail(cursor.getString(1));
                mandor.setNamaLengkap(cursor.getString(2));

                mandor.setIdJabatan(cursor.getString(3));
                mandor.setIdKeraniAskep(cursor.getString(4));
                mandor.setIdKeraniKcs(cursor.getString(5));

                mandor.setIdKebun(cursor.getString(6));
                mandor.setIdAfdeling(cursor.getString(7));
                mandor.setPhoto(cursor.getString(8));

                mandor.setToken(cursor.getString(9));
            } while (cursor.moveToNext());
        }


        return mandor;
    }

    public KeraniKcs getDataKeraniKcsAsPreference() {
        KeraniKcs keraniKcs = new KeraniKcs();

        String id_kcs = getAppPreference(AppPreferenceConstant.ID_USER_LOGIN);

        final String SQL = "SELECT " +
                "id, email, nama_lengkap, " +
                "id_jabatan, id_kerani_askep, id_kebun, " +
                "id_afdeling, photo, token " +
                "FROM " +
                "tbl_kerani_kcs " +
                "WHERE " +
                "id = '" + id_kcs + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                keraniKcs.setId(cursor.getString(0));
                keraniKcs.setEmail(cursor.getString(1));
                keraniKcs.setNamaLengkap(cursor.getString(2));
                keraniKcs.setIdJabatan(cursor.getString(3));
                keraniKcs.setIdKeraniAskep(cursor.getString(4));
                keraniKcs.setIdKebun(cursor.getString(5));
                keraniKcs.setIdAfdeling(cursor.getString(6));
                keraniKcs.setPhoto(cursor.getString(7));
                keraniKcs.setToken(cursor.getString(8));
            } while (cursor.moveToNext());
        }


        return keraniKcs;
    }

    public void truncateKeraniKcs(){
        final String SQL = "DELETE FROM tbl_kerani_kcs";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void insertKeraniKcs(KeraniKcs keraniKcs){
        final String SQL = "INSERT INTO tbl_kerani_kcs(id, email, nama_lengkap, id_jabatan, id_kerani_askep, id_kebun, id_afdeling, photo, token)" +
                "VALUES('" + keraniKcs.getId() + "', '" + keraniKcs.getEmail() + "', '" + keraniKcs.getNamaLengkap() + "', '" + keraniKcs.getIdJabatan() + "', '" + keraniKcs.getIdKeraniAskep() + "', '" + keraniKcs.getIdKebun() + "', '" + keraniKcs.getIdAfdeling() + "', '" + keraniKcs.getPhoto() + "', '" + keraniKcs.getToken() + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public List<ListViewAdapterKebunAfdeling> getListViewAdapterKebunAfdeling(){
        List<ListViewAdapterKebunAfdeling> listViewAdapterKebunAfdelings = new ArrayList<ListViewAdapterKebunAfdeling>();
        final String SQL = "SELECT " +
                "afd.id, afd.nama_afdeling, kbn.nama_kebun, dst.nama_lengkap AS nama_distrik," +
                "(SELECT count(*) FROM tbl_blok WHERE id_afdeling = afd.id AND lower(keterangan) = 'all') AS jlh_blok " +
                "FROM " +
                "tbl_afdeling afd " +
                "LEFT JOIN tbl_kebun kbn ON afd.id_kebun = kbn.id " +
                "LEFT JOIN tbl_distrik dst ON kbn.id_distrik = dst.id " +
                "ORDER BY " +
                "kbn.nama_kebun ASC, afd.nama_afdeling ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                ListViewAdapterKebunAfdeling temp =  new ListViewAdapterKebunAfdeling();
                temp.setId(cursor.getInt(0));
                temp.setNamaAfdeling(cursor.getString(1));
                temp.setNamaKebun(cursor.getString(2));
                temp.setNamaDistrik(cursor.getString(3));
                temp.setJlhBlok(cursor.getInt(4));
                listViewAdapterKebunAfdelings.add(temp);
            } while (cursor.moveToNext());
        }

        return listViewAdapterKebunAfdelings;
    }

    public void setAfdelingPreference(int id_afdeling) {
        final String SQL_HAPUS = "DELETE FROM preference_afdeling";
        SQLiteDatabase db_hapus = this.getReadableDatabase();
        db_hapus.execSQL(SQL_HAPUS);

        final String SQL = "INSERT INTO preference_afdeling(id_afdeling, tgl_modifikasi) VALUES('" + id_afdeling + "', (CURRENT_DATE || ' ' || CURRENT_TIME))";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public ListViewAdapterKebunAfdeling getAfdelingPreference() {
        ListViewAdapterKebunAfdeling listViewAdapterKebunAfdelings = new ListViewAdapterKebunAfdeling();
        final String SQL = "SELECT " +
                "afd.id, afd.nama_afdeling, kbn.nama_kebun, dst.nama_lengkap AS nama_distrik, kbn.id AS id_kebun " +
                "FROM " +
                "tbl_afdeling afd " +
                "LEFT JOIN tbl_kebun kbn ON afd.id_kebun = kbn.id " +
                "LEFT JOIN tbl_distrik dst ON kbn.id_distrik = dst.id " +
                "INNER JOIN preference_afdeling pref ON afd.id = pref.id_afdeling " +
                "ORDER BY " +
                "kbn.nama_kebun ASC, afd.nama_afdeling ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                ListViewAdapterKebunAfdeling temp =  new ListViewAdapterKebunAfdeling();
                temp.setId(cursor.getInt(0));
                temp.setNamaAfdeling(cursor.getString(1));
                temp.setNamaKebun(cursor.getString(2));
                temp.setNamaDistrik(cursor.getString(3));
                temp.setIdKebun(cursor.getInt(4));
                listViewAdapterKebunAfdelings = temp;
            } while (cursor.moveToNext());
        }

        return listViewAdapterKebunAfdelings;
    }

    public List<Mandor> getMandorList(int id_afdeling){
        List<Mandor> lstMandor = new ArrayList<Mandor>();

        String sql = "";
        if(id_afdeling == 0) {
            sql = "SELECT id, email, nama_lengkap, " +
                    "id_jabatan, id_kerani_askep, id_kerani_kcs, " +
                    "id_kebun, id_afdeling, photo, " +
                    "token " +
                    "FROM " +
                    "tbl_mandor " +
                    "ORDER BY " +
                    "nama_lengkap ASC";
        } else {
            sql = "SELECT id, email, nama_lengkap, " +
                    "id_jabatan, id_kerani_askep, id_kerani_kcs, " +
                    "id_kebun, id_afdeling, photo, " +
                    "token " +
                    "FROM tbl_mandor " +
                    "WHERE id_afdeling='" + id_afdeling + "' " +
                    "ORDER BY nama_lengkap ASC";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                Mandor temp =  new Mandor();
                temp.setId(cursor.getString(0));
                temp.setEmail(cursor.getString(1));
                temp.setNamaLengkap(cursor.getString(2));
                temp.setIdJabatan(cursor.getString(3));
                temp.setIdKeraniAskep(cursor.getString(4));
                temp.setIdKeraniKcs(cursor.getString(5));
                temp.setIdKebun(cursor.getString(6));
                temp.setIdAfdeling(cursor.getString(7));
                temp.setPhoto(cursor.getString(8));
                temp.setToken(cursor.getString(9));
                lstMandor.add(temp);
            } while (cursor.moveToNext());
        }

        return lstMandor;
    }

    public List<Pemanen> getPemanenList(int id_afdeling) {
        List<Pemanen> lstPemanen = new ArrayList<Pemanen>();

        String sql = "";
        if(id_afdeling == 0) {
            sql = "SELECT id, id_kerani_askep, id_kerani_kcs, " +
                    "id_mandor, id_kebun, id_afdeling, " +
                    "nama_pemanen, photo, barcode, " +
                    "img_barcode, keterangan " +
                    "FROM tbl_pemanen " +
                    "ORDER BY nama_pemanen ASC";
        } else {
            sql = "SELECT id, id_kerani_askep, id_kerani_kcs, " +
                    "id_mandor, id_kebun, id_afdeling, " +
                    "nama_pemanen, photo, barcode, " +
                    "img_barcode, keterangan " +
                    "FROM tbl_pemanen " +
                    "WHERE id_afdeling='" + id_afdeling + "' " +
                    "ORDER BY nama_pemanen ASC";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                Pemanen temp =  new Pemanen();
                temp.setId(cursor.getString(0));
                temp.setIdKeraniAskep(cursor.getString(1));
                temp.setIdKeraniKcs(cursor.getString(2));
                temp.setIdMandor(cursor.getString(3));
                temp.setIdKebun(cursor.getString(4));
                temp.setIdAfdeling(cursor.getString(5));
                temp.setNamaPemanen(cursor.getString(6));
                temp.setPhoto(cursor.getString(7));
                temp.setBarcode(cursor.getString(8));
                temp.setImgBarcode(cursor.getString(9));
                temp.setKeterangan(cursor.getString(10));
                lstPemanen.add(temp);
            } while (cursor.moveToNext());
        }

        return lstPemanen;
    }

    public List<KeraniKcs> getKeraniKcsList(int id_afdeling) {
        List<KeraniKcs> lstKeraniKcs = new ArrayList<KeraniKcs>();

        String sql = "";
        if(id_afdeling == 0) {
            sql = "SELECT " +
                    "id, " +
                    "email, " +
                    "nama_lengkap, " +
                    "id_jabatan, " +
                    "id_kerani_askep, " +
                    "id_kebun, " +
                    "id_afdeling, " +
                    "photo, " +
                    "token " +
                    "FROM tbl_kerani_kcs " +
                    "ORDER BY nama_lengkap ASC";
        } else {
            sql = "SELECT " +
                    "id, " +
                    "email, " +
                    "nama_lengkap, " +
                    "id_jabatan, " +
                    "id_kerani_askep, " +
                    "id_kebun, " +
                    "id_afdeling, " +
                    "photo, " +
                    "token " +
                    "FROM tbl_kerani_kcs " +
                    "WHERE id_afdeling='" + id_afdeling + "' " +
                    "ORDER BY nama_lengkap ASC";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                KeraniKcs temp =  new KeraniKcs();
                temp.setId(cursor.getString(0));
                temp.setEmail(cursor.getString(1));
                temp.setNamaLengkap(cursor.getString(2));
                temp.setIdJabatan(cursor.getString(3));
                temp.setIdKeraniAskep(cursor.getString(4));
                temp.setIdKebun(cursor.getString(5));
                temp.setIdAfdeling(cursor.getString(6));
                temp.setPhoto(cursor.getString(7));
                temp.setToken(cursor.getString(8));
                lstKeraniKcs.add(temp);
            } while (cursor.moveToNext());
        }

        return lstKeraniKcs;
    }

    public KeraniKcs getKeraniKcs(int id_kerani_kcs) {
        KeraniKcs keraniKcs = new KeraniKcs();

        String sql = "";
            sql = "SELECT " +
                    "id, " +
                    "email, " +
                    "nama_lengkap, " +
                    "id_jabatan, " +
                    "id_kerani_askep, " +
                    "id_kebun, " +
                    "id_afdeling, " +
                    "photo, " +
                    "token " +
                    "FROM tbl_kerani_kcs " +
                    "WHERE id='" + id_kerani_kcs + "' " +
                    "ORDER BY nama_lengkap ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                keraniKcs.setId(cursor.getString(0));
                keraniKcs.setEmail(cursor.getString(1));
                keraniKcs.setNamaLengkap(cursor.getString(2));
                keraniKcs.setIdJabatan(cursor.getString(3));
                keraniKcs.setIdKeraniAskep(cursor.getString(4));
                keraniKcs.setIdKebun(cursor.getString(5));
                keraniKcs.setIdAfdeling(cursor.getString(6));
                keraniKcs.setPhoto(cursor.getString(7));
                keraniKcs.setToken(cursor.getString(8));

            } while (cursor.moveToNext());
        }

        return keraniKcs;
    }

    public List<ListViewAdapterKehadiranPekerja> getListViewAdapterKehadiranPekerja(String tanggal) {
        List<ListViewAdapterKehadiranPekerja> lstKehadiranPkerja = new ArrayList<ListViewAdapterKehadiranPekerja>();
        final String SQL = "SELECT " +
                "COALESCE(abs.id, 0) AS id, pemanen.id AS id_pemanen, pemanen.nama_pemanen, COALESCE(abs.kehadiran, '') AS kehadiran, " +
                "COALESCE(abs.tanggal || ' ' || abs.jam,'') AS waktu_kehadiran, abs.keterangan, upl.response " +
                "FROM " +
                "tbl_pemanen pemanen " +
                "LEFT JOIN tbl_absen abs ON pemanen.barcode = abs.id_absen AND abs.tanggal = '" + tanggal + "' " +
                "LEFT JOIN tbl_upload_info_absen upl ON abs.id_absen = upl.barcode AND abs.tanggal = upl.tanggal " +
                "INNER JOIN preference_afdeling pref ON pemanen.id_afdeling = pref.id_afdeling " +
                "ORDER BY " +
                "pemanen.nama_pemanen ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                ListViewAdapterKehadiranPekerja temp = new ListViewAdapterKehadiranPekerja();
                temp.setId(cursor.getInt(0));
                temp.setIdPekerja(cursor.getInt(1));
                temp.setNamaPemanen(cursor.getString(2));
                temp.setStatusKehadiran(cursor.getString(3));
                temp.setTanggal(cursor.getString(4));
                temp.setKeterangan(cursor.getString(5));
                temp.setStatusUpload(cursor.getString(6));
                lstKehadiranPkerja.add(temp);
            } while (cursor.moveToNext());
        }

        return lstKehadiranPkerja;
    }

    public void setAppPreference(String nama_variable, String nilai_variable){
        final String SQL = "UPDATE preference_app SET nilai_variable='" + nilai_variable + "' WHERE nama_variable='" + nama_variable + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public String getAppPreference(String nama_variable){
        String nilai_variable = "";
        final String SQL = "SELECT nilai_variable FROM preference_app WHERE nama_variable='" + nama_variable + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                nilai_variable = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return nilai_variable;
    }

    public HashMap<String, String> loginAplikasi(String username, String password) {
        HashMap<String, String> berhasil = new HashMap<String, String>();
        if(username.equals(password)) {
            final String SQL = "SELECT " +
                    "'mandor' AS jenis, mndr.id " +
                    "FROM " +
                    "tbl_mandor mndr " +
                    "INNER JOIN preference_afdeling pref ON mndr.id_afdeling = pref.id_afdeling " +
                    "WHERE " +
                    "email = '" + username + "' " +
                    "UNION ALL " +
                    "SELECT " +
                    "'kcs' AS jenis, kcs.id " +
                    "FROM " +
                    "tbl_kerani_kcs kcs " +
                    "INNER JOIN preference_afdeling pref ON kcs.id_afdeling = pref.id_afdeling " +
                    "WHERE " +
                    "email = '" + username + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(SQL,null);

            String jenis = "";
            int id = 0;
            if (cursor.moveToFirst()) {
                do {
                    jenis = cursor.getString(0);
                    id = cursor.getInt(1);
                } while (cursor.moveToNext());
            }

            if(id != 0){
                setAppPreference(AppPreferenceConstant.ID_USER_LOGIN, Integer.toString(id));
                setAppPreference(AppPreferenceConstant.TIPE_USER_LOGIN, jenis);
                berhasil.put("hasil", "1");
                berhasil.put("pesan", "Login berhasil");
            } else {
                berhasil.put("hasil", "0");
                berhasil.put("pesan", "Username / email serta password tidak ditemukan pada afdeling yang dikelola");
            }
        } else {
            berhasil.put("hasil", "0");
            berhasil.put("pesan", "Username / email serta password tidak ditemukan");
        }

        return berhasil;
    }

    public int serviceSimpanAbsen(Absen absen) {
        // Mengecek apakah pemanen ini ada didalam afdeling yang dikelola? Jika ya, maka bisa absen, jika tidak, throws exception
        final String SQL_CEK = "SELECT " +
                "pmn.barcode " +
                "FROM " +
                "tbl_pemanen pmn " +
                "INNER JOIN preference_afdeling pref ON pmn.id_afdeling = pref.id_afdeling " +
                "WHERE " +
                "pmn.barcode = '" + absen.getId_absen() + "'";
        SQLiteDatabase db_cek = this.getReadableDatabase();
        Cursor cursor = db_cek.rawQuery(SQL_CEK,null);

        boolean adaDiAfdeling = false;
        if (cursor.moveToFirst()) {
            do {
                adaDiAfdeling = true;
            } while (cursor.moveToNext());
        }

        if(adaDiAfdeling == true) {
            Calendar calendar = Calendar.getInstance();
            String tglSekarang = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd").format(calendar.getTime());
            String jamSekarang = new java.text.SimpleDateFormat(
                    "HH:mm:ss").format(calendar.getTime());

            final String SQL_HAPUS = "DELETE FROM tbl_absen WHERE id_absen='" + absen.getId_absen() + "' AND tanggal='" + tglSekarang + "'";
            SQLiteDatabase db_hapus = this.getReadableDatabase();
            db_hapus.execSQL(SQL_HAPUS);

            final String SQL = "INSERT INTO tbl_absen(id_mandor, id_absen, kehadiran, keterangan, tanggal, jam, creat_att) " +
                    "VALUES('" + absen.getId_mandor() + "', '" + absen.getId_absen() + "', '" + absen.getKehadiran() + "', '" + absen.getKeterangan() + "', '" + tglSekarang + "', '" + jamSekarang + "', '')";
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(SQL);

            return 1;
        } else {
            return 0;
        }
    }

    public List<ListViewAdapterPanen> getDataPanenHarian(String tanggal) {
        List<ListViewAdapterPanen> listViewAdapterPanens = new ArrayList<ListViewAdapterPanen>();

        ListViewAdapterKebunAfdeling dataAfdeling = getAfdelingPreference();
        int id_afdeling = dataAfdeling.getId();

        final String SQL = "SELECT\n" +
                "	pemanen.id AS id_pemanen, coalesce(panen.id, 0) AS id_panen,\n" +
                "	pemanen.nama_pemanen,\n" +
                "	coalesce(panen.tph, '') AS tph,\n" +
                "	coalesce(blok.blok, '') AS blok,\n" +
                "	coalesce(panen.jmlh_panen, '') AS janjang,\n" +
                "	coalesce(panen.jmlh_brondolan, '') AS brondolan,\n" +
                "	coalesce(alat.nama_alat, '') AS id_alat,\n" +
                "   coalesce(blok.tahun_tanam, '') AS tahun_tanam,\n" +
                "   upl.response " +
                "FROM\n" +
                "	tbl_pemanen pemanen\n" +
                "	LEFT JOIN tbl_panen panen ON pemanen.id = panen.id_pemanen AND panen.tanggal = '" + tanggal + "'\n" +
                "	LEFT JOIN tbl_blok blok ON panen.blok = blok.id\n" +
                "   LEFT JOIN tbl_alat alat ON panen.id_alat = alat.id\n" +
                "   LEFT JOIN tbl_upload_info_panen upl ON panen.id_pemanen = upl.id_pemanen AND panen.tanggal = upl.tanggal " +
                "WHERE\n" +
                "	pemanen.id_afdeling = '" + id_afdeling + "'\n" +
                "ORDER BY\n" +
                "	pemanen.nama_pemanen ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                ListViewAdapterPanen temp = new ListViewAdapterPanen();
                temp.setId_pemanen(cursor.getString(0));
                temp.setId_panen(cursor.getString(1));
                temp.setNama_pemanen(cursor.getString(2));
                temp.setTph(cursor.getString(3));
                temp.setBlok(cursor.getString(4) + " (TT : " + cursor.getString(8) + ")");
                temp.setJanjang(cursor.getString(5));
                temp.setBrondolan(cursor.getString(6));
                temp.setId_alat(cursor.getString(7));
                temp.setStatusUpload(cursor.getString(9));
                listViewAdapterPanens.add(temp);
            } while (cursor.moveToNext());
        }

        return listViewAdapterPanens;
    }

    public void truncateBlok() {
        final String SQL = "DELETE FROM tbl_blok";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void insertBlok(Blok blok) {
        final String SQL = "INSERT INTO tbl_blok(\n" +
                "id, " +
                "\tid_kebun, id_afdeling, blok,\n" +
                "\tbt, p0, p1,\n" +
                "\tp2, p3, rp_p0,\n" +
                "\trp_p1, rp_p2, rp_p3,\n" +
                "\ttahun_tanam, prediksi_komidel, status,\n" +
                "\tketerangan\n" +
                ") VALUES(\n" +
                "'" + blok.getId() + "', " +
                "\t'" + blok.getIdKebun() + "', '" + blok.getIdAfdeling() + "', '" + blok.getBlok() + "',\n" +
                "\t'" + blok.getBt() + "', '" + blok.getP0() + "', '" + blok.getP1() + "',\n" +
                "\t'" + blok.getP2() + "', '" + blok.getP3() + "', '" + blok.getRpP0() + "',\n" +
                "\t'" + blok.getRpP1() + "', '" + blok.getRpP2() + "', '" + blok.getRpP3() + "',\n" +
                "\t'" + blok.getTahunTanam() + "', '" + blok.getPrediksiKomidel() + "', '" + blok.getStatus() + "',\n" +
                "\t'" + blok.getKeterangan() + "'\n" +
                ")";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void truncateAlatPanen() {
        final String SQL = "DELETE FROM tbl_alat";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void insertAlatPanen(AlatPanen alatPanen) {
        final String SQL = "INSERT INTO tbl_alat(id, nama_alat, premi_alat) " +
                "VALUES('" + alatPanen.getId() + "', '" + alatPanen.getNamaAlat() + "', '" + alatPanen.getPremiAlat() + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public List<Blok> getListBlok() {
        List<Blok> bloks = new ArrayList<Blok>();

        ListViewAdapterKebunAfdeling dataAfdeling = getAfdelingPreference();
        int id_afdeling = dataAfdeling.getId();

        final String SQL = "select " +
                "id, id_kebun, id_afdeling,\n" +
                "blok, bt, p0,\n" +
                "p1, p2, p3,\n" +
                "rp_p0, rp_p1, rp_p2,\n" +
                "rp_p3, tahun_tanam, prediksi_komidel,\n" +
                "status, keterangan from tbl_blok where id_afdeling = '" + id_afdeling + "' and lower(keterangan) = 'all' order by blok asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                Blok blok = new Blok();
                blok.setId(cursor.getString(0));
                blok.setIdKebun(cursor.getString(1));
                blok.setIdAfdeling(cursor.getString(2));
                blok.setBlok(cursor.getString(3));
                blok.setBt(cursor.getString(4));
                blok.setP0(cursor.getString(5));
                blok.setP1(cursor.getString(6));
                blok.setP2(cursor.getString(7));
                blok.setP3(cursor.getString(8));
                blok.setRpP0(cursor.getString(9));
                blok.setRpP1(cursor.getString(10));
                blok.setRpP2(cursor.getString(11));
                blok.setRpP3(cursor.getString(12));
                blok.setTahunTanam(cursor.getString(13));
                blok.setPrediksiKomidel(cursor.getString(14));
                blok.setStatus(cursor.getString(15));
                blok.setKeterangan(cursor.getString(16));
                bloks.add(blok);

            } while (cursor.moveToNext());
        }

        return bloks;
    }

    public List<AlatPanen> getListAlatPanen() {
        List<AlatPanen> alatPanens = new ArrayList<AlatPanen>();

        final String SQL = "SELECT id, nama_alat, premi_alat FROM tbl_alat ORDER BY nama_alat ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                AlatPanen alatPanen = new AlatPanen();
                alatPanen.setId(cursor.getString(0));
                alatPanen.setNamaAlat(cursor.getString(1));
                alatPanen.setPremiAlat(cursor.getString(2));
                alatPanens.add(alatPanen);
            } while (cursor.moveToNext());
        }

        return alatPanens;
    }

    public void insertPanen(Panen panen) {
        final String hapus = "DELETE FROM tbl_panen WHERE id_pemanen='" + panen.getId_pemanen() + "' AND tanggal='" + panen.getTanggal() + "'";

        final String SQL = "INSERT INTO tbl_panen (\n" +
                "\tid_kerani_askep, id_kerani_kcs, id_kebun,\n" +
                "\tid_afdeling, id_pemanen, tph,\n" +
                "\tblok, jmlh_panen, jmlh_brondolan,\n" +
                "\tid_alat, tanggal, status,\n" +
                "\tapprove, kode, creat_att\n" +
                ") VALUES (\n" +
                "\t'" + panen.getId_kerani_askep() + "', '" + panen.getId_kerani_kcs() + "', '" + panen.getId_kebun() + "',\n" +
                "\t'" + panen.getId_afdeling() + "', '" + panen.getId_pemanen() + "', '" + panen.getTph() + "',\n" +
                "\t'" + panen.getBlok() + "', '" + panen.getJmlh_panen() + "', '" + panen.getJmlh_brondolan() + "',\n" +
                "\t'" + panen.getId_alat() + "', '" + panen.getTanggal() + "', '" + panen.getStatus() + "',\n" +
                "\t'" + panen.getApprove() + "', '" + panen.getKode() + "', '" + panen.getCreat_att() + "'\n" +
                ")";

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(hapus);
        db.execSQL(SQL);
    }

    public Panen getPanen(String id_panen) {
        Panen panen = new Panen();
        final String SQL = "SELECT id, " +
                "id_kerani_askep, id_kerani_kcs, id_kebun,\n" +
                "id_afdeling, id_pemanen, tph,\n" +
                "blok, jmlh_panen, jmlh_brondolan,\n" +
                "id_alat, tanggal, status,\n" +
                "approve, kode, creat_att " +
                "FROM tbl_panen " +
                "WHERE id='" + id_panen + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                Panen temp = new Panen();
                temp.setId(cursor.getInt(0));
                temp.setId_kerani_askep(cursor.getInt(1));
                temp.setId_kerani_kcs(cursor.getString(2));
                temp.setId_kebun(cursor.getInt(3));
                temp.setId_afdeling(cursor.getInt(4));
                temp.setId_pemanen(cursor.getInt(5));
                temp.setTph(cursor.getInt(6));
                temp.setBlok(cursor.getInt(7));
                temp.setJmlh_panen(cursor.getString(8));
                temp.setJmlh_brondolan(cursor.getString(9));
                temp.setId_alat(cursor.getInt(10));
                temp.setTanggal(cursor.getString(11));
                temp.setStatus(cursor.getString(12));
                temp.setApprove(cursor.getString(13));
                temp.setKode(cursor.getString(14));
                temp.setCreat_att(cursor.getString(15));
                panen = temp;
            } while (cursor.moveToNext());
        }

        return panen;
    }

    public int generateNoSptbs(String tanggal) {
        int noSptbs = 0;

        final String SQL = "SELECT\n" +
                "\tsptbs, CAST(substr(sptbs,13,4) AS INT) AS buntut\n" +
                "FROM\n" +
                "\ttbl_trip\n" +
                "WHERE\n" +
                "\tsubstr(tanggal, 1, 7) = substr('" + tanggal + "', 1, 7) " +
                "ORDER BY\n" +
                "\tCAST(substr(sptbs,13,4) AS INT) ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                String sptbsExist = cursor.getString(0);
                String[] pecahSptbs = sptbsExist.split("-");
                String ujung = pecahSptbs[4];
                noSptbs = Integer.parseInt(ujung);
                Log.d("sptbs", cursor.getString(0) + " - " + noSptbs);
            } while (cursor.moveToNext());
        }

        noSptbs += 1;

        return noSptbs;
    }

    public void insertTrip(Trip trip) {
        final String SQL = "INSERT INTO tbl_trip (" +
                "sptbs,id_kerani_kcs,id_afdeling,\n" +
                "id_blok_1,jumlah_janjang_1,jumlah_brondolan_1,\n" +
                "id_blok_2,jumlah_janjang_2,jumlah_brondolan_2,\n" +
                "id_blok_3,jumlah_janjang_3,jumlah_brondolan_3,\n" +
                "nomor_polisi_trek,tanggal" +
                ") VALUES (" +
                "'" + trip.getSptbs() + "', '" + trip.getId_kerani_kcs() + "', '" + trip.getId_afdeling() + "'," +
                "'" + trip.getId_blok_1() + "', '" + trip.getJumlah_janjang_1() + "', '" + trip.getJumlah_brondolan_1() + "'," +
                "'" + trip.getId_blok_2() + "', '" + trip.getJumlah_janjang_2() + "', '" + trip.getJumlah_brondolan_2() + "'," +
                "'" + trip.getId_blok_3() + "', '" + trip.getJumlah_janjang_3() + "', '" + trip.getJumlah_brondolan_3() + "'," +
                "'" + trip.getNomor_polisi_trek() + "', '" + trip.getTanggal() + "'" +
                ")";

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void updateTrip(Trip trip) {
        final String SQL = "UPDATE tbl_trip SET " +
                "sptbs='" + trip.getSptbs() + "', " +
                "id_blok_1='" + trip.getId_blok_1() + "',jumlah_janjang_1='" + trip.getJumlah_janjang_1() + "',jumlah_brondolan_1='" + trip.getJumlah_brondolan_1() + "',\n" +
                "id_blok_2='" + trip.getId_blok_2() + "',jumlah_janjang_2='" + trip.getJumlah_janjang_2() + "',jumlah_brondolan_2='" + trip.getJumlah_brondolan_2() + "',\n" +
                "id_blok_3='" + trip.getId_blok_3() + "',jumlah_janjang_3='" + trip.getJumlah_janjang_3() + "',jumlah_brondolan_3='" + trip.getJumlah_brondolan_3() + "',\n" +
                "nomor_polisi_trek='" + trip.getNomor_polisi_trek() + "' " +
                "WHERE " +
                "id='" + trip.getId() + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public void deleteTrip(Trip trip) {
        final String SQL = "DELETE FROM tbl_trip WHERE id='" + trip.getId() + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SQL);
    }

    public List<ListViewAdapterTrip> getListTrip(String tanggal) {
        List<ListViewAdapterTrip> trips = new ArrayList<ListViewAdapterTrip>();

        final String SQL = "SELECT\n" +
                "\ttrp.id, trp.sptbs, trp.id_kerani_kcs, trp.id_afdeling,\n" +
                "\ttrp.id_blok_1, trp.jumlah_janjang_1, trp.jumlah_brondolan_1,\n" +
                "\ttrp.id_blok_2, trp.jumlah_janjang_2, trp.jumlah_brondolan_2,\n" +
                "\ttrp.id_blok_3, trp.jumlah_janjang_3, trp.jumlah_brondolan_3,\n" +
                "\ttrp.nomor_polisi_trek, trp.tanggal,\n" +
                "\tcoalesce(blok1.blok, 0) AS blok1, coalesce(blok2.blok, 0) AS blok2, coalesce(blok3.blok, 0) AS blok3,\n" +
                "\tcoalesce(blok1.tahun_tanam, '') AS thn_tanam1, coalesce(blok2.tahun_tanam, '') AS thn_tanam2, coalesce(blok3.tahun_tanam, '') AS thn_tanam3,\n" +
                "upl.response " +
                "FROM\n" +
                "\ttbl_trip trp\n" +
                "\tLEFT JOIN tbl_blok blok1 ON trp.id_blok_1 = blok1.id\n" +
                "\tLEFT JOIN tbl_blok blok2 ON trp.id_blok_2 = blok2.id\n" +
                "\tLEFT JOIN tbl_blok blok3 ON trp.id_blok_3 = blok3.id\n" +
                "LEFT JOIN tbl_upload_info_trip upl ON trp.sptbs = upl.no_sptbs AND trp.tanggal = upl.tanggal " +
                "WHERE\n" +
                "\ttrp.tanggal = '" + tanggal + "' " +
                "ORDER BY " +
                "trp.id ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                ListViewAdapterTrip trip = new ListViewAdapterTrip();
                trip.setId(cursor.getInt(0));
                trip.setSptbs(cursor.getString(1));
                trip.setId_kerani_kcs(cursor.getString(2));
                trip.setId_afdeling(cursor.getInt(3));

                trip.setId_blok_1(cursor.getString(4));
                trip.setJumlah_janjang_1(cursor.getString(5));
                trip.setJumlah_brondolan_1(cursor.getInt(6));

                trip.setId_blok_2(cursor.getString(7));
                trip.setJumlah_janjang_2(cursor.getString(8));
                trip.setJumlah_brondolan_2(cursor.getInt(9));

                trip.setId_blok_3(cursor.getString(10));
                trip.setJumlah_janjang_3(cursor.getString(11));
                trip.setJumlah_brondolan_3(cursor.getInt(12));

                trip.setNomor_polisi_trek(cursor.getString(13));
                trip.setTanggal(cursor.getString(14));

                trip.setNama_blok_1(cursor.getString(15));
                trip.setNama_blok_2(cursor.getString(16));
                trip.setNama_blok_3(cursor.getString(17));

                trip.setThn_tanam_1(cursor.getString(18));
                trip.setThn_tanam_2(cursor.getString(19));
                trip.setThn_tanam_3(cursor.getString(20));

                trip.setStatusUpload(cursor.getString(21));

                trips.add(trip);
            } while (cursor.moveToNext());
        }

        return trips;
    }

    public Trip getTripObject(String id_trip) {
        Trip trip = new Trip();

        final String SQL = "SELECT " +
                "id, sptbs,id_kerani_kcs,id_afdeling,\n" +
                "id_blok_1,jumlah_janjang_1,jumlah_brondolan_1,\n" +
                "id_blok_2,jumlah_janjang_2,jumlah_brondolan_2,\n" +
                "id_blok_3,jumlah_janjang_3,jumlah_brondolan_3,\n" +
                "nomor_polisi_trek,tanggal " +
                "FROM " +
                " tbl_trip " +
                "WHERE " +
                "id = '" + id_trip + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                trip.setId(cursor.getInt(0));
                trip.setSptbs(cursor.getString(1));
                trip.setId_kerani_kcs(cursor.getString(2));
                trip.setId_afdeling(cursor.getInt(3));

                trip.setId_blok_1(cursor.getString(4));
                trip.setJumlah_janjang_1(cursor.getString(5));
                trip.setJumlah_brondolan_1(cursor.getInt(6));

                trip.setId_blok_2(cursor.getString(7));
                trip.setJumlah_janjang_2(cursor.getString(8));
                trip.setJumlah_brondolan_2(cursor.getInt(9));

                trip.setId_blok_3(cursor.getString(10));
                trip.setJumlah_janjang_3(cursor.getString(11));
                trip.setJumlah_brondolan_3(cursor.getInt(12));

                trip.setNomor_polisi_trek(cursor.getString(13));
                trip.setTanggal(cursor.getString(14));

            } while (cursor.moveToNext());
        }

        return trip;
    }

    public List<Absen> getAbsenTable(String tanggal) {
        List<Absen> absens = new ArrayList<Absen>();

        final String SQL = "SELECT " +
                "id, id_mandor, id_absen, kehadiran, keterangan, tanggal, jam, creat_att " +
                "FROM tbl_absen " +
                "WHERE tanggal='" + tanggal + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                Absen temp = new Absen();
                temp.setId(cursor.getInt(0));
                temp.setId_mandor(cursor.getString(1));
                temp.setId_absen(cursor.getString(2));
                temp.setKehadiran(cursor.getString(3));
                temp.setKeterangan(cursor.getString(4));
                temp.setTanggal(cursor.getString(5));
                temp.setJam(cursor.getString(6));
                temp.setCreat_att(cursor.getString(7));
                absens.add(temp);
            } while (cursor.moveToNext());
        }

        return absens;
    }

    public List<Panen> getPanenTable(String tanggal) {
        List<Panen> panens = new ArrayList<Panen>();

        final String SQL = "SELECT id, " +
                "id_kerani_askep, id_kerani_kcs, id_kebun,\n" +
                "id_afdeling, id_pemanen, tph,\n" +
                "blok, jmlh_panen, jmlh_brondolan,\n" +
                "id_alat, tanggal, status,\n" +
                "approve, kode, creat_att " +
                "FROM tbl_panen " +
                "WHERE tanggal='" + tanggal + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                Panen temp = new Panen();
                temp.setId(cursor.getInt(0));
                temp.setId_kerani_askep(cursor.getInt(1));
                temp.setId_kerani_kcs(cursor.getString(2));
                temp.setId_kebun(cursor.getInt(3));
                temp.setId_afdeling(cursor.getInt(4));
                temp.setId_pemanen(cursor.getInt(5));
                temp.setTph(cursor.getInt(6));
                temp.setBlok(cursor.getInt(7));
                temp.setJmlh_panen(cursor.getString(8));
                temp.setJmlh_brondolan(cursor.getString(9));
                temp.setId_alat(cursor.getInt(10));
                temp.setTanggal(cursor.getString(11));
                temp.setStatus(cursor.getString(12));
                temp.setApprove(cursor.getString(13));
                temp.setKode(cursor.getString(14));
                temp.setCreat_att(cursor.getString(15));
                panens.add(temp);
            } while (cursor.moveToNext());
        }

        return panens;
    }

    public List<Trip> getTripTable(String tanggal) {
        List<Trip> trips = new ArrayList<Trip>();

        final String SQL = "SELECT\n" +
                "\tid, sptbs, id_kerani_kcs,\n" +
                "\tid_afdeling,\n" +
                "\tid_blok_1, jumlah_janjang_1, jumlah_brondolan_1,\n" +
                "\tid_blok_2, jumlah_janjang_2, jumlah_brondolan_2,\n" +
                "\tid_blok_3, jumlah_janjang_3, jumlah_brondolan_3,\n" +
                "\tnomor_polisi_trek, tanggal\n" +
                "FROM\n" +
                "\ttbl_trip\n" +
                "WHERE\n" +
                "\ttanggal = '" + tanggal + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.moveToFirst()) {
            do {
                Trip temp = new Trip();
                temp.setId(cursor.getInt(0));
                temp.setSptbs(cursor.getString(1));
                temp.setId_kerani_kcs(cursor.getString(2));
                temp.setId_afdeling(cursor.getInt(3));
                temp.setId_blok_1(cursor.getString(4));
                temp.setJumlah_janjang_1(cursor.getString(5));
                temp.setJumlah_brondolan_1(cursor.getInt(6));

                temp.setId_blok_2(cursor.getString(7));
                temp.setJumlah_janjang_2(cursor.getString(8));
                temp.setJumlah_brondolan_2(cursor.getInt(9));

                temp.setId_blok_3(cursor.getString(10));
                temp.setJumlah_janjang_3(cursor.getString(11));
                temp.setJumlah_brondolan_3(cursor.getInt(12));

                temp.setNomor_polisi_trek(cursor.getString(13));
                temp.setTanggal(cursor.getString(14));

                trips.add(temp);
            } while (cursor.moveToNext());
        }

        return trips;
    }

    public void pushStatusUploadAbsen(String barcode, String tanggal, String response) {
        Calendar calendar = Calendar.getInstance();
        String tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        String sql_hapus = "DELETE FROM tbl_upload_info_absen WHERE barcode='" + barcode + "' AND tanggal = '" + tanggal + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql_hapus);

        String sql_simpan = "INSERT INTO tbl_upload_info_absen(barcode, tanggal, response, tgl_upload) " +
                "VALUES('" + barcode + "', '" + tanggal + "', '" + response + "', '" + tglSekarang + "')";
        db.execSQL(sql_simpan);
    }

    public void pushStatusUploadPanen(String id_pemanen, String tanggal, String response) {
        Calendar calendar = Calendar.getInstance();
        String tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        String sql_hapus = "DELETE FROM tbl_upload_info_panen WHERE id_pemanen='" + id_pemanen + "' AND tanggal = '" + tanggal + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql_hapus);

        String sql_simpan = "INSERT INTO tbl_upload_info_panen(id_pemanen, tanggal, response, tgl_upload) " +
                "VALUES('" + id_pemanen + "', '" + tanggal + "', '" + response + "', '" + tglSekarang + "')";
        db.execSQL(sql_simpan);
    }

    public void pushStatusUploadTrip(String no_sptbs, String tanggal, String response) {
        Calendar calendar = Calendar.getInstance();
        String tglSekarang = new java.text.SimpleDateFormat(
                "yyyy-MM-dd").format(calendar.getTime());

        String sql_hapus = "DELETE FROM tbl_upload_info_trip WHERE no_sptbs='" + no_sptbs + "' AND tanggal = '" + tanggal + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql_hapus);

        String sql_simpan = "INSERT INTO tbl_upload_info_trip(no_sptbs, tanggal, response, tgl_upload) " +
                "VALUES('" + no_sptbs + "', '" + tanggal + "', '" + response + "', '" + tglSekarang + "')";
        db.execSQL(sql_simpan);
    }
}
