package com.techsavvy.admin.Api;

import com.techsavvy.admin.Models.GetIpAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/products";

    public String getRandomId() throws IOException {
        String id = "";
        String api = ipAddress + "/generateIdProduct";
        URL url = new URL(api);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Đọc phản hồi từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            // Xử lý phản hồi và trả về kết quả phù hợp
            id = response;
        } else {
            // Xử lý lỗi
            System.out.println("Lỗi kết nối đến API: " + responseCode);
        }

        return id;
    }

    public String getRandomLH() throws IOException {
        String lh = "";
        String api = ipAddress + "/generateLH";
        URL url = new URL(api);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Đọc phản hồi từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            // Xử lý phản hồi và trả về kết quả phù hợp
            lh = response;
        } else {
            // Xử lý lỗi
            System.out.println("Lỗi kết nối đến API: " + responseCode);
        }

        return lh;
    }


}
