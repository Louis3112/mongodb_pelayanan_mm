package com.example;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public abstract class AbsUser {
    protected String namaLengkap;
    protected Date tanggalLahir;
    protected String noTelp;
    protected String alamat;
    protected String email;
    protected int age;

    public AbsUser(String namaLengkap, Date tanggalLahir, String noTelp, String alamat, String email, int age) {
        this.namaLengkap = namaLengkap;
        this.tanggalLahir = tanggalLahir;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.email = email;
        this.age = calculateAge(tanggalLahir);
    }
    
    public AbsUser() {
        this.namaLengkap = "";
        this.tanggalLahir = null;
        this.noTelp = "";
        this.alamat = "";
        this.email = "";
        this.age = 0;
    }
    public final int calculateAge(Date tglLahir){
        if (tglLahir == null) {
            return 0;
        }
        else{
            return Period.between(tglLahir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears(); 
        }
    }
    
    public void setNamaLengkap(String namaLengkap) {this.namaLengkap = namaLengkap;}
    public String getNamaLengkap() {return this.namaLengkap;}
    public void setTanggalLahir(Date tanggalLahir) {this.tanggalLahir = tanggalLahir;}
    public Date getTanggalLahir() {return this.tanggalLahir;}
    public void setNoTelp(String noTelp) {this.noTelp = noTelp;}
    public String getNoTelp() {return this.noTelp;}
    public void setAlamat(String Alamat) {this.alamat = Alamat;}
    public String getAlamat() {return this.alamat;}
    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return this.email;}
    public void setAge(int age) {this.age = calculateAge(tanggalLahir);}
    public int getAge() {return this.age;}
    
}
