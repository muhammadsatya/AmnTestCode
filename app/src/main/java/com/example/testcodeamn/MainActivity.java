package com.example.testcodeamn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testcodeamn.ViewModel.MainViewModel;
import com.example.testcodeamn.objcet.Trip;
import com.example.testcodeamn.recycler.TripRecyclers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private CardView btnSearch;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private LinearLayout linTripNotFound;
    private ImageView imgAddNewTrip;
    private TextView tvSearch;
    private ProgressDialog progressDialog = null;
    MainViewModel mainViewModel;
    TripRecyclers tripRecyclers;
    List<Trip> tripList = new ArrayList<Trip>();

    String USERID;
    String NAME_EMPLOYEE;

    boolean statusSearch = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Business Trip");
        initUI();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewTripActivity.class);
                intent.putExtra("user_id", USERID);
                intent.putExtra("name_employee", NAME_EMPLOYEE);
                startActivity(intent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData();
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData();
            }
        });

        imgAddNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewTripActivity.class);
                intent.putExtra("user_id", USERID);
                intent.putExtra("name_employee", NAME_EMPLOYEE);
                startActivity(intent);
                finish();
            }
        });


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.sessionStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResultSession(response);
            }
        });

        mainViewModel.tripStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                ResultTrip(response);
            }
        });

        mainViewModel.tripSearchStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                statusSearch = true;
                ResultTrip(response);
            }
        });

        mainViewModel.logOutStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                resultLogout(response);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tripRecyclers = new TripRecyclers(this, tripList) {
            @Override
            public void onClickItem(Trip itemTrip) {
                Log.d("cekItem","enter");
                getDetail(itemTrip);
            }
        };
        recyclerView.setAdapter(tripRecyclers);

        getSession();
    }


    private void initUI(){
        fab = findViewById(R.id.fab);
        btnSearch = findViewById(R.id.btn_search);
        etSearch = findViewById(R.id.et_search);
        linTripNotFound = findViewById(R.id.lin_trip_not_found);
        imgAddNewTrip = findViewById(R.id.img_add_new_trip);
        tvSearch = findViewById(R.id.tv_search);
        recyclerView = findViewById(R.id.recycler_view);

    }


    public void ResultSession(String Response){
        try {
            String status = "";
            String message = "";
            String userId = "";
            String Name = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");
            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    userId = jso.getString("user_id");
                    Name = jso.getString("name");
                }

                USERID = userId;
                NAME_EMPLOYEE = Name;
            }else {
                USERID = userId;
                NAME_EMPLOYEE = Name;
            }

            getTrip();
        } catch (JSONException e) {
            USERID = "";
            NAME_EMPLOYEE = "";
            e.printStackTrace();
        }
    }

    public void ResultTrip(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            if (statusSearch){
                tripList.clear();
                tripRecyclers.notifyDataSetChanged();
            }


            if (status.equals("Success")){
                progressDialogDismiss();
                JSONArray jsa = new JSONArray(message);

                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    String id;
                    String userId;
                    String employee;
                    String visitDate;
                    String destination;

                    id = jso.getString("id");
                    userId = jso.getString("user_id");
                    employee = jso.getString("employee");
                    visitDate = jso.getString("visit_date");
                    destination = jso.getString("Destination");

                    Log.d("JSONCek", destination);

                    Trip trip = new Trip(id, userId, employee, visitDate, destination);
                    tripList.add(trip);
                    tripRecyclers.notifyDataSetChanged();
                }
                linTripNotFound.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }else {
                progressDialogDismiss();

                linTripNotFound.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resultLogout(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            if (status.equals("Success")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(status);
                builder.setMessage(message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
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


    public void getSession(){
        mainViewModel.checkSession(MainActivity.this);
    }

    public void searchData(){
        String search = etSearch.getText().toString();
        if (search.equals("")){
            getTrip();
        }else {
            progressDialogShowing();
            mainViewModel.getDataSearchTrip(this,USERID,search);
        }
    }

    public void getTrip(){
        progressDialogShowing();
        mainViewModel.getDataTrip(this,USERID);
    }

    public  void logOut(){
        progressDialogShowing();
        mainViewModel.logOut(this);
    }




    public void getDetail(Trip itemTrip){
        Intent intent = new Intent(MainActivity.this, DetailTripActivity.class);
        intent.putExtra("id_business", itemTrip.getId());
        intent.putExtra("user_id", itemTrip.getUser_id());
        intent.putExtra("employee", itemTrip.getEmployee());
        intent.putExtra("visit_date", itemTrip.getVisit_date());
        intent.putExtra("Destination", itemTrip.getDestination());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout){
            dialogLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (statusSearch){
            tripList.clear();
            tripRecyclers.notifyDataSetChanged();
            getTrip();
            statusSearch = false;
        }else {
            dialogExit();
        }

    }

    public void exitApp(){
        this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.up_to_bottom_out);
    }

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                exitApp();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void dialogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                logOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
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
