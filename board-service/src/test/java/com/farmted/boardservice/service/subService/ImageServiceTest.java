package com.farmted.boardservice.service.subService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.farmted.boardservice.config.AwsS3Config;
import com.farmted.boardservice.config.ImageUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import({ImageService.class, AwsS3Config.class})
@DisplayName("Image-Service 테스트 코드")
public class ImageServiceTest {
    @Mock
    private AmazonS3 amazonS3;
    @InjectMocks
    private ImageService imageService;

    @Test
    @DisplayName("S3에 업로드 후 URL 반환")
    void uploadImageToS3() throws MalformedURLException{
        // given
        String path = "images/testJpg.jpg";
        MultipartFile mockMultipartFile = ImageUtils.createTestImage(path);
        given(amazonS3.putObject(any(PutObjectRequest.class)))
                .willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any()))
                .willReturn(new URL(ImageUtils.IMAGE_URL));
        // when
        String url = imageService.uploadImageToS3(mockMultipartFile);
        // then
        assertThat(url).isEqualTo(ImageUtils.IMAGE_URL);
    }

    @Test
    @DisplayName("S3에 사진 삭제")
    void deleteImage(){
        // given
        String url = ImageUtils.IMAGE_URL;
        doNothing()
                .when(amazonS3).deleteObject(any(DeleteObjectRequest.class));
        // when
        imageService.deleteImage(url);
        // then
        verify(amazonS3, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
