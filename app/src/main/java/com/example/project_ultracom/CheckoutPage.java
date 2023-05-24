package com.example.project_ultracom;

import static com.example.project_ultracom.DbContract.SERVER_CREATE_SERVICE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CheckoutPage extends AppCompatActivity {
    private Button btnGenerateID, btncheckout;
    private EditText id_service, jasa_service, jml_service, catatan_service, metode_pembayaran;
    Spinner spinner, pembayaran;
    String[] jasa = {"Laptop", "Printer", "Passbook", "Mesin Hitung Uang"};
    String[] payment = {"Cash", "Transfer"};

    AutoCompleteTextView dropdown;
    ArrayAdapter<String> adapterItems;

    String url;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        id_service = (EditText) findViewById(R.id.editText_idService);
        spinner = findViewById(R.id.dropdownJasa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckoutPage.this, android.R.layout.simple_spinner_item, jasa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pembayaran = findViewById(R.id.dropdownPayment);
        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(CheckoutPage.this, android.R.layout.simple_spinner_item, payment);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pembayaran.setAdapter(adaptor);

        pembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String payment = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        jasa_service = (EditText) findViewById(R.id.editText_jasaService);
        jml_service =(EditText) findViewById(R.id.editText_jmlService);
        catatan_service = (EditText) findViewById(R.id.editText_catatanService);
//        metode_pembayaran = (EditText) findViewById(R.id.editText_metodepembayaran);
        btnGenerateID = (Button) findViewById(R.id.btn_generateID);
        btncheckout = (Button) findViewById(R.id.btn_checkout);

        btnGenerateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aksi yang akan dijalankan ketika button di klik
                String generatedId = generateId();
                id_service.setText(generatedId);
            }
        });

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahData();
            }
        });
    }

    private void tambahData() {
        final String sId_service = id_service.getText().toString().trim();
        final String sJasa_service = spinner.getSelectedItem().toString().trim();
        final String sJml_service = jml_service.getText().toString().trim();
        final String sCatatan_service = catatan_service.getText().toString().trim();
        final String sMetode_pembayaran = pembayaran.getSelectedItem().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("id", null);

        if(TextUtils.isEmpty(sId_service)){
            id_service.setError("ID Service Tidak Boleh Kosong");
            id_service.requestFocus();
            return;
        }if(TextUtils.isEmpty(sJasa_service)){
            jasa_service.setError("Jasa Service Tidak Boleh Kosong");
            jasa_service.requestFocus();
            return;
        }if(TextUtils.isEmpty(sJml_service)){
            jml_service.setError("Jumlah Barang Tidak Boleh Kosong");
            jml_service.requestFocus();
            return;
        }if(TextUtils.isEmpty(sCatatan_service)){
            catatan_service.setError("Catatan Service Tidak Boleh Kosong");
            catatan_service.requestFocus();
            return;
        }if(TextUtils.isEmpty(sMetode_pembayaran)){
            metode_pembayaran.setError("Metode Pembayaran Tidak Boleh Kosong");
            metode_pembayaran.requestFocus();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_CREATE_SERVICE_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("User_data", response);
                Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                id_service.setText("");
//                jasa_service.setText("");
                jml_service.setText("");
                catatan_service.setText("");
                metode_pembayaran.setText("");
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
                params.put("id_service", sId_service);
                params.put("id", myId);
                params.put("nama_kategori", sJasa_service);
                params.put("jml_service", sJml_service);
                params.put("catatan_service", sCatatan_service);
                params.put("metode_pembayaran", sMetode_pembayaran);


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

    private String generateId() {
        // generate ID secara acak
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        return "ID-" + randomNumber;
    }
}