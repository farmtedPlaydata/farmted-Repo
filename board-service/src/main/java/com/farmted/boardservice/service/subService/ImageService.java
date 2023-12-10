package com.farmted.boardservice.service.subService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.exception.BoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; //버킷 이름

    //이미지를 S3에 업로드하고 이미지의 url을 반환
    public String uploadImageToS3(MultipartFile image) {
        // 확장자 추출
        String ext = getEXT(image);
        // S3 저장용 이름 새로 발급
        String imageName = changedImageName(ext);
        //메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/"+ext.substring(1));
        // S3에 업로드
        amazonS3.putObject(
                new PutObjectRequest(bucketName, imageName, handleInputStream(()->image), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucketName, imageName).toString(); //데이터베이스에 저장할 이미지가 저장된 주소
    }

    // 확장자명 추출, 파일에 이름없으면 예외
    private String getEXT(MultipartFile image){
        String originName = Objects.requireNonNull(image.getOriginalFilename(),
                ()-> {throw new BoardException(ExceptionType.IMAGE);});
        return originName.substring(originName.lastIndexOf("."));
    }
    //이미지 이름 중복 방지를 위해 랜덤으로 생성
    private String changedImageName(String ext) {
        String random = UUID.randomUUID().toString();
        return random+ext;
    }
    // 이미지 업로드 예외처리 (받아온 image에 InputStream없는 경우)
    private InputStream handleInputStream(Supplier<MultipartFile> imageInputStream) {
        try {
            return imageInputStream.get().getInputStream();
        } catch (IOException e) {
            throw new BoardException(ExceptionType.IMAGE);
        }
    }
// 필요할 때 추가할 기초 삭제 로직
    public void deleteImage(String key) {
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketName, key);
        amazonS3.deleteObject(deleteRequest);
    }
}