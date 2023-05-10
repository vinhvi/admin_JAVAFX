package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techsavvy.admin.Models.DateTypeAdapter;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Employee;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EmployeeApi {

    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/manage";

    private final LocalStorage localStorage = new LocalStorage();


    public String getRandomId() throws IOException, ClassNotFoundException {
        String id = "";
        String token = "Bearer " + localStorage.getTokenInLocal();
        String api = ipAddress + "/admin/employee/getRandomId";
        URL url = new URL(api);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        System.out.println("token gửi về server: " + token);
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


    public boolean add(Employee employee) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/admin/employee/create";
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(employee)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Update successful
            // Update failed
            isUpdate = response.statusCode() == HttpStatus.OK.value();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return isUpdate;
    }


//    public boolean add(Employee employee) throws IOException, ClassNotFoundException {
//        String url = ipAddress + "/admin/employee/create";
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        String token = "Bearer " + localStorage.getTokenInLocal();
//        // Set the request method and headers
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        con.setRequestProperty("Accept", "application/json");
//        con.setRequestProperty("Authorization", token);
//
//        // Convert Employee object to JSON format
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(employee);
//
//        // Set the request body
//        con.setDoOutput(true);
//        OutputStream os = con.getOutputStream();
//        os.write(json.getBytes());
//        os.flush();
//        os.close();
//
//        // Get the response status code
//        int responseCode = con.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            // Read the response body
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            return true;
//        }
//        return false;
//    }

    public List<Employee> getListEmployee() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/employee/getListEmployee";
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
        List<Employee> employees = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return employees;
    }

    public Employee getById(String maNV) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/employee/getById/" + maNV;
        URL obj = new URL(url);
        String token = "Bearer " + localStorage.getTokenInLocal();
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
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

            return gson.fromJson(response.toString(), Employee.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }

    public Employee getByEmail(String email) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/employee/getByEmail/" + email;
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
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

            return gson.fromJson(response.toString(), Employee.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }


    public boolean update(Employee employee) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/employee/update";
        URL obj = new URL(url);
        String token = "Bearer " + localStorage.getTokenInLocal();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", token);

        // Convert Employee object to JSON format
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);

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
        } else {
            System.out.println("Up date employee error " + responseCode);
        }
        return false;
    }
}


