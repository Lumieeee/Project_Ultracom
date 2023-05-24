package com.example.project_ultracom;

import static com.example.project_ultracom.DbContract.SERVER_CREATE_SERVICE_URL;
import static com.example.project_ultracom.DbContract.SERVER_DELETE_SERVICE_URL;
import static com.example.project_ultracom.DbContract.SERVER_READ_SERVICE_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_ultracom.Adapter.Adaptor;
import com.example.project_ultracom.Adapter.GetData;
import com.example.project_ultracom.model.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {
    ListView listView;
    Adaptor adaptor;
    ArrayList<GetData> model;

    public HistoryFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listView = view.findViewById(R.id.list);
        load_data();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_opsi, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.edit){
                            Intent intent = new Intent(getActivity().getApplicationContext(), EditCheckoutPage.class);
                            intent.putExtra("updateCheckout", model.get(position).getIdTransaksi());
                            startActivity(intent);
                        }else if (item.getItemId()==R.id.hapus){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Apakah anda ingin hapus data ini?");
                            builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    hapus(model.get(position).getIdTransaksi());
                                    Toast.makeText(getActivity(), "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        return false;
                    }
                });
            }
        });

        return view;
    }

    void load_data(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("id", null);

        StringRequest request = new StringRequest(Request.Method.POST, SERVER_READ_SERVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            model = new ArrayList<>();
                            for (int i=0; i<=jsonArray.length();i++){
                                JSONObject getData = jsonArray.getJSONObject(i);
                                model.add(new GetData(
                                        getData.getString("id_service"),
                                        getData.getString("nama_kategori"),
                                        getData.getString("status")
                                        ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adaptor = new Adaptor(getActivity().getApplicationContext(), model);
                        listView.setAdapter(adaptor);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", myId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    void hapus(String idservice){
        StringRequest request = new StringRequest(Request.Method.POST, SERVER_DELETE_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("server_response");
                    if (status.equals("Data Berhasil di Hapus")){
                        load_data();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> form = new HashMap<String,String>();
                form.put("id_service", idservice);
                return form;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }
}