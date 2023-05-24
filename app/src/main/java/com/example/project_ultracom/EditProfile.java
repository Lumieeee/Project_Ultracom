package com.example.project_ultracom;

import static com.example.project_ultracom.DbContract.SERVER_CREATE_SERVICE_URL;
import static com.example.project_ultracom.DbContract.SERVER_UPDATE_USER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText etusername, etemail, etphone, etaddress, etpassword, etconfpassword;
    private Button btnUpdateSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etusername = (EditText) findViewById(R.id.editText_updateUsername);
        etemail = (EditText) findViewById(R.id.editText_updateEmail);
        etphone = (EditText) findViewById(R.id.editText_updatephone);
        etaddress = (EditText) findViewById(R.id.editText_updateaddress);
        etpassword = (EditText) findViewById(R.id.editText_updatePassword);
        etconfpassword = (EditText) findViewById(R.id.editText_updateconfirmPassword);
        btnUpdateSave = (Button) findViewById(R.id.btn_editprofile);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        etusername.setText(sharedPreferences.getString("username", null));
        etemail.setText(sharedPreferences.getString("email", null));
        etphone.setText(sharedPreferences.getString("phone", null));
        etaddress.setText(sharedPreferences.getString("address", null));
        etpassword.setText(sharedPreferences.getString("password", null));

        btnUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataUser();

            }
        });
    }

    private void updateDataUser() {
        final String sUsername = etusername.getText().toString().trim();
        final String sEmail = etemail.getText().toString().trim();
        final String sPhone = etphone.getText().toString().trim();
        final String sAddress = etaddress.getText().toString().trim();
        final String sPassword = etpassword.getText().toString().trim();
        final String sConfPassword = etconfpassword.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("id", null);

        if(TextUtils.isEmpty(sUsername)){
            etusername.setError("Username Tidak Boleh Kosong");
            etusername.requestFocus();
            return;
        }if(TextUtils.isEmpty(sEmail)){
            etemail.setError("Email Tidak Boleh Kosong");
            etemail.requestFocus();
            return;
        }if(TextUtils.isEmpty(sPhone)){
            etphone.setError("Nomor Handphone Tidak Boleh Kosong");
            etphone.requestFocus();
            return;
        }if(TextUtils.isEmpty(sAddress)){
            etaddress.setError("Alamat Tidak Boleh Kosong");
            etaddress.requestFocus();
            return;
        }if(TextUtils.isEmpty(sPassword)){
            etpassword.setError("Password Tidak Boleh Kosong");
            etpassword.requestFocus();
            return;
        }


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_UPDATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("User_data", response);
                        Toast.makeText(getApplicationContext(), "Data berhasil di Update", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User_data", "POS Data Gagal!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", myId);
                params.put("username", sUsername);
                params.put("email", sEmail);
                params.put("phone", sPhone);
                params.put("address", sAddress);
                params.put("password", sPassword);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}