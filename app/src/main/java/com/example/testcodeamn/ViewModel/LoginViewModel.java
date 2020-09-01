package com.example.testcodeamn.ViewModel;

import android.content.Context;
import android.os.Handler;

import com.example.testcodeamn.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> loginStatus = new MutableLiveData<String>();
    public MutableLiveData<String> sessionStatus = new MutableLiveData<String>();


    public void doLogin(String userName, String password, Context context) {
        LoginModel loginModel = new LoginModel(userName, password, context);
        final int code = loginModel.checkUserValidity(userName,password);
        System.out.println("@Code "+code);

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
                        reqpart.put("message", "Login Successfully");
                        reqpart.put("code", "0");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Email or Password wrong.");
                        reqpart.put("code", "-1");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (code == -2){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Your email have not be exixts.");
                        reqpart.put("code", "-2");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Something went wrong.");
                        reqpart.put("code", "-3");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;
                System.out.println("@LOGIN: "+Response);

                loginStatus.postValue(Response);
            }
        }, 2000);
    }

    public void checkSession(Context context){
        LoginModel loginModel = new LoginModel("", "", context);
        final int code = loginModel.checkSession();
        System.out.println("@Code "+code);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        if(code == 0) {
            try {
                reqpart.put("status", "Success");
                reqpart.put("message", "0");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (code == -1){
            try {
                reqpart.put("status", "Success");
                reqpart.put("message", "-1");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                reqpart.put("status", "Success");
                reqpart.put("message", "-2");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Response = msg;
        System.out.println("@LOGIN: "+Response);
        sessionStatus.postValue(Response);
    }
}
