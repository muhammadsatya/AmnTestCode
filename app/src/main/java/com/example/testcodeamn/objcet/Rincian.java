package com.example.testcodeamn.objcet;

public class Rincian {
    String id = null;
    String id_business = null;
    String peruntukan = null;
    String date_time = null;
    String jumlah = null;
    String image = null;
    int type = 0;

    public Rincian(String id, String id_business, String peruntukan, String date_time, String jumlah, String image, int type){
        this.id = id;
        this.id_business = id_business;
        this.peruntukan = peruntukan;
        this.date_time = date_time;
        this.jumlah = jumlah;
        this.image = image;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_business() {
        return id_business;
    }

    public void setId_business(String id_business) {
        this.id_business = id_business;
    }

    public String getPeruntukan() {
        return peruntukan;
    }

    public void setPeruntukan(String peruntukan) {
        this.peruntukan = peruntukan;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
