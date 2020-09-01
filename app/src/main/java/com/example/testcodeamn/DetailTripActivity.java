package com.example.testcodeamn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testcodeamn.ViewModel.DetailTripViewModel;
import com.example.testcodeamn.objcet.Rincian;
import com.example.testcodeamn.objcet.Trip;
import com.example.testcodeamn.recycler.RincianRecyclers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailTripActivity extends AppCompatActivity {
    private TextView tvEmployeeDetail;
    private TextView tvVisitDateDetail;
    private TextView tvDestinationDetail;
    private TextView tvTotalPengeluaran;
    private RecyclerView recyclerRincianDetail;
    private FloatingActionButton fbAddRincian;
    private ProgressDialog progressDialog = null;

    RincianRecyclers rincianRecyclers;
    List<Rincian> rinciansList = new ArrayList<Rincian>();

    DetailTripViewModel detailTripViewModel;

    String IDBUSINESS;
    String USERID;
    String EMPLOYEE;
    String VISITDATE;
    String DESTINATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Business Trip Detail");
        initUI();


        fbAddRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailTripActivity.this, RincianActivity.class);
                intent.putExtra("id_business", IDBUSINESS);
                intent.putExtra("user_id", USERID);
                intent.putExtra("employee", EMPLOYEE);
                intent.putExtra("visit_date", VISITDATE);
                intent.putExtra("Destination", DESTINATION);
                intent.putExtra("requestFrom","0");
                startActivity(intent);
                finish();
            }
        });
        detailTripViewModel = ViewModelProviders.of(this).get(DetailTripViewModel.class);
        detailTripViewModel.detailTripStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResulDetail(response);
            }
        });


        recyclerRincianDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rincianRecyclers = new RincianRecyclers(this, rinciansList) {
            @Override
            public void onDeleteItem(Rincian itemTrip, int position) {

            }
        };

        recyclerRincianDetail.setAdapter(rincianRecyclers);

        getDetailTrip();
    }

    private void initUI(){
        tvEmployeeDetail = findViewById(R.id.tv_employee_detail);
        tvVisitDateDetail = findViewById(R.id.tv_visit_date_detail);
        tvDestinationDetail = findViewById(R.id.tv_destination_detail);
        tvTotalPengeluaran = findViewById(R.id.tv_total_pengeluaran);
        fbAddRincian = findViewById(R.id.fab_add_rincian_detail);
        recyclerRincianDetail = findViewById(R.id.recycler_detail_trip);


        IDBUSINESS = getIntent().getStringExtra("id_business");
        USERID = getIntent().getStringExtra("user_id");
        EMPLOYEE = getIntent().getStringExtra("employee");
        VISITDATE = getIntent().getStringExtra("visit_date");
        DESTINATION = getIntent().getStringExtra("Destination");

        tvEmployeeDetail.setText(EMPLOYEE);
        tvVisitDateDetail.setText(VISITDATE);
        tvDestinationDetail.setText(DESTINATION);


    }

    public void getDetailTrip(){
        progressDialogShowing();
        detailTripViewModel.getDetailTrip(this,IDBUSINESS);
    }

    public void ResulDetail(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            int total = 0;

            if (status.equals("Success")){
                progressDialogDismiss();
                JSONArray jsa = new JSONArray(message);
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    String id;
                    String idBusiness;
                    String peruntukan;
                    String dateTime;
                    String jumlah;
                    String image;

                    id = jso.getString("id");
                    idBusiness = jso.getString("id_business");
                    peruntukan = jso.getString("peruntukan");
                    dateTime = jso.getString("date_time");
                    jumlah = jso.getString("jumlah");
                    image = jso.getString("image");

                    total = total + Integer.parseInt(jumlah);

                    Rincian rincian = new Rincian(id, idBusiness, peruntukan, dateTime, jumlah, image, 0);
                    rinciansList.add(rincian);
                    rincianRecyclers.notifyDataSetChanged();
                }

                String totalRincian="";
                try {
                    double dprice = Double.parseDouble(String.valueOf(total));
                    DecimalFormat formatter2 = new DecimalFormat("#,###");
                    String AB = String.valueOf(formatter2.format(dprice));
                    if (AB.startsWith(".")) AB = "0" + AB;
                    totalRincian = AB;
                }catch(Exception e){
                    totalRincian = String.valueOf(total);
                }
                tvTotalPengeluaran.setText("Rp. "+totalRincian);
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(status);
                builder.setMessage(message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailTripActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
}
