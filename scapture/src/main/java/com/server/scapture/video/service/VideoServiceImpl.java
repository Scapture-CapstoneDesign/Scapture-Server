package com.server.scapture.video.service;

import com.server.scapture.util.S3.S3Service;
import com.server.scapture.util.response.CustomAPIResponse;
import com.server.scapture.video.dto.VideoUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{
    private final S3Service s3Service;
    @Override
    public ResponseEntity<CustomAPIResponse<?>> upload(List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageUrlList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            // 파일명 지정 (겹치면 안되고, 확장자 빼먹지 않도록 조심!)
            String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
            // 파일데이터와 파일명 넘겨서 S3에 저장
            s3Service.upload(multipartFile, fileName);
            String presignedURL = s3Service.getPresignedURL(fileName);
            // DB에는 전체 url말고 파일명으로 저장할 것임
            imageUrlList.add(presignedURL);
        }
        // data
        VideoUploadResponseDto data = VideoUploadResponseDto.builder()
                .videoUrl(imageUrlList)
                .build();
        // resonseBody
        CustomAPIResponse<VideoUploadResponseDto> responseBody = CustomAPIResponse.createSuccess(HttpStatus.CREATED.value(), data, "비디오 업로드 성공");
        // ResponseEntity
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }
}
