package oeg.crec;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Helper class with useful methods.
 * @author victor
 */
public class Misc {
    
    public static void download(String url)
    {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    String fileName = extractFileNameFromUrl(url);
                    String filePath = fileName;                    
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    System.out.println("PDF downloaded successfully.");
                } else {
                    System.out.println("Failed to download PDF. Status code: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    /**
     * 
     */
    private static String extractFileNameFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            return Paths.get(path).getFileName().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "downloaded.pdf"; // Default filename if extraction fails
        }
    }    
     private static final String CONFIG_FILE_PATH = "ai4labour.cfg";    
   
    public static String getConfig(String key) {
        List<String> configLines = new ArrayList();        
        try {
            Files.lines(Paths.get(CONFIG_FILE_PATH))
                .forEach(line -> configLines.add(line));
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
        for (String line : configLines) {
            if (!line.trim().startsWith("#") && line.contains("=")) {
                String[] parts = line.split("=", 2);
                if (parts[0].trim().equals(key)) {
                    return parts[1].trim();
                }
            }
        }
        return null;
    }
    
}
