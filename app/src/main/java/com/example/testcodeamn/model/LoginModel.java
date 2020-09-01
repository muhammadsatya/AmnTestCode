package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;

public class LoginModel {
    private String username;
    private String password;
    private sqLiteDb dbHelper;
    public LoginModel(String username, String password, Context context){
        this.username = username;
        this.password = password;
        dbHelper = new sqLiteDb(context);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



     public int checkUserValidity(String username, String password){
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         int data;
         int status = 0;

         try {
             Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE email ='" + username +"'",null);
             cursor.moveToFirst();
             data = cursor.getCount();

             if (data > 0){
                 for (int i = 0; i<cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    if (password.equals(cursor.getString(cursor.getColumnIndex("password")))){
                        try {
                            String sqlinsert = "INSERT INTO SESSION_LOGIN (" +
                                    "id, " +
                                    "user_id, " +
                                    "username, " +
                                    "firstname, " +
                                    "lastname, " +
                                    "phone " +
                                    ") VALUES"
                                    + "('" + 1 + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("user_id")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("email")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("firstname")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("lastname")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("phone")) + "');";
                            db.execSQL(sqlinsert);
                            status = 0;
                        }catch (Exception e){
                            Log.d("ExceptionErrorInsert",e.toString());
                        }
                    }else {
                        status = -1;
                    }
                 }
             }else {
                 status = -2;
             }
             cursor.close();

         }catch (Exception e){
             status = -3;
             Log.d("ExceptionError",e.toString());
         }
         return status;
     }

     public int checkSession(){
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         int data;
         int status = 0;

         try {
             Cursor cursor = db.rawQuery("SELECT * FROM SESSION_LOGIN ",null);
             cursor.moveToFirst();
             data = cursor.getCount();

             if (data > 0){
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
}