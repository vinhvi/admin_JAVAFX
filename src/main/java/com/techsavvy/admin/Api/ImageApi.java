package com.techsavvy.admin.Api;

import com.techsavvy.admin.Models.GetIpAddress;
import com.techsavvy.admin.entity.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class ImageApi {
    public final GetIpAddress getIpAddress = new GetIpAddress();
    private final String ipAddress = "http://" + getIpAddress.getIpAddressServer() + ":8521/api/images";

    public Image uploadImage(File file) {
        Image image = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = ipAddress + "/upload";
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("multipartFile", new FileSystemResource(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Image> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Image.class);

            if (responseEntity != null && responseEntity.getBody() != null) {
                image = responseEntity.getBody();
                // Code để hiển thị ảnh trong JavaFX
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

}
