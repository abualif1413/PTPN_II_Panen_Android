package com.ptpn.panen.handler;

import com.ptpn.panen.entity.JsonAfdeling;
import com.ptpn.panen.entity.JsonAlatPanen;
import com.ptpn.panen.entity.JsonBlok;
import com.ptpn.panen.entity.JsonDistrik;
import com.ptpn.panen.entity.JsonKebun;
import com.ptpn.panen.entity.JsonKeraniKcs;
import com.ptpn.panen.entity.JsonMandor;
import com.ptpn.panen.entity.JsonPemanen;
import com.ptpn.panen.entity.JsonPostAbsen;
import com.ptpn.panen.entity.JsonPostUmum;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitApiInterface {
    @GET("api/kebun")
    Call<JsonKebun> getJsonKebun();

    @GET("api/afdeling")
    Call<JsonAfdeling> getJsonAfdeling();

    @GET("api/distrik")
    Call<JsonDistrik> getJsonDistrik();

    @GET("api/Mandor")
    Call<JsonMandor> getJsonMandor();

    @GET("api/Pemanen")
    Call<JsonPemanen> getJsonPemanen();

    @GET("api/Keranikcs")
    Call<JsonKeraniKcs> getJsonKeraniKcs();

    @GET("api/blok")
    Call<JsonBlok> getJsonBlok();

    @GET("api/alat")
    Call<JsonAlatPanen> getAlatPanen();

    @POST("api/Absen")
    @FormUrlEncoded
    Call<JsonPostAbsen> getPostAbsen(@Field("id_mandor") String idMandor,
                                     @Field("id_absen") String idAbsen,
                                     @Field("kehadiran") String kehadiran,
                                     @Field("tanggal") String tanggal,
                                     @Field("jam") String jam,
                                     @Field("device") String device);

    @POST("api/Panen")
    @FormUrlEncoded
    Call<JsonPostUmum> getPostPanen(@Field("id_kerani_askep") String idKeraniAskep,
                                    @Field("id_kerani_kcs") String idKeraniKcs,
                                    @Field("id_kebun") String idKebun,
                                    @Field("id_afdeling") String idAfdeling,
                                    @Field("id_pemanen") String idPemanen,
                                    @Field("tph") String tph,
                                    @Field("blok") String blok,
                                    @Field("jmlh_panen") String jmlhPanen,
                                    @Field("jmlh_brondolan") String jmlhBrondolan,
                                    @Field("id_alat") String idAlat,
                                    @Field("tanggal") String tanggal,
                                    @Field("status") String status,
                                    @Field("approve") String approve,
                                    @Field("kode") String kode,
                                    @Field("device") String device);

    @POST("api/Trip")
    @FormUrlEncoded
    Call<JsonPostUmum> getPostTrip(
            @Field("no_sptbs") String NoSptbs,
            @Field("id_kerani_askep") String idKeraniAskep,
            @Field("id_kerani_kcs") String idKeraniKcs,
            @Field("id_kebun") String idKebun,
            @Field("id_afdeling") String idAfdeling,
            @Field("id_blok_1") String idBlok1,
            @Field("id_blok_2") String idBlok2,
            @Field("id_blok_3") String idBlok3,
            @Field("jumlah_janjang_1") String jlhJanjang1,
            @Field("jumlah_janjang_2") String jlhJanjang2,
            @Field("jumlah_janjang_3") String jlhJanjang3,
            @Field("jumlah_brondolan_1") String jlhBrd1,
            @Field("jumlah_brondolan_2") String jlhBrd2,
            @Field("jumlah_brondolan_3") String jlhBrd3,
            @Field("nomor_polisi_trek") String noPolisiTrek,
            @Field("tanggal") String tanggal,
            @Field("status") String status,
            @Field("device") String device
    );

    @POST("api/Trip020")
    @FormUrlEncoded
    Call<JsonPostUmum> getPostTrip020(
            @Field("data") String data
    );

    @POST("api/Panen020")
    @FormUrlEncoded
    Call<JsonPostUmum> getPostPanen020(
            @Field("data") String data
    );
}
