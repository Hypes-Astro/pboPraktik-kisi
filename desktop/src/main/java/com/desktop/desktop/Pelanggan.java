package com.desktop.desktop;

public class Pelanggan extends Produk {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    private String id;
    private String nama;
    private String alamat;
    public Pelanggan(){
        //nothing
    }
    public Pelanggan(String id,String nama, String alamat) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
    }
    public String getNama() {
        return this.nama;
    }
}
