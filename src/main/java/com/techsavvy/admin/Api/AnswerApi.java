package com.techsavvy.admin.Api;

import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Answer;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AnswerApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/answer";
    private final LocalStorage localStorage = new LocalStorage();
    public boolean createAnswer(Answer newAnswer) throws IOException, ClassNotFoundException {
        String url = ipAddress + "/createAnswer";
        String token = localStorage.getTokenInLocal();
        boolean isUpdate;
        // Send POST request to server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(newAnswer)))
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
}
