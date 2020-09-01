package com.example.testcodeamn.ViewModel;

import android.content.Context;

import com.example.testcodeamn.model.DetailTripModel;
import com.example.testcodeamn.model.MainModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailTripViewModel extends ViewModel {

    public MutableLiveData<String> detailTripStatus = new MutableLiveData<String>();
    public MutableLiveData<String> detailTripTempStatus = new MutableLiveData<String>();


    public void getDetailTrip(Context context, String id_business){
        DetailTripModel detailTrip = new DetailTripModel(context,id_business);
        final String dataDetailTrip = detailTrip.getDataRincian(id_business);
        System.out.println("@getDetailTrip "+dataDetailTrip);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataDetailTrip);
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
        System.out.println("@LOGIN: "+Response);
        detailTripStatus.postValue(Response);
    }

    public void getDetailTripTemp(Context context){
        DetailTripModel detailTripTemp = new DetailTripModel(context,"");
        final String dataDetailTripTemp = detailTripTemp.getDataRincianTemp();
        System.out.println("@getDetailTripTemp "+dataDetailTripTemp);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataDetailTripTemp);
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
        System.out.println("@getDetailTripTemp: "+Response);
        detailTripTempStatus.postValue(Response);
    }


}
