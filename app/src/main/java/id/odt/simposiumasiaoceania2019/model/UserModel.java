package id.odt.simposiumasiaoceania2019.model;

import java.util.ArrayList;

public class UserModel {
    public String uid;
    public boolean approve;
    public String alergi;
    public String bukti_url;
    public long created_at;
    public String gender;
    public String jurusan;
    public String kota;
    public String nama;
    public String negara;
    public String no_passport;
    public String nohp;
    public boolean puasa;
    public ArrayList<String> status;
    public String universitas;
    public boolean vege;
    public String wechat;
    public String whatsapp;

    public UserModel() {
    }

    public UserModel(String uid, boolean approve, String alergi, String bukti_url, long created_at,
                     String gender, String jurusan, String kota, String nama, String negara,
                     String no_passport, String nohp, boolean puasa, ArrayList<String> status,
                     String universitas, boolean vege, String wechat, String whatsapp) {
        this.uid = uid;
        this.approve = approve;
        this.alergi = alergi;
        this.bukti_url = bukti_url;
        this.created_at = created_at;
        this.gender = gender;
        this.jurusan = jurusan;
        this.kota = kota;
        this.nama = nama;
        this.negara = negara;
        this.no_passport = no_passport;
        this.nohp = nohp;
        this.puasa = puasa;
        this.status = status;
        this.universitas = universitas;
        this.vege = vege;
        this.wechat = wechat;
        this.whatsapp = whatsapp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public String getAlergi() {
        return alergi;
    }

    public void setAlergi(String alergi) {
        this.alergi = alergi;
    }

    public String getBukti_url() {
        return bukti_url;
    }

    public void setBukti_url(String bukti_url) {
        this.bukti_url = bukti_url;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getNo_passport() {
        return no_passport;
    }

    public void setNo_passport(String no_passport) {
        this.no_passport = no_passport;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public boolean isPuasa() {
        return puasa;
    }

    public void setPuasa(boolean puasa) {
        this.puasa = puasa;
    }

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public String getUniversitas() {
        return universitas;
    }

    public void setUniversitas(String universitas) {
        this.universitas = universitas;
    }

    public boolean isVege() {
        return vege;
    }

    public void setVege(boolean vege) {
        this.vege = vege;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
