package com.example.project_ultracom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText username, email, phone, address, password, confPassword;
    Button register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        username = (EditText) findViewById(R.id.editText_usernameregister);
        email = (EditText) findViewById(R.id.editText_emailregister);
        phone = (EditText) findViewById(R.id.editText_phoneregister);
        address = (EditText) findViewById(R.id.editText_addressregister);
        password = (EditText) findViewById(R.id.editText_Password);
        confPassword = (EditText) findViewById(R.id.editText_confPassword);
        register = (Button) findViewById(R.id.btn_signUp);
        progressDialog = new ProgressDialog(RegisterPage.this);


        TextView tv = (TextView)findViewById(R.id.txt_btn_signIn);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUsername = username.getText().toString();
                String sEmail = email.getText().toString();
                String sPhone = phone.getText().toString();
                String sAddress = address.getText().toString();
                String sPassword = password.getText().toString();
                String sConfPassword = confPassword.getText().toString();

                if(TextUtils.isEmpty(sUsername)){
                    username.setError("Username Tidak Boleh Kosong");
                    username.requestFocus();
                    return;
                }if(TextUtils.isEmpty(sEmail)) {
                    email.setError("Email Tidak Boleh Kosong");
                    email.requestFocus();
                    return;
                }if(TextUtils.isEmpty(sPhone)) {
                    phone.setError("Nomor Handphone Tidak Boleh Kosong");
                    phone.requestFocus();
                    return;
                }if(TextUtils.isEmpty(sAddress)) {
                    address.setError("Alamat Tidak Boleh Kosong");
                    address.requestFocus();
                    return;
                }if(TextUtils.isEmpty(sPassword)) {
                    password.setError("Password Tidak Boleh Kosong");
                    password.requestFocus();
                    return;
                }

                if (sPassword.equals(sConfPassword) && !sPassword.equals("")){
                    CreateDataToServer(sUsername, sEmail, sPhone, sAddress, sPassword);
                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal! Password Tidak Cocok", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CreateDataToServer(final String username, final String email, final String phone, final String address, final String password){
        if (checkNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"OK\"}]")){
                                    sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", username);
                                    editor.putString("email", email);
                                    editor.putString("phone", phone);
                                    editor.putString("address", address);
                                    editor.putString("password", password);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("address", address);
                    params.put("password", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(RegisterPage.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}