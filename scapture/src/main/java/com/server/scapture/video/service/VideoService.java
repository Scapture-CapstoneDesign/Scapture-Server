package com.server.scapture.video.service;

import com.server.scapture.util.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    ResponseEntity<CustomAPIResponse<?>> upload(List<MultipartFile> multipartFiles) throws IOException;
}
