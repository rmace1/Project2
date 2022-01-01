package com.revature.Project2.util;

import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

//https://crunchify.com/java-properties-files-how-to-update-config-properties-file-in-java/

public class PropertyConfig {
    private static Properties config = new Properties();
    private static String configName = "./src/main/resources/config.txt";

    private static String url;
    private static String userName;
    private static String password;

    public static void updateProperties(){


        try{
            FileInputStream configFis = new FileInputStream(configName);

            PropertiesConfiguration prop = new PropertiesConfiguration("./src/main/resources/application.properties");

            config.load(configFis);

            url = "jdbc:postgresql://" + config.getProperty("jdbcConnection").toString() + "/" + config.getProperty("jdbcDbName").toString();
            userName = config.getProperty("jdbcUserName").toString();
            password = config.getProperty("jdbcPassword").toString();
            configFis.close();

            prop.setProperty("spring.datasource.url", url);
            prop.setProperty("spring.datasource.username", userName);
            prop.setProperty("spring.datasource.password", password);
            prop.save();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
