package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

public class Main {
    private final Properties properties;

    public Main(Properties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {
        Properties properties = loadProperties("config.properties");
        if (properties == null) {
            System.out.println("Failed to load properties.");
            return;
        }

        Main app = new Main(properties);
        app.run();
    }

    public void run() {
        readFile();
        makeHttpRequest();
    }

    private void readFile() {
        String filePath = properties.getProperty("file.path");
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeHttpRequest() {
        String apiUrl = properties.getProperty("api.url");
        try {
            URL url = new URI(apiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    System.out.println("Response from API: " + content.toString());
                }
            } else {
                System.err.println("Failed to connect to API with response code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return null;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
