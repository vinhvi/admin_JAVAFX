package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.ImportOrder;
import entity.ImportOrderDetail;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

public class ImportOrderApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final LocalStorage localStorage = new LocalStorage();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/manage/importOrders";

    public String getRandomId() throws IOException, ClassNotFoundException {
        String id = "";
        String token = "Bearer " + localStorage.getTokenInLocal();
        String api = ipAddress + "/generateRandomId";
        URL url = new URL(api);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", token);

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

    public boolean add(ImportOrder importOrder) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createImport";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String token = "Bearer " + localStorage.getTokenInLocal();
        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", token);

        // Convert object to JSON format
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(importOrder);

        // Set the request body
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(json.getBytes());
        os.flush();
        os.close();

        // Get the response status code
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response body
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return true;
        }
        return false;
    }

    public boolean createImportDetail(ImportOrderDetail importOrderDetail) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createImportDetail";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String token = "Bearer " + localStorage.getTokenInLocal();
        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", token);

        // Convert object to JSON format
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(importOrderDetail);

        // Set the request body
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(json.getBytes());
        os.flush();
        os.close();

        // Get the response status code
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response body
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return true;
        }
        return false;
    }

    public ImportOrder getImportOrderById(String ma) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getByImportOrderId/" + ma;
        URL obj = new URL(url);
        String token = "Bearer " + localStorage.getTokenInLocal();
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            // Parse JSON response into a list of Role objects
            Gson gson = new Gson();

            return gson.fromJson(response.toString(), ImportOrder.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }

    public List<ImportOrder> getListImportOrder() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getAllImportOrder";
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
        List<ImportOrder> importOrders = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return importOrders;
    }

    public List<ImportOrderDetail> getListImportOrderDetailByImportOrderId(String id) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getImportOrderDetailByImportOrderId/" + id;
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

        List<ImportOrderDetail> importOrderDetails = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return importOrderDetails;
    }


}
