package com.example.testcodeamn.ViewModel;

import android.content.Context;
import android.os.Handler;

import com.example.testcodeamn.model.AddTripModel;
import com.example.testcodeamn.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTripViewModel extends ViewModel {

    public MutableLiveData<String> addTripStatus = new MutableLiveData<String>();
    public MutableLiveData<String> addDetailTripStatus = new MutableLiveData<String>();
    public MutableLiveData<String> deleteStatus = new MutableLiveData<String>();


    public void addTripData(Context context, String idBusiness, String userId, String employee, String visitDate, String destination){
        AddTripModel addTripModel = new AddTripModel(context, idBusiness, userId, employee, visitDate, destination, "");
        final int code = addTripModel.addTripData(idBusiness, userId, employee, visitDate, destination);

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
                        reqpart.put("message", "Save Data Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Save Data Failed, This data already exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Save Data Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Trip: "+Response);

                addTripStatus.postValue(Response);
            }
        }, 500);
    }

    public void addTripDetailData(Context context, String dataJson){
        AddTripModel addTripModel = new AddTripModel(context, "", "", "", "", "", dataJson);
        final int code = addTripModel.addDetailRincianData(dataJson);

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
                        reqpart.put("message", "Save Data Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Save Data Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Detail: "+Response);

                addDetailTripStatus.postValue(Response);
            }
        }, 500);
    }

    public void deleteItem(Context context, String id){
        AddTripModel addTripModel = new AddTripModel(context, "", "", "", "", "", id);
        final String delTemp = addTripModel.deleteItem(id);
        System.out.println("@deleteItem "+delTemp);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(delTemp);
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
        System.out.println("@Delete: "+Response);
        deleteStatus.postValue(Response);
    }

}
