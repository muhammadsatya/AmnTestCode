package com.example.testcodeamn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testcodeamn.ViewModel.AddTripViewModel;
import com.example.testcodeamn.ViewModel.DetailTripViewModel;
import com.example.testcodeamn.ViewModel.RincianViewModel;
import com.example.testcodeamn.objcet.Rincian;
import com.example.testcodeamn.objcet.Trip;
import com.example.testcodeamn.recycler.RincianRecyclers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddNewTripActivity extends AppCompatActivity {

    TextView tvEmployee;
    EditText etVisitDate;
    EditText etDestination;
    RecyclerView recyclerRincian;
    LinearLayout linRincianNotFound;
    ImageView imgAddNewRincian;
    FloatingActionButton fabAddRincian;
    CardView btnCardSaveTrip;
    private ProgressDialog progressDialog = null;

    String USERID;
    String NAME_EMPLOYEE;
    String IDBUSINESS = "0";

    SimpleDateFormat dateFormatter;

    RincianRecyclers rincianRecyclers;
    List<Rincian> rinciansList = new ArrayList<Rincian>();

    AddTripViewModel addTripViewModel;
    DetailTripViewModel detailTripViewModel;
    RincianViewModel rincianViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        initUI();

        fabAddRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewTripActivity.this, RincianActivity.class);
                intent.putExtra("user_id",USERID);
                intent.putExtra("name_employee",NAME_EMPLOYEE);
                intent.putExtra("requestFrom","-1");
                startActivity(intent);
                finish();
            }
        });

        imgAddNewRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewTripActivity.this, RincianActivity.class);
                intent.putExtra("user_id",USERID);
                intent.putExtra("name_employee",NAME_EMPLOYEE);
                intent.putExtra("requestFrom","-1");
                startActivity(intent);
                finish();
            }
        });

        etVisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddNewTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etVisitDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

        btnCardSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTrip();
            }
        });

        detailTripViewModel = ViewModelProviders.of(this).get(DetailTripViewModel.class);
        detailTripViewModel.detailTripTempStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResulRincian(response);
            }
        });

        addTripViewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);
        addTripViewModel.addTripStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResultInsertTrip(response);
            }
        });


        addTripViewModel.addDetailTripStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResultInsertDetail(response);
            }
        });

        addTripViewModel.deleteStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });

        rincianViewModel = ViewModelProviders.of(this).get(RincianViewModel.class);
        rincianViewModel.rincianDeleteStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResultDeleteTemp(response);
            }
        });



        recyclerRincian.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rincianRecyclers = new RincianRecyclers(this, rinciansList) {
            @Override
            public void onDeleteItem(Rincian itemTrip, int position) {
                String id = itemTrip.getId();
                rinciansList.remove(position);
                rincianRecyclers.notifyDataSetChanged();
                deleteItem(id);
                if (rinciansList.size() == 0){
                    linRincianNotFound.setVisibility(View.VISIBLE);
                    fabAddRincian.setVisibility(View.GONE);
                }
            }
        };

        recyclerRincian.setAdapter(rincianRecyclers);

        getRincianTemp();
    }

    private void initUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("New Trip");

        tvEmployee = findViewById(R.id.tv_name_employee);
        etVisitDate = findViewById(R.id.et_visit_date);
        etDestination = findViewById(R.id.et_destination);
        recyclerRincian = findViewById(R.id.recycler_rincian);
        fabAddRincian = findViewById(R.id.fab_add_rincian);
        linRincianNotFound = findViewById(R.id.lin_rincian_not_found);
        imgAddNewRincian = findViewById(R.id.img_add_new_rincian);
        btnCardSaveTrip = findViewById(R.id.btn_card_save_trip);

        USERID = getIntent().getStringExtra("user_id");
        NAME_EMPLOYEE = getIntent().getStringExtra("name_employee");

        tvEmployee.setText(NAME_EMPLOYEE);


    }

    public void getRincianTemp(){
        detailTripViewModel.getDetailTripTemp(this);
    }

    public void addNewTrip(){
        Random rand = new Random();
        int n =0;
        String code = "0";
        n	=rand.nextInt(99);
        String nu = String.valueOf(n);
        if(nu.length()<2){
            nu = "00"+nu;
        }else if(nu.length()<3){
            nu="0"+nu;
        }
        code =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        IDBUSINESS = code+nu;
        Log.d("IDBUSINESS",IDBUSINESS);
        String employee = tvEmployee.getText().toString();
        String visitdate = etVisitDate.getText().toString();
        String destination = etDestination.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(employee) || employee == "") {
            tvEmployee.setError("This field is required!");
            focusView = tvEmployee;
            cancel = true;
        }

        if (TextUtils.isEmpty(visitdate) || visitdate == "") {
            etVisitDate.setError("This field is required!");
            focusView = etVisitDate;
            cancel = true;
        }

        if (TextUtils.isEmpty(destination) || destination == "") {
            etDestination.setError("This field is required!");
            focusView = etDestination;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (rinciansList.size() > 0){
                progressDialogShowing();
                addTripViewModel.addTripData(this, IDBUSINESS, USERID,employee,visitdate,destination);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attantion!");
                builder.setMessage("Please add rincian pengeluaran first");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }

        }

    }

    public void deleteItem(String id){
        addTripViewModel.deleteItem(this, id);
    }

    public void deleteTemp(){
        rincianViewModel.deleteTemp(this);
    }

    public void ResulRincian(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");
            rincianRecyclers.notifyDataSetChanged();
            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                linRincianNotFound.setVisibility(View.GONE);
                fabAddRincian.setVisibility(View.VISIBLE);
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    String id;
                    String idBusiness;
                    String peruntukan;
                    String dateTime;
                    String jumlah;
                    String image;

                    id = jso.getString("id");
                    peruntukan = jso.getString("peruntukan");
                    dateTime = jso.getString("date_time");
                    jumlah = jso.getString("jumlah");
                    image = jso.getString("image");

                    Rincian rincian = new Rincian(id, "", peruntukan, dateTime, jumlah, image, -1);
                    rinciansList.add(rincian);
                    rincianRecyclers.notifyDataSetChanged();
                }

                if (rinciansList.size() > 0){
                    btnCardSaveTrip.setVisibility(View.VISIBLE);
                }
            }else {
                linRincianNotFound.setVisibility(View.VISIBLE);
                fabAddRincian.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ResultInsertTrip(String Response){
        try {
            String Status = "";
            String Message = "";
            String data = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0 ; i < rinciansList.size(); i++){
                    JSONObject jsonObject = new JSONObject();
                    Rincian rincian =  rinciansList.get(i);
                    jsonObject.put("id",rincian.getId());
                    jsonObject.put("id_business",IDBUSINESS);
                    jsonObject.put("peruntukan",rincian.getPeruntukan());
                    jsonObject.put("date_time",rincian.getDate_time());
                    jsonObject.put("jumlah",rincian.getJumlah());
                    jsonObject.put("image",rincian.getImage());
                    jsonArray.put(jsonObject);
                }
                data = jsonArray + "";
                addTripViewModel.addTripDetailData(this, data);
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void ResultInsertDetail(String Response){
        try {
            String Status = "";
            String Message = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                deleteTemp();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void ResultDeleteTemp(String Response){
        try {
            String Status = "";
            String Message = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(AddNewTripActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void progressDialogShowing() {
        progressDialogDismiss();
        progressDialog = null;
        progressDialog = ProgressDialog.show(this, "", "Loading...");
    }

    public void progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (rinciansList.size() > 0){
                    deleteTemp();
                }
                Intent intent = new Intent(AddNewTripActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }
}
