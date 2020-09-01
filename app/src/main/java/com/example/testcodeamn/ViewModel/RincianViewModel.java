package com.example.testcodeamn.ViewModel;

import android.content.Context;
import android.os.Handler;

import com.example.testcodeamn.model.RegisterModel;
import com.example.testcodeamn.model.RincianModel;
import com.example.testcodeamn.objcet.Rincian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RincianViewModel extends ViewModel {
    public MutableLiveData<String> rincianStatus = new MutableLiveData<String>();
    public MutableLiveData<String> rincianDetailStatus = new MutableLiveData<String>();
    public MutableLiveData<String> rincianDeleteStatus = new MutableLiveData<String>();

    public void addRincianData(Context context, String idrincian, String idbusiness, String peruntukan, String datetime, String jumlah, String image){
        RincianModel rincianModel = new RincianModel(context, idrincian, idbusiness, peruntukan, datetime, jumlah, image);
        final int code = rincianModel.addRincianData(idrincian, peruntukan, datetime, jumlah, image);

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
                        reqpart.put("message", "Save Data Failed, This data have been exixts.");
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

                System.out.println("@LOGIN: "+Response);

                rincianStatus.postValue(Response);
            }
        }, 2000);
    }

    public void addDetailRincianData(Context context, String idrincian, String idbusiness, String peruntukan, String datetime, String jumlah, String image){
        RincianModel rincianModel = new RincianModel(context, idrincian, idbusiness, peruntukan, datetime, jumlah, image);
        final int code = rincianModel.addDetailRincianData(idrincian, idbusiness, peruntukan, datetime, jumlah, image);

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
                        reqpart.put("message", "Save Data Failed, This data have been exixts.");
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

                System.out.println("@LOGIN: "+Response);

                rincianDetailStatus.postValue(Response);
            }
        }, 2000);
    }


    public void deleteTemp(Context context){
        RincianModel delTemp = new RincianModel(context,"","","","","","");
        final String delete = delTemp.deleteTempDate();
        System.out.println("@DeleteTemp "+delete);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(delete);
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
        System.out.println("@DeleteTemp: "+Response);
        rincianDeleteStatus.postValue(Response);
    }
}
