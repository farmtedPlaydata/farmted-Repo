package com.farmted.memberservice.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.farmted.memberservice.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ProfileManager {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;  // 버킷 이름

    // 이미지를 S3에 업로드하고 이미지의 url을 반환
    public String uploadImageToS3(MultipartFile image) {
        // 확장자 추출
        String ext = getEXT(image);
        // S3 저장용 이름 새로 발급
        String imageName = changedImageName(ext);
        // 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));
        // S3에 업로드
        amazonS3.putObject(
                new PutObjectRequest(bucketName, imageName, handleInputStream(() -> image), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucketName, imageName).toString();   // 데이터베이스에 저장할 이미지가 저장된 주소
    }

    private String getEXT(MultipartFile image) {
        String originName = Objects.requireNonNull(image.getOriginalFilename(),
                () -> { throw new MemberException("ProfileManager - getEXT"); });
        return originName.substring(originName.lastIndexOf("."));
    }

    // 이미지 이름 중복 방지를 위해 랜덤으로 생성
    private String changedImageName(String ext) {
        return UUID.randomUUID() + ext;
    }

    // 이미지 업로드 예외처리 (받아온 image에 InputStream이 없는 경우)
    private InputStream handleInputStream(Supplier<MultipartFile> imageInputStream) {
        try {
            return imageInputStream.get().getInputStream();
        } catch (IOException e) {
            throw new MemberException("ProfileManager - handleInputStream");
        }
    }

    // 필요할 때 추가할 기초 삭제 로직
    public void deleteImage(String url) {
        String key = url.replace("http://" + bucketName + "s3.ap-northeast-2.amazonaws.com/", "");
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}
