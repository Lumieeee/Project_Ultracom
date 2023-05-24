package com.example.project_ultracom.Adapter;

public class GetData {
    String idTransaksi = "", jasaService = "", status_service = "";

    public GetData(String idTransaksi, String jasaService, String status_service){

        this.idTransaksi = idTransaksi;
        this.jasaService = jasaService;
        this.status_service = status_service;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getJasaService() {
        return jasaService;
    }

    public String getStatus_service() {
        return status_service;
    }

}
