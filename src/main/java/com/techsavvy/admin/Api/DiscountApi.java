package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Discount;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

public class DiscountApi {

    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/discounts";

    private final LocalStorage localStorage = new LocalStorage();

    public List<Discount> getListDiscount() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getListDiscount";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        InputStream inputStream = connection.getInputStream();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        mapper.setDateFormat(dateFormat);
        List<Discount> discountList = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return discountList;
    }
}
