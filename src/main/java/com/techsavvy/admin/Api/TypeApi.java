package com.techsavvy.admin.Api;

import com.google.gson.Gson;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.entity.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TypeApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://"+ getIpAddress.getIpAddressServer() + ":8521";

    public Type getByName(String name) throws IOException {
        String apiUrl = ipAddress + "/api/types/getByName/" + name;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
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

            return gson.fromJson(response.toString(), Type.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }
}
