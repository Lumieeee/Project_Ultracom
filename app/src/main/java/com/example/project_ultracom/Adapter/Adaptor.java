package com.example.project_ultracom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project_ultracom.R;

import java.util.ArrayList;

public class Adaptor extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<GetData> model;

    public Adaptor(Context context, ArrayList<GetData> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView idTransaksi, jasaService, status_service;
        View view1 = inflater.inflate(R.layout.item_cardview_history, null);
        if (view1 != null){
            idTransaksi = view1.findViewById(R.id.tv_idTransaksi);
            jasaService = view1.findViewById(R.id.tv_KategoriJasa);
            status_service = view1.findViewById(R.id.tv_StatusService);

            idTransaksi.setText(model.get(position).getIdTransaksi());
            jasaService.setText(model.get(position).getJasaService());
            status_service.setText(model.get(position).getStatus_service());
        }
        return view1;
    }
}
