package com.example.testcodeamn.ViewModel;

import android.content.Context;

import com.example.testcodeamn.model.MainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public MutableLiveData<String> sessionStatus = new MutableLiveData<String>();
    public MutableLiveData<String> tripStatus = new MutableLiveData<String>();
    public MutableLiveData<String> tripSearchStatus = new MutableLiveData<String>();
    public MutableLiveData<String> logOutStatus = new MutableLiveData<String>();

    public void checkSession(Context context){
        MainModel loginModel = new MainModel(context,"", "");
        final String dataSession = loginModel.getDataSession();
        System.out.println("@CheckSession "+dataSession);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String message = "";
        String Response;

        String userId = "";
        String name = "";
        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataSession);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (Code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
        Response = msg;
        System.out.println("@CheckSession: "+Response);
        sessionStatus.postValue(Response);
    }

    public void getDataTrip(Context context, String userID){
        MainModel loginModel = new MainModel(context,"","");
        final String dataTrip = loginModel.getDataTrip(userID);
        System.out.println("@getDataTrip "+dataTrip);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataTrip);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                }else if (Code == -1){
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                }else {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                }
            }
        }catch (Exception e){
            try {
                reqpart.put("status", "Failed");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }catch (Exception i){

            }
        }
        Response = msg;
        System.out.println("@getDataTrip: "+Response);
        tripStatus.postValue(Response);
    }

    public void getDataSearchTrip(Context context, String userID, String search){
        MainModel loginModel = new MainModel(context,userID,search);
        final String dataTrip = loginModel.getDataSearchTrip(userID,search);
        System.out.println("@getDataSearchTrip "+dataTrip);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataTrip);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                }else if (Code == -1){
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                }else {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                reqpart.put("status", "Failed");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }catch (Exception i){

            }
        }
        Response = msg;
        System.out.println("@getDataSearchTrip: "+Response);
        tripSearchStatus.postValue(Response);
    }

    public void logOut(Context context){
        MainModel logOutModel = new MainModel(context,"","");
        final String logOut = logOutModel.logOut();
        System.out.println("@Logout "+logOut);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(logOut);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    reqpart.put("status", "Success");
                    reqpart.put("message", message);
                    arrayData.put(reqpart);
                    msg = arrayData + "";
                }else {
                    reqpart.put("status", "Failed");
                    reqpart.put("message", message);
                    arrayData.put(reqpart);
                    msg = arrayData + "";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                reqpart.put("status", "Failed");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }catch (Exception i){

            }
        }
        Response = msg;
        System.out.println("@Logout: "+Response);
        logOutStatus.postValue(Response);
    }
}
