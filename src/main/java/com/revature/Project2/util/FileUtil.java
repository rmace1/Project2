package com.revature.Project2.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.revature.Project2.models.User;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtil {

    private static Logger log = Logger.getLogger(FileUtil.class);

   /* public static File convertToFile(MultipartFile multipartFile){
        File file = new File("./src/main/resources", multipartFile.getOriginalFilename());
        //converts from Multipart File to File
        try {
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
            log.info(multipartFile.getName() + " has beeen uploaded to S3 bucket.");
        } catch (IOException e) {
            log.error(e);
        }

        return file;
    }*/

    public static String uploadToS3(User user, MultipartFile multipartFile) {
        Properties config = new Properties();
        String configName = "./src/main/resources/config.txt";
        try {
            config.load(new FileInputStream(configName));
        } catch (IOException e) {
            log.error(e);
        }
        final String awsID = config.getProperty("AWSPass");
        final String secretKey = config.getProperty("AWSSecretPass");
        final String region = "us-east-2";
        final String bucketName = "jwa-p2";



        /*
         * credentials
         * */
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsID, secretKey);

        /*
         * s3 instance
         * */
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        String imageURL = "SocialNetwork/" + user.getUserName() + "/" + multipartFile.getOriginalFilename();

        try{
            s3Client.putObject(new PutObjectRequest(bucketName, imageURL,
                    multipartFile.getInputStream(), new ObjectMetadata()));
            log.info(multipartFile.getName() + " has beeen uploaded to S3 bucket.");
        } catch (Exception e) {
            log.error(e);
        }
        return "https://jwa-p2.s3.us-east-2.amazonaws.com/" + imageURL;
    }

}
