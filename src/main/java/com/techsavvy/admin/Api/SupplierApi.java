package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Supplier;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SupplierApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/manage/suppliers";
    private final LocalStorage localStorage = new LocalStorage();

    public String getRandomId() throws IOException {
        String id = "";
        String api = ipAddress + "/generateRandomId";
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
            System.out.println("Lỗi kết nối đến API getRandomId Supplier: " + responseCode);
        }

        return id;
    }

    public boolean add(Supplier supplier) throws IOException {
        String url = ipAddress + "/add";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Convert Employee object to JSON format
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(supplier);

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
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return true;
        }
        return false;
    }

    public List<Supplier> getListSupplier() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getListSupplier";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        InputStream inputStream = connection.getInputStream();
        List<Supplier> suppliers = new ObjectMapper().readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return suppliers;
    }

    public Supplier getByPhone(String phone) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getByPhone/" + phone;
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
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

            return gson.fromJson(response.toString(), Supplier.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }
}
