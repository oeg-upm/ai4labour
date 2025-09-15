package oeg.crec.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class describes a learning guide
 * https://www.onetonline.org/
 * @author victor
 */
public class Course {

    public String id = "";
    public String local_id = "";
    public String institution = "";
    public String title = "";
    public String lan = "";
    public String degree = "";
    public String link="";
    public String contents="";
    public String objective="";
    public List<String> learning_outcomes = new ArrayList();
    public List<String> skills = new ArrayList();
    public List<Float> bloom = Arrays.asList(0F,0F,0F,0F,0F);


    
    
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Course parseJSON(String json)
    {
        Course course = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            course = objectMapper.readValue(json, Course.class);
            System.out.println("Deserialized Course: " + course);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return course;
    }    

    public void autoSetId() {
        String tin = encodeValue(institution);
        String tid = encodeValue(local_id);
        id = "http://ai4labour.linkeddata.es/data/course/" + tin + "/" + tid;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return value;
        }
    }
    public boolean hasAnywhere(String value) {
        String s1 = this.toString().toLowerCase();
        String s2 = value.toLowerCase();
        if (s1.contains(s2))
            return true;
        return false;
    }
    
    public String getText()
    {
        String text = title+"\n";
        text+=String.join(" ", learning_outcomes);
        text += "\n"+objective+"\n";
        text = text.replace("\\", "\\\\") // Escape backslashes
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\n", " ") // Replace newlines with spaces
                .replace("\r", " ");   // Handle carriage returns
        return text;
    }
    

    public boolean hasLO(String value) {
        for(String lo : learning_outcomes)
        {
            String s1 = lo.toLowerCase();
            String s2 = value.toLowerCase();
            
            if (s1.contains(s2))
                return true;
        }
        return false;
    }
}
