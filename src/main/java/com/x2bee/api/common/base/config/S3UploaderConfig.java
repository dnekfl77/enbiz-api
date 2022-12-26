package com.x2bee.api.common.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.services.s3.AmazonS3Client;
import com.x2bee.common.base.upload.S3UploadCommonComponent;
import com.x2bee.common.base.upload.S3UploaderCommonImpl;
import com.x2bee.common.base.upload.Uploader;

@Configuration
public class S3UploaderConfig {
	@Autowired
    private Environment env;
	@Autowired
	private AmazonS3Client amazonS3Client;

	@ConfigurationProperties("cloud.aws.s3")
	@Bean
	public S3UploadCommonComponent s3UploadCommonComponent() {
		return new S3UploadCommonComponent();
	}

	@ConfigurationProperties("cloud.aws.s3-video-origin")
	@Bean
	public S3UploadCommonComponent s3VideoOriginUploadCommonComponent() {
		return new S3UploadCommonComponent();
	}

	@Bean
	public Uploader uploader() {
		return new S3UploaderCommonImpl(env, amazonS3Client, s3UploadCommonComponent());
	}
	
	@Bean
	public Uploader videoUploader() {
		return new S3UploaderCommonImpl(env, amazonS3Client, s3VideoOriginUploadCommonComponent());
	}
	
}
