package com.example.testcodeamn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.testcodeamn.ViewModel.RegisterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    AutoCompleteTextView atEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etPhone;
    EditText etFname, etLname;
    EditText etDob;
    RadioGroup rdGender;
    RadioButton rdTypeGender;
    CardView btnRegister;
    TextView tvToLogin;
    RegisterViewModel registerViewModel;
    ImageView imgShowPass, imgShowConfPass;
    private ProgressDialog progressDialog = null;

    Boolean isShowPass = false;
    Boolean isShowConfPass = false;

    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerResult();
            }
        });

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.registerStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShowPass) {
                    imgShowPass.setImageResource(R.drawable.ic_visibility);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShowPass = false;
                } else {
                    imgShowPass.setImageResource(R.drawable.ic_visibility_off);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    isShowPass = true;
                }
            }
        });

        imgShowConfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShowConfPass) {
                    imgShowConfPass.setImageResource(R.drawable.ic_visibility);
                    etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShowConfPass = false;
                } else {
                    imgShowConfPass.setImageResource(R.drawable.ic_visibility_off);
                    etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    isShowConfPass = true;
                }
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy");
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDob.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

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
            progressDialogDismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle(Status);
            builder.setMessage(Message);
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private void registerResult(){
        String email = atEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String fName = etFname.getText().toString();
        String lName = etLname.getText().toString();
        String dob = etDob.getText().toString();
        String gender = rdTypeGender.getText().toString();


        boolean cancel = false;
        View focusView = null;

        Pattern p = Pattern.compile("^[a-zA-Z' ]*$");
        Matcher m1,m2 ;
        m1 = p.matcher(fName);
        m2 = p.matcher(lName);

        if(!m1.find()) {
            etFname.setError("This field is required!");
            focusView = etFname;
            cancel = true;
        }

        if(!m2.find()) {
            etLname.setError("This field is required!");
            focusView = etLname;
            cancel = true;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            atEmail.setError("Invalid Email Format");
            focusView = atEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(email) || email == "") {
            atEmail.setError("This field is required!");
            focusView = atEmail;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            etPassword.setError("This password is to short!");
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(confirmPassword) && !isPasswordValid(confirmPassword)) {
            etConfirmPassword.setError("This password is to short!");
            focusView = etConfirmPassword;
            cancel = true;
        }


        if (etConfirmPassword.equals(password)) {
            etConfirmPassword.setError("Confirm password is not matched!");
            focusView = etConfirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone) || phone == "") {
            etPhone.setError("This field is required!");
            focusView = etPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(fName) || fName == "") {
            etFname.setError("This field is required!");
            focusView = etFname;
            cancel = true;
        }

        if (TextUtils.isEmpty(lName) || lName == "") {
            etLname.setError("This field is required!");
            focusView = etLname;
            cancel = true;
        }

        if (TextUtils.isEmpty(dob) || dob == "") {
            etDob.setError("This field is required!");
            focusView = etDob;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialogShowing();
            registerViewModel.addUserData(email,password,phone,fName,lName,dob,gender,this);
        }
    }

    private void initUI(){
        atEmail = findViewById(R.id.at_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etPhone = findViewById(R.id.et_phone);
        etFname = findViewById(R.id.et_fname);
        etLname = findViewById(R.id.et_lname);
        etDob = findViewById(R.id.et_dob);
        rdGender = findViewById(R.id.rd_gender);
        int genderId = rdGender.getCheckedRadioButtonId();
        rdTypeGender = findViewById(genderId);
        btnRegister = findViewById(R.id.btn_card_register);
        tvToLogin = findViewById(R.id.tv_to_login);
        imgShowPass = findViewById(R.id.img_show_pass);
        imgShowConfPass = findViewById(R.id.img_show_conf_pass);
    }

    @Override
    public void onBackPressed() {
        dialogExit();
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
