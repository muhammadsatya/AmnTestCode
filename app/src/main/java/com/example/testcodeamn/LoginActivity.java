package com.example.testcodeamn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcodeamn.ViewModel.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText editUserName;
    private EditText editPassword;
    private TextView tvRegister;
    private CardView btnLogin;
    private ProgressDialog progressDialog = null;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginResult();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.loginStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                System.out.println("onChanged: "+response);
                Result(response);
            }
        });

        loginViewModel.sessionStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                checkLogin(response);
            }
        });

        loginViewModel.checkSession(this);
    }

    public void Result(String Response){
        try {
            String Status = "";
            String Message = "";
            String Code = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
                Code = jso.getString("code");
            }

            if (Code.equals("0")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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

                    }
                });
                builder.show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkLogin(String Response){
        try {
            String Status = "";
            String Message = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
           if (Message.equals("0")){
               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private void loginResult(){

        String userName = editUserName.getText().toString();
        String password = editPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            editUserName.setError("Invalid Email Format");
            focusView = editUserName;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName) || userName == "") {
            editUserName.setError("This field is required!");
            focusView = editUserName;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            editPassword.setError("This password is to short!");
            focusView = editPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialogShowing();
            loginViewModel.doLogin(userName, password, this);
        }
    }

    private void initUI(){
        editUserName = findViewById(R.id.et_email);
        editPassword = findViewById(R.id.et_password);
        tvRegister = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btn_card);
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
