package com.techsavvy.admin.Api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Options;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OptionsApi {

    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/manage/optionProduct";
    private final LocalStorage localStorage = new LocalStorage();

    public boolean createOptions(Options options) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/save";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String token = "Bearer " + localStorage.getTokenInLocal();
        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", token);

        // Convert Employee object to JSON format
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(options);

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


    public boolean updateOptions(Options options) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/update";
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(options)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK.value()) {
                // Update successful
                isUpdate = true;
            } else {
                // Update failed
                isUpdate = false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return isUpdate;
    }


    public List<Options> getOptionsByProduct(String productId) throws IOException, ClassNotFoundException {
        List<Options> options = new ArrayList<>();
        String token = localStorage.getTokenInLocal();
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = ipAddress + "/getByProduct/" + productId;

            // Tạo request HTTP GET tới API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            // Gửi request và lấy response trả về
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Xử lý response nếu response status code là 200 OK
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                options = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Options.class));
            } else {
                System.out.println("API getOptionsByProduct error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return options;
    }


}
