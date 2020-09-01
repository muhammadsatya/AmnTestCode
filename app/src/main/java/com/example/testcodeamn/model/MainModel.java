package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainModel {
    private sqLiteDb dbHelper;
    private String search;
    public MainModel(Context context, String userID, String search){
        this.search = search;
        dbHelper = new sqLiteDb(context);
    }

    public String getSearch() {
        return search;
    }

    public String getDataSession(){
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         JSONArray arrayData = new JSONArray();
         JSONObject reqpart = new JSONObject();
         int data;
         String msg = "";
         String dataJson = "";

         try {
             Cursor cursor = db.rawQuery("SELECT * FROM SESSION_LOGIN ",null);
             cursor.moveToFirst();
             data = cursor.getCount();

             if (data > 0){
                 JSONArray jsonArray = new JSONArray();
                 for (int i = 0; i<cursor.getCount(); i++){
                     JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                     jsonObject.put("user_id", cursor.getString(cursor.getColumnIndex("user_id")));
                     jsonObject.put("name", cursor.getString(cursor.getColumnIndex("firstname")) +" "+ cursor.getString(cursor.getColumnIndex("lastname")));
                     jsonArray.put(jsonObject);
                     dataJson = jsonArray + "";
                 }
                 reqpart.put("status", "0");
                 reqpart.put("message", dataJson);
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
             Log.d("ExceptionError",e.toString());
         }
         return msg;
     }

    public String getDataTrip(String userID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int dataDB;
        String msg = "";
        String data = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM BUSINESS_TRIP WHERE user_id='"+userID+"'",null);
            cursor.moveToFirst();
            dataDB = cursor.getCount();

            if (dataDB > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    Log.d("CheckMain",cursor.getString(cursor.getColumnIndex("id_business")));
                    jsonObject.put("id", cursor.getString(cursor.getColumnIndex("id_business")));
                    jsonObject.put("user_id", cursor.getString(cursor.getColumnIndex("user_id")));
                    jsonObject.put("employee", cursor.getString(cursor.getColumnIndex("employee")));
                    jsonObject.put("visit_date", cursor.getString(cursor.getColumnIndex("visit_date")));
                    jsonObject.put("Destination", cursor.getString(cursor.getColumnIndex("Destination")));
                    jsonArray.put(jsonObject);
                }
                data = jsonArray + "";
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
            Log.d("ExceptionError",e.toString());
        }
        return msg;
    }

    public String getDataSearchTrip(String userID, String search){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int dataDB;
        String msg = "";
        String data = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM BUSINESS_TRIP WHERE user_id='"+userID+"'"+" AND employee LIKE '"+search+"' OR Destination LIKE '"+search+"'",null);
            cursor.moveToFirst();
            dataDB = cursor.getCount();

            if (dataDB > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    Log.d("CheckMain",cursor.getString(cursor.getColumnIndex("id_business")));
                    jsonObject.put("id", cursor.getString(cursor.getColumnIndex("id_business")));
                    jsonObject.put("user_id", cursor.getString(cursor.getColumnIndex("user_id")));
                    jsonObject.put("employee", cursor.getString(cursor.getColumnIndex("employee")));
                    jsonObject.put("visit_date", cursor.getString(cursor.getColumnIndex("visit_date")));
                    jsonObject.put("Destination", cursor.getString(cursor.getColumnIndex("Destination")));
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

    public String logOut(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM SESSION_LOGIN");
            reqpart.put("status", "0");
            reqpart.put("message", "Logout Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Logout Failed");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorLogout",e.toString());
        }

        return msg;
    }
}