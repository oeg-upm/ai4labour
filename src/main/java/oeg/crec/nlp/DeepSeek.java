package oeg.crec.nlp;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import oeg.crec.Misc;
import oeg.crec.model.Skill;
import oeg.crec.parsers.ParserESCO;

/**
 * Class to interact with DeepSeek API.
 * Registered user, see usage here: https://platform.deepseek.com/usage
 */
public class DeepSeek {
    private static final String DS_API_URL = "https://api.deepseek.com/chat/completions";
    private static final String DS_API_KEY = new Misc().getConfig("DEEPSEEK_APIKEY");

    public static void main(String[] args) {
        DeepSeek ds = new DeepSeek();
        String curso ="";
        String resp = DeepSeek.chat("You are a helpful assistant", "Hello, how are you?");
        System.out.println(resp);
    }
    


    
    
    public static String findESCO(String curso, String skills)
    {
        String sistema = "Eres un clasificador que debe atribuir cero o más skills al texto que describe un curso.";
        
        String pregunta = "Eres un clasificador que debe atribuir cero o más skills al texto que describe un curso. Aquí está la descripción del curso: '" + curso + "'.\n Y aquí está la lista de skills, separada por comas: '\n" + skills +"\n'";
        pregunta+="Por favor, devuelve **solo las skills que se enseñan en este curso**, como lista separada por comas. A lo sumo, quiero 3 skills. No añadas nada más.";
  //      System.out.println("Using DeepSeek!");
  
        pregunta = pregunta.replace("\\", "\\\\") // Escape backslashes
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\n", " ") // Replace newlines with spaces
                .replace("\r", " ");   // Handle carriage returns
  
  
        String apiKey = DS_API_KEY; // 
        String endpoint = "https://api.deepseek.com/chat/completions";

        String payload = "{"
                + "\"model\":\"deepseek-chat\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\""+sistema+"\"},"
                + "{\"role\":\"user\",\"content\":\""+pregunta+"\"}"
                + "],"
                + "\"stream\":false"
                + "}";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
           // System.out.println("Response Code: " + responseCode);

            Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8");
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());
                String content = rootNode.path("choices").get(0).path("message").path("content").asText();
                return content;
            } catch (Exception e2) {
                System.out.println("Error parseando la respuesta de DeepSeek");
                return "";
            }            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }    
    
    
    public static String chat(String tarea, String pregunta)
    {
  //      System.out.println("Using DeepSeek!");
        String apiKey = DS_API_KEY; // 
        String endpoint = "https://api.deepseek.com/chat/completions";

        String payload = "{"
                + "\"model\":\"deepseek-chat\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\""+tarea+"\"}, "
                + "{\"role\":\"user\",\"content\":\""+pregunta+"\"}"
                + "],"
                + "\"stream\":false"
                + "}";
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
           // System.out.println("Response Code: " + responseCode);

            Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8");
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());
                String content = rootNode.path("choices").get(0).path("message").path("content").asText();
                return content;
            } catch (Exception e2) {
                System.out.println("Error parseando la respuesta de DeepSeek");
                return "";
            }            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
