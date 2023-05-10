package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Evaluate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.List;

public class EvaluateApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final LocalStorage localStorage = new LocalStorage();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/auth/evaluates";

    public List<Evaluate> getListEvaluateByProduct(String product_id) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getListEvaluateByProduct/" + product_id;
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
        List<Evaluate> evaluateList = mapper.readValue(inputStream, new TypeReference<>() {
        });
        inputStream.close();
        return evaluateList;
    }

    public Evaluate getEvaluateById(int id) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getByPhone/" + id;
        String token = localStorage.getTokenInLocal();
        Evaluate evaluate = null;
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
                ObjectMapper mapper = new ObjectMapper();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                mapper.setDateFormat(dateFormat);

                evaluate = mapper.readValue(response.body(), mapper.getTypeFactory().constructType(Evaluate.class));
            } else {
                System.out.println("API getOptionsByProduct error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return evaluate;
    }
    public void deleteEvaluate(int id) throws IOException, InterruptedException, ClassNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        String token = "Bearer " + localStorage.getTokenInLocal();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", token).uri(URI.create(ipAddress + "/deleteById/" + id)).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        response.statusCode();
    }

}
