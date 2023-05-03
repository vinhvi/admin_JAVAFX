package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Product;
import com.techsavvy.admin.entity.Specifications;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/manage/products";
    private final LocalStorage localStorage = new LocalStorage();

    public String getRandomId() throws IOException, ClassNotFoundException {
        String id = "";
        String token = "Bearer " + localStorage.getTokenInLocal();
        String api = ipAddress + "/generateIdProduct";
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
            System.out.println("Lỗi kết nối đến API getRandomProduct: " + responseCode);
        }

        return id;
    }

    public String getRandomLH() throws IOException, ClassNotFoundException {
        String lh = "";
        String token = "Bearer " + localStorage.getTokenInLocal();
        String api = ipAddress + "/generateLH";
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
            lh = response;
        } else {
            // Xử lý lỗi
            System.out.println("Lỗi kết nối đến API getRandomLH: " + responseCode);
        }

        return lh;
    }

    public boolean add(Product product) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/add";
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(product)))
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

    public List<Product> getListProduct() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getAllProduct";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        InputStream inputStream = connection.getInputStream();
        List<Product> productList = new ObjectMapper().readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return productList;
    }

    public boolean createSpecifi(Specifications specifications) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createSpecification";
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(specifications)))
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

    public List<Specifications> getSpecifiByProduct(String productId) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getSpecifiByProduct/" + productId;
        String token = localStorage.getTokenInLocal();
        List<Specifications> specifications = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

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
                specifications = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Specifications.class));
            } else {
                System.out.println("API getOptionsByProduct error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return specifications;
    }

    public List<Product> getProductByIdOrName(String key) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getByIdOrName/" + URLEncoder.encode(key, StandardCharsets.UTF_8);
        String token = localStorage.getTokenInLocal();
        List<Product> productList = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

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
                productList = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
            } else {
                System.out.println("API getOptionsByProduct error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }


}
