package com.example.testcodeamn.ViewModel;

import android.content.Context;
import android.os.Handler;

import com.example.testcodeamn.model.RegisterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> registerStatus = new MutableLiveData<String>();

    public void addUserData(String email, String password, String phone, String firstname, String lastname, String dob, String gender, Context context){
        RegisterModel registerModel = new RegisterModel(email,password,phone,firstname,lastname,dob,gender,context);
        final int code = registerModel.addUserData(email,password,phone,firstname,lastname,dob,gender);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONArray arrayData = new JSONArray();
                JSONObject reqpart = new JSONObject();
                String msg = "";
                String Response;

                if(code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", "Register Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Register Failed, Your email have been exixts.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Register Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                registerStatus.postValue(Response);
            }
        }, 2000);
    }
}
