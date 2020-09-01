package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailTripModel {
    private sqLiteDb dbHelper;
    private String id_business;
    public DetailTripModel(Context context, String id_business){
        this.id_business = id_business;
        dbHelper = new sqLiteDb(context);
    }

    public String getId_business() {
        return id_business;
    }

    public String getDataRincian(String id_business){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int dataDB;
        String msg = "";
        String data = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM RINCIAN WHERE id_business = '"+id_business+"'",null);
            cursor.moveToFirst();
            dataDB = cursor.getCount();

            if (dataDB > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    jsonObject.put("id", cursor.getString(cursor.getColumnIndex("id_rincian")));
                    jsonObject.put("id_business", cursor.getString(cursor.getColumnIndex("id_business")));
                    jsonObject.put("peruntukan", cursor.getString(cursor.getColumnIndex("peruntukan")));
                    jsonObject.put("date_time", cursor.getString(cursor.getColumnIndex("date_time")));
                    jsonObject.put("jumlah", cursor.getString(cursor.getColumnIndex("jumlah")));
                    jsonObject.put("image", cursor.getString(cursor.getColumnIndex("image")));
                    jsonArray.put(jsonObject);
                    data = jsonArray + "";
                }
                reqpart.put("status", "0");
                reqpart.put("message", data);
                arrayData.put(reqpart);
                msg = arrayData + "";
            }else {
                reqpart.put("status", "-1");
                reqpart.put("message", "No data we are found");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }
            cursor.close();

        }catch (Exception e){
            try {
                reqpart.put("status", "-2");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorSearch",e.toString());
        }
        return msg;
    }

    public String getDataRincianTemp(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int dataDB;
        String msg = "";
        String data = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM RINCIAN_TEMP ",null);
            cursor.moveToFirst();
            dataDB = cursor.getCount();

            if (dataDB > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    Log.d("CheckPeruntukan", cursor.getString(cursor.getColumnIndex("peruntukan")));
                    jsonObject.put("id", cursor.getString(cursor.getColumnIndex("id_rincian")));
                    jsonObject.put("peruntukan", cursor.getString(cursor.getColumnIndex("peruntukan")));
                    jsonObject.put("date_time", cursor.getString(cursor.getColumnIndex("date_time")));
                    jsonObject.put("jumlah", cursor.getString(cursor.getColumnIndex("jumlah")));
                    jsonObject.put("image", cursor.getString(cursor.getColumnIndex("image")));
                    jsonArray.put(jsonObject);
                    data = jsonArray + "";
                }
                reqpart.put("status", "0");
                reqpart.put("message", data);
                arrayData.put(reqpart);
                msg = arrayData + "";
            }else {
                reqpart.put("status", "-1");
                reqpart.put("message", "No data we are found");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }
            cursor.close();

        }catch (Exception e){
            try {
                reqpart.put("status", "-2");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorSearch",e.toString());
        }
        return msg;
    }


}