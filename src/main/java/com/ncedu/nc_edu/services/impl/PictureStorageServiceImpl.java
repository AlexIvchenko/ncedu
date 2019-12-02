package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.services.PictureStorageService;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class PictureStorageServiceImpl implements PictureStorageService {
    private final String BUCKET_NAME = "pictures";

    @Value("${OBJECT_STORAGE_ACCESS_KEY}")
    private String accessKey;

    @Value("${OBJECT_STORAGE_SECRET_KEY}")
    private String secretKey;

    @Value("${OBJECT_STORAGE_ENDPOINT}")
    private String endpoint;

    @Value("${OBJECT_STORAGE_ENDPOINT_PORT}")
    private Integer endpointPort;

    private MinioClient minioClient;

    @PostConstruct
    private void init() {
        try {
            this.minioClient = new MinioClient(endpoint, endpointPort, accessKey, secretKey);
            if (!this.minioClient.bucketExists(BUCKET_NAME)) {
                this.minioClient.makeBucket(BUCKET_NAME);
            }
        } catch (MinioException ex) {
            throw new RuntimeException("Cannot connect to object storage: " + ex.getMessage());
        } catch (NoSuchAlgorithmException | XmlPullParserException | IOException | InvalidKeyException ex) {
            throw new RuntimeException("Error occurred during object storage initialization: " + ex.getMessage());
        }
    }

    @Override
    public UUID upload(MultipartFile file) {
        InputStream inputStream;

        try {
             inputStream = file.getInputStream();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot upload file: " + ex.getMessage());
        }

        UUID id = UUID.randomUUID();

        try {
            this.minioClient.putObject(BUCKET_NAME, id.toString(), inputStream,
                    null, null, null, null);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | IOException | InvalidKeyException |
                NoResponseException | XmlPullParserException | ErrorResponseException | InternalException |
                InvalidArgumentException | InsufficientDataException | InvalidResponseException ex) {
            throw new RuntimeException("Cannot upload file: " + ex.getMessage());
        }

        return id;
    }

    @Override
    public boolean existsById(UUID id) {
        try {
            this.minioClient.statObject(BUCKET_NAME, id.toString());
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoResponseException | InvalidResponseException |
                XmlPullParserException | InvalidBucketNameException | InvalidArgumentException |
                InsufficientDataException | InternalException | IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (ErrorResponseException ex) {
            return false;
        }

        return true;
    }

    @Override
    public InputStream getById(UUID id) {
        InputStream stream;

        try {
            stream = this.minioClient.getObject(BUCKET_NAME, id.toString());
        } catch (InvalidKeyException | NoResponseException | InvalidResponseException | XmlPullParserException |
                InvalidBucketNameException | InvalidArgumentException | InsufficientDataException |
                InternalException | IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (ErrorResponseException ex) {
            throw new EntityNotFoundException("Picture with id " + id.toString());
        }

        return stream;
    }
}
