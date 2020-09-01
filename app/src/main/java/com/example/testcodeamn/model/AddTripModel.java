package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;
import com.example.testcodeamn.objcet.Rincian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddTripModel {
    private String idBusiness;
    private String userId;
    String employee;
    String visitDate;
    String destination;
    private sqLiteDb dbHelper;
    public AddTripModel(Context context, String idBusiness, String userId, String employee, String visitDate, String destination, String dataJson){
        this.idBusiness = idBusiness;
        this.userId = userId;
        this.employee = employee;
        this.visitDate = visitDate;
        this.destination = destination;
        dbHelper = new sqLiteDb(context);
    }

    public String getIdBusiness() {
        return idBusiness;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmployee() {
        return employee;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getDestination() {
        return destination;
    }


    public int addTripData(String idBusiness, String userId, String employee, String visitDate, String destination){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM BUSINESS_TRIP WHERE id_business ='" + idBusiness +"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO BUSINESS_TRIP (" +
                        "id_business, " +
                        "user_id, " +
                        "employee, " +
                        "visit_date, " +
                        "Destination " +
                        ") VALUES"
                        + "('" + idBusiness + "',"
                        + "'" + userId + "',"
                        + "'" + employee + "',"
                        + "'" + visitDate + "',"
                        + "'" + destination + "');";
                db.execSQL(sqlinsert);
                Log.d("insertTrip",sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionErrorTrip",e.toString());
        }
        return status;
    }

    public int addDetailRincianData(String dataJson){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("CekJson",dataJson);
        int data;
        int status = 0;
        int statuscek = 0;
        try {
            Cursor cursor = null;
            JSONArray jsa = new JSONArray(dataJson);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);

                String id;
                String idBusiness;
                String peruntukan;
                String dateTime;
                String jumlah;
                String image;

                id = jso.getString("id");
                idBusiness = jso.getString("id_business");
                peruntukan = jso.getString("peruntukan");
                dateTime = jso.getString("date_time");
                jumlah = jso.getString("jumlah");
                image = jso.getString("image");
                String sqlinsert = "INSERT INTO RINCIAN (" +
                        "id_rincian, " +
                        "id_business, " +
                        "peruntukan, " +
                        "date_time, " +
                        "jumlah, " +
                        "image " +
                        ") VALUES"
                        + "('" + id + "',"
                        + "'" + idBusiness + "',"
                        + "'" + peruntukan + "',"
                        + "'" + dateTime + "',"
                        + "'" + jumlah + "',"
                        + "'" + image + "');";
                db.execSQL(sqlinsert);
                Log.d("InsertDataRincian",sqlinsert);
                status = 0;
            }
           // cursor.close();
        }catch (Exception e){
            status = -1;
            Log.d("ExceptionInsertRincian",e.toString());
        }
        return status;
    }


    public String deleteItem(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM RINCIAN_TEMP WHERE id_rincian ='"+id+"'");
            reqpart.put("status", "0");
            reqpart.put("message", "Delete Data Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Delete Data Failed");
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