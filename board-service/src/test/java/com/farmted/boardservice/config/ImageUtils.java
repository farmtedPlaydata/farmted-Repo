package com.farmted.boardservice.config;

import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.exception.BoardException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Supplier;

public class ImageUtils {
    // 가장 S3 URL 반환
    public static final String IMAGE_URL =
            "https://s3.ap-northeast-2.amazonaws.com/farmted-product/example.png";
    // 더미 이미지 생성
    public static MultipartFile createTestImage(String path){
        // 클래스패스 상에서 이미지를 읽어온다.
        ClassPathResource classPathResource = new ClassPathResource(path);
        // 이미지를 바이트 배열로 읽어온다.
        byte[] imageBytes = handleInputStream(()->classPathResource);

        // MultipartFile 생성
        return new MockMultipartFile(
                "file",               // 파라미터 이름
                "light_image.jpg",    // 파일 이름
                "image/jpeg",         // 파일 타입
                imageBytes             // 이미지 바이트 배열
        );
    }
    // 이미지 업로드 예외처리 (받아온 image에 InputStream없는 경우)
    private static byte[] handleInputStream(Supplier<ClassPathResource> classPath) {
        try {
            return classPath.get().getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new BoardException(ExceptionType.IMAGE);
        }
    }
}
