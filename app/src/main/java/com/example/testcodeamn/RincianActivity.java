package com.example.testcodeamn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testcodeamn.ViewModel.DetailTripViewModel;
import com.example.testcodeamn.ViewModel.RincianViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RincianActivity extends AppCompatActivity {
    EditText etNameRincian;
    EditText etDateRincian;
    EditText etCurrentRincian;
    ImageView imgRincian;
    CardView btnSaveRincian;
    String PATHIMAGES = "";
    private Uri mCropImageUri;
    SimpleDateFormat dateFormatter;

    RincianViewModel rincianViewModel;

    private ProgressDialog progressDialog = null;

    String IDBUSINESS;
    String USERID;
    String EMPLOYEE;
    String VISITDATE;
    String DESTINATION;
    String REQUESTFROM;
    String IDRINCIAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian);
        initUI();

        imgRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RincianActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(RincianActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(RincianActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2);
                }else {
                    CropImage.startPickImageActivity(RincianActivity.this);
                }
            }
        });

        btnSaveRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRincianData();
            }
        });

        etDateRincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                dateFormatter = new SimpleDateFormat("MM/dd/yy HH:mm");
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(RincianActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDateRincian.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

        rincianViewModel = ViewModelProviders.of(this).get(RincianViewModel.class);
        rincianViewModel.rincianDetailStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

        rincianViewModel.rincianStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            try{
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("result", result.getUri().toString());
                if (resultCode == RESULT_OK) {
                    imgRincian.setImageURI(result.getUri());
                    PATHIMAGES = result.getUri().getPath();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Cropping failed: " + result.toString(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Log.d("ErrorCropImage", e.toString());
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

        }else if (requestCode == 2){
            CropImage.startPickImageActivity(this);
        }else{
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(16,9)
                .setMaxCropResultSize(3072,2304)
                .start(this);
    }

    private void initUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Rincian");

        etNameRincian = findViewById(R.id.et_name_rincian);
        etDateRincian = findViewById(R.id.et_rincian_date);
        etCurrentRincian = findViewById(R.id.et_rincian_current);
        imgRincian = findViewById(R.id.img_rincian);
        btnSaveRincian = findViewById(R.id.btn_card_save_rincian);

        try {
            IDBUSINESS = getIntent().getStringExtra("id_business");
            USERID = getIntent().getStringExtra("user_id");
            EMPLOYEE = getIntent().getStringExtra("employee");
            VISITDATE = getIntent().getStringExtra("visit_date");
            DESTINATION = getIntent().getStringExtra("Destination");
        }catch (Exception e){

        }

        try {
            Log.d("userID",getIntent().getStringExtra("user_id"));
            Log.d("NameEmploye",getIntent().getStringExtra("name_employee"));
        }catch (Exception e){

        }

        REQUESTFROM = getIntent().getStringExtra("requestFrom");
    }

    private void addRincianData(){
        Random rand = new Random();
        int n =0;
        String code = "0";
        n	=rand.nextInt(999);
        String nu = String.valueOf(n);
        if(nu.length()<2){
            nu = "00"+nu;
        }else if(nu.length()<3){
            nu="0"+nu;
        }
        code =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        IDRINCIAN = code+nu;
        Log.d("IDRINCIAN",IDRINCIAN);
        String peruntukan = etNameRincian.getText().toString();
        String datetime = etDateRincian.getText().toString();
        String jumlah = etCurrentRincian.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(peruntukan) || peruntukan == "") {
            etNameRincian.setError("This field is required!");
            focusView = etNameRincian;
            cancel = true;
        }

        if (TextUtils.isEmpty(datetime) || datetime == "") {
            etDateRincian.setError("This field is required!");
            focusView = etDateRincian;
            cancel = true;
        }

        if (TextUtils.isEmpty(jumlah) || jumlah == "") {
            etCurrentRincian.setError("This field is required!");
            focusView = etCurrentRincian;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            if (TextUtils.isEmpty(PATHIMAGES) || PATHIMAGES == "") {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attantion!");
                builder.setMessage("Please choose image first");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }else {
                progressDialogShowing();
                if (REQUESTFROM.equals("0")){
                    rincianViewModel.addDetailRincianData(this,IDRINCIAN,IDBUSINESS,peruntukan,datetime,jumlah,PATHIMAGES);
                }else {
                    rincianViewModel.addRincianData(this,IDRINCIAN,"",peruntukan,datetime,jumlah,PATHIMAGES);
                }
            }
        }

    }

    public void Result(String Response){
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
                        if (REQUESTFROM.equals("0")){
                            gotoDetailTrip();
                        }else {
                            gotoAddTrip();
                        }
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

    public void gotoDetailTrip(){
        Intent intent = new Intent(RincianActivity.this, DetailTripActivity.class);
        intent.putExtra("id_business", IDBUSINESS);
        intent.putExtra("user_id", USERID);
        intent.putExtra("employee", EMPLOYEE);
        intent.putExtra("visit_date", VISITDATE);
        intent.putExtra("Destination", DESTINATION);
        startActivity(intent);
        finish();
    }

    public void gotoAddTrip(){
        Intent intent = new Intent(RincianActivity.this, AddNewTripActivity.class);
        intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
        intent.putExtra("name_employee", getIntent().getStringExtra("name_employee"));
        startActivity(intent);
        finish();
    }

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (REQUESTFROM.equals("0")){
                    gotoDetailTrip();
                }else {
                    gotoAddTrip();
                }
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
