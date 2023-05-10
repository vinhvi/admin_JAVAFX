package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.ShippingCompany;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ShippingApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/shipping-company";
    private final LocalStorage localStorage = new LocalStorage();

    public List<ShippingCompany> getListShippingCompany() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getAllShippingCompany";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        InputStream inputStream = connection.getInputStream();
        List<ShippingCompany> shippingCompanyList = new ObjectMapper().readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return shippingCompanyList;
    }
}
