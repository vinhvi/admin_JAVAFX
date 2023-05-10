package com.techsavvy.admin.Api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.Models.LocalStorage;
import entity.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();

    private final LocalStorage localStorage = new LocalStorage();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/images";

    public Image uploadImage(File file, String id_Product) throws IOException, ClassNotFoundException {
        Image image = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = ipAddress + "/upload/" + id_Product;
        String token = "Bearer " + localStorage.getTokenInLocal();
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("multipartFile", new FileSystemResource(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", token);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Image> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Image.class);

            if (responseEntity.getBody() != null) {
                image = responseEntity.getBody();
                // Code để hiển thị ảnh trong JavaFX
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public boolean deleteImage(String id) throws IOException, InterruptedException, ClassNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        String token = "Bearer " + localStorage.getTokenInLocal();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", token).uri(URI.create(ipAddress + "/delete/" + id)).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public List<Image> getImageByProduct(String productId) throws IOException, ClassNotFoundException {
        List<Image> images = new ArrayList<>();
        String token = "Bearer " + localStorage.getTokenInLocal();
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = ipAddress + "/getImageByProduct/"+productId;

            // Tạo request HTTP GET tới API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .GET()
                    .build();

            // Gửi request và lấy response trả về
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Xử lý response nếu response status code là 200 OK
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                mapper.setDateFormat(dateFormat);
                images = mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Image.class));
            } else {
                System.out.println("API getImageByProduct error: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }

}
