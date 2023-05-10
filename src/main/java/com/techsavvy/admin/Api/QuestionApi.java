package com.techsavvy.admin.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Question;
import org.springframework.http.HttpStatus;

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

public class QuestionApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/question";
    private final LocalStorage localStorage = new LocalStorage();


    public boolean updateQuestion(int id) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/updateQuestion/" + id;
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(""))
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

    public List<Question> getQuestionByReply() throws IOException, ClassNotFoundException {
        String url = ipAddress + "/getQuestionByReply";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        int statusCode = connection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            mapper.setDateFormat(dateFormat);
            List<Question> questionList = mapper.readValue(inputStream, new TypeReference<>() {
            });
            inputStream.close();
            return questionList;
        }
        return null;
    }
}
