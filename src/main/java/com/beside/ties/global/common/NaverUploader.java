package com.beside.ties.global.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static java.time.LocalTime.now;

@Configuration
public class NaverUploader {

    private AmazonS3 s3;
    @Value("${cloud.endpoint}")
    private String endPoint;
    @Value("${cloud.region-name}")
    private String regionName;
    @Value("${cloud.access-key}")
    private String accessKey;
    @Value("${cloud.secret-key}")
    private String secretKey;
    @Value("${cloud.bucket-name}")
    private String bucketName;

    @PostConstruct
    private void setAmazonS3() {
        // S3 client
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }


    public String upload(MultipartFile multipartFile, String filename) throws IOException {
        if(multipartFile.isEmpty()){
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(
                IOUtils.toByteArray(multipartFile.getInputStream()).length
        );

        String originalName = multipartFile.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".")+1);
        String time = now().toString();
        String fullFilename = filename + time + ext;

        s3.putObject(
                new PutObjectRequest(bucketName, fullFilename, multipartFile.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
                return fullFilename;
    }

}
