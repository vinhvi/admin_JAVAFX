package com.techsavvy.admin.Api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Role;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RoleApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final LocalStorage localStorage = new LocalStorage();
    private final String ipAddress = "http://"+ getIpAddress.getIpAddressServer() + ":8521";

    public RoleApi() {
    }

    public List<Role> getAllRoles() throws IOException, ClassNotFoundException {
        String apiUrl = ipAddress + "/api/role/getList";
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
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
            Type roleListType = new TypeToken<ArrayList<Role>>() {
            }.getType();
            List<Role> roles = gson.fromJson(response.toString(), roleListType);

            return roles;
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }

    public Role getByName(String name) throws IOException, ClassNotFoundException {
        String apiUrl = ipAddress + "/api/role/getByName?name=" + name;
        String token = "Bearer " + localStorage.getTokenInLocal();
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
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
            return gson.fromJson(response.toString(), Role.class);
        } else {
            throw new IOException("Failed to retrieve roles from API, status code: " + status);
        }
    }

}
