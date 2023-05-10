package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Order;
import entity.OrderDetail;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/auth/order";
    private final LocalStorage localStorage = new LocalStorage();

    public String getRandomIdOrder() throws IOException, ClassNotFoundException {
        String id = "";
        String token = "Bearer " + localStorage.getTokenInLocal();
        String api = ipAddress + "/getRandomIdOrder";
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
            System.out.println("Lỗi kết nối đến API getRandomIdOrder: " + responseCode);
        }

        return id;
    }


    public Order createOrder(Order order) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createOrder";
        String token = localStorage.getTokenInLocal();
        Order order_saved = null;
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        mapper.setDateFormat(dateFormat);
        String json = mapper.writeValueAsString(order);
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK.value()) {
                order_saved = mapper.readValue(response.body(), mapper.getTypeFactory().constructType(Order.class));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return order_saved;
    }

    public OrderDetail createOrderDetail(OrderDetail orderDetail) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createOrderDetail";
        String token = localStorage.getTokenInLocal();
        OrderDetail orderDetail_saved = null;
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        mapper.setDateFormat(dateFormat);
        String json = mapper.writeValueAsString(orderDetail);
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK.value()) {
                orderDetail_saved = mapper.readValue(response.body(), mapper.getTypeFactory().constructType(OrderDetail.class));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return orderDetail_saved;
    }

    public List<OrderDetail> getByOrder(String order_id) throws IOException, ClassNotFoundException {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        String token = localStorage.getTokenInLocal();
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = ipAddress + "/getByOrder/" + order_id;

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
                ObjectMapper mapper = new ObjectMapper();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                mapper.setDateFormat(dateFormat);
                orderDetailList = mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, OrderDetail.class));
            } else {
                System.out.println("API getOptionsByProduct error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderDetailList;
    }


    public List<Order> getListOrder() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getListOrder";
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
        List<Order> orderList = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return orderList;
    }

}
