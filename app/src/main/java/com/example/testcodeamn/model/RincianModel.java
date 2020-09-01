package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RincianModel {
    private String idrincian;
    private String idbusiness;
    private String peruntukan;
    private String datetime;
    private String jumlah;
    private String image;
    private sqLiteDb dbHelper;

    public RincianModel(Context context, String idrincian, String idbusiness, String peruntukan, String datetime, String jumlah, String image){
        this.idrincian = idrincian;
        this.idbusiness = idbusiness;
        this.peruntukan = peruntukan;
        this.datetime = datetime;
        this.jumlah = jumlah;
        this.image = image;
        dbHelper = new sqLiteDb(context);
    }

    public String getIdrincian() {
        return idrincian;
    }

    public String getIdbusiness() {
        return idbusiness;
    }

    public String getPeruntukan() {
        return peruntukan;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getImage() {
        return image;
    }


    public int addRincianData(String idrincian, String peruntukan, String datetime, String jumlah, String image){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM RINCIAN_TEMP WHERE id_rincian ='" + idrincian +"'",null);
            cursor.moveToFirst();
             data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO RINCIAN_TEMP (" +
                        "id_rincian, " +
                        "peruntukan, " +
                        "date_time, " +
                        "jumlah, " +
                        "image " +
                        ") VALUES"
                        + "('" + idrincian + "',"
                        + "'" + peruntukan + "',"
                        + "'" + datetime + "',"
                        + "'" + jumlah + "',"
                        + "'" + image + "');";
                db.execSQL(sqlinsert);
                Log.d("InsertDataTemp",sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public int addDetailRincianData(String idrincian, String idbusiness, String peruntukan, String datetime, String jumlah, String image){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM RINCIAN WHERE id_rincian ='" + idrincian +"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO RINCIAN (" +
                        "id_rincian, " +
                        "id_business, " +
                        "peruntukan, " +
                        "date_time, " +
                        "jumlah, " +
                        "image " +
                        ") VALUES"
                        + "('" + idrincian + "',"
                        + "'" + idbusiness + "',"
                        + "'" + peruntukan + "',"
                        + "'" + datetime + "',"
                        + "'" + jumlah + "',"
                        + "'" + image + "');";
                db.execSQL(sqlinsert);
                Log.d("InsertDataDetail",sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public String deleteTempDate(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM RINCIAN_TEMP");
            reqpart.put("status", "0");
            reqpart.put("message", "Save Data Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Save Data Failed");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorDelete",e.toString());
        }

        return msg;
    }
}
