package com.dissonance.itit.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.enums.Directory;
import com.dissonance.itit.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.dissonance.itit.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final AmazonS3Client amazonS3Client;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지를 업로드하고 관련 데이터베이스 레코드를 생성합니다.
     *
     * @param multipartFile 업로드할 이미지 파일
     * @param directory     이미지가 저장될 디렉토리
     * @return 업로드된 이미지 정보 객체
     */
    @Transactional
    public Image upload(Directory directory, MultipartFile multipartFile) {
        validateImage(multipartFile.getContentType());

        String fileName = createFileName(multipartFile.getOriginalFilename(), directory.getName());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(multipartFile.getSize());

        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String path = amazonS3Client.getUrl(bucket, fileName).toString();

            Image image = Image.builder()
                    .imageUrl(path)     // TODO: CloudFront을 이용해 CDN 구축
                    .directory(directory)
                    .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1))
                    .build();

            imageRepository.save(image);

            return image;
        } catch (IOException e) {
            throw new CustomException(IO_EXCEPTION);
        }
    }

    /**
     * 주어진 콘텐츠 타입이 이미지 파일인지 검증합니다.
     *
     * @param contentType 콘텐츠 타입
     */
    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException(INVALID_FILE_TYPE);
        }
    }

    /**
     * S3 버킷에서 이미지를 삭제하고 관련 데이터베이스 레코드를 제거합니다.
     *
     * @param image 삭제할 이미지 정보
     */
    @Transactional
    public void delete(Image image) {
        amazonS3Client.deleteObject(bucket, image.getDirectory().getName() + "/" + image.getConvertImageName());

        imageRepository.deleteById(image.getId());
    }

    /**
     * S3 버킷에 저장될 파일 이름을 생성합니다. 파일 이름 중복을 방지하기 위해 UUID를 사용합니다.
     *
     * @param fileName 원본 파일 이름
     * @param dirName  이미지가 저장될 디렉토리 이름
     * @return 생성된 파일 이름
     */
    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + "_" + fileName;
    }

    /**
     * 주어진 이미지 ID를 사용하여 이미지를 조회합니다.
     *
     * @param imageId 이미지 ID
     * @return 조회된 이미지 정보
     * @throws CustomException 이미지를 찾을 수 없는 경우 발생
     */
    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));
    }
}
