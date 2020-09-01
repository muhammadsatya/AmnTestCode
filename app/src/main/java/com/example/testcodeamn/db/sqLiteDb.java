package com.example.testcodeamn.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class sqLiteDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test_code.db";
    private static final int DATABASE_VERSION = 1;


    public String USER = "CREATE TABLE IF NOT EXISTS USER( " +
            "user_id integer primary key AUTOINCREMENT, " +
            "email text null, " +
            "password text null, " +
            "phone text null, " +
            "firstname text null," +
            "lastname text null, " +
            "birthday text null," +
            "gender text null)";

    public String SESLOG = "CREATE TABLE IF NOT EXISTS SESSION_LOGIN( "
            + " id integer primary key ,"
            + " user_id text null,"
            + " username text null,"
            + " firstname text null,"
            + " lastname text null,"
            + " phone text null);";

    public String BUSINESS = "CREATE TABLE IF NOT EXISTS BUSINESS_TRIP( "
            + " id_business integer primary key ,"
            + " user_id text null,"
            + " employee text null,"
            + " visit_date text null,"
            + " Destination text null);";

    public String RINCIAN = "CREATE TABLE IF NOT EXISTS RINCIAN( "
            + " id_rincian integer primary key ,"
            + " id_business text null,"
            + " peruntukan text null,"
            + " date_time text null,"
            + " jumlah text null,"
            + " image text null);";

    public String RINCIAN_TEMP = "CREATE TABLE IF NOT EXISTS RINCIAN_TEMP( "
            + " id_rincian integer primary key ,"
            + " peruntukan text null,"
            + " date_time text null,"
            + " jumlah text null,"
            + " image text null);";

    public sqLiteDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER);
        db.execSQL(SESLOG);
        db.execSQL(BUSINESS);
        db.execSQL(RINCIAN);
        db.execSQL(RINCIAN_TEMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close(SQLiteDatabase db) {
        db.close();
    }
}
