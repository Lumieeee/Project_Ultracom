package com.example.project_ultracom;

import static com.example.project_ultracom.DbContract.SERVER_GET_DATA_URL;
import static com.example.project_ultracom.DbContract.SERVER_UPDATE_SERVICE_URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditCheckoutPage extends AppCompatActivity {
    EditText edtId, edtjasa, edtjml, edtcatatan, edtmetode;
    Button btn_updateCheckout, btn_wa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_checkout_page);

        edtId = findViewById(R.id.editText_editidService);
        edtjasa = findViewById(R.id.editText_editjasaService);
        edtjml = findViewById(R.id.editText_editjmlService);
        edtcatatan = findViewById(R.id.editText_editcatatanService);
        edtmetode = findViewById(R.id.editText_editmetodepembayaran);
        btn_updateCheckout = findViewById(R.id.btn_editcheckout);
        btn_wa = findViewById(R.id.btn_wa);

        getData();

        btn_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Halo, apakah Service dengan "+edtId.getText().toString()+" sudah di proses?";
                String wpul = "https://wa.me/+6282131802740?text="+ message;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wpul));
                startActivity(intent);
            }
        });

        btn_updateCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCheckout();
            }

        });
    }

    private void updateCheckout() {
        final String sEdtIdService = edtId.getText().toString().trim();
        final String sEdtJasaService = edtjasa.getText().toString().trim();
        final String sEdtJml = edtjml.getText().toString().trim();
        final String sEdtcatatan = edtcatatan.getText().toString().trim();
        final String sEdtMetode = edtmetode.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("id", null);

        if(TextUtils.isEmpty(sEdtIdService)){
            edtId.setError("ID Service Tidak Boleh Kosong");
            edtId.requestFocus();
            return;
        }if(TextUtils.isEmpty(sEdtJasaService)){
            edtjasa.setError("Jasa Service Tidak Boleh Kosong");
            edtjasa.requestFocus();
            return;
        }if(TextUtils.isEmpty(sEdtJml)){
            edtjml.setError("Jumlah Barang Tidak Boleh Kosong");
            edtjml.requestFocus();
            return;
        }if(TextUtils.isEmpty(sEdtcatatan)){
            edtcatatan.setError("Catatan Service Tidak Boleh Kosong");
            edtcatatan.requestFocus();
            return;
        }if(TextUtils.isEmpty(sEdtMetode)){
            edtmetode.setError("Metode Pembayaran Tidak Boleh Kosong");
            edtmetode.requestFocus();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_UPDATE_SERVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("User_data", response);
                        Intent intent = new Intent(EditCheckoutPage.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Data berhasil di Update", Toast.LENGTH_SHORT).show();
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
                params.put("id_service", sEdtIdService);
                params.put("id", myId);
                params.put("nama_kategori", sEdtJasaService);
                params.put("jml_service", sEdtJml);
                params.put("catatan_service", sEdtcatatan);
                params.put("metode_pembayaran", sEdtMetode);


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

    private void getData(){
        StringRequest request = new StringRequest(Request.Method.POST, SERVER_GET_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                            String sid_service = jsonObject.getString("id_service");
                            String sid = jsonObject.getString("id");
                            String sjasa = jsonObject.getString("nama_kategori");
                            String sjml = jsonObject.getString("jml_service");
                            String scatatan = jsonObject.getString("catatan_service");
                            String smetode = jsonObject.getString("metode_pembayaran");

                            edtId.setText(sid_service);
                            edtjasa.setText(sjasa);
                            edtjml.setText(sjml);
                            edtcatatan.setText(scatatan);
                            edtmetode.setText(smetode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> form = new HashMap<String,String>();
                form.put("id_service", getIntent().getStringExtra("updateCheckout"));
                return form;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}