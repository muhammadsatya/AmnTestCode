package com.example.testcodeamn.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcodeamn.db.sqLiteDb;

public class RegisterModel {
    private String email;
    private String password;
    private String phone;
    private String firstname;
    private String lastname;
    private String dob;
    private String gender;
    private sqLiteDb dbHelper;

    public RegisterModel(String email, String password, String phone, String firstname, String lastname, String dob, String gender, Context context){
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        dbHelper = new sqLiteDb(context);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public int addUserData(String email, String password, String phone, String firstname, String lastname, String dob, String gender){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE email ='" + email +"'",null);
            cursor.moveToFirst();
             data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO USER (" +
                        "email, " +
                        "password, " +
                        "phone, " +
                        "firstname, " +
                        "lastname, " +
                        "birthday , " +
                        "gender " +
                        ") VALUES"
                        + "('" + email + "',"
                        + "'" + password + "',"
                        + "'" + phone + "',"
                        + "'" + firstname + "',"
                        + "'" + lastname + "',"
                        + "'" + dob + "',"
                        + "'" + gender + "');";
                db.execSQL(sqlinsert);
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
