package oeg.crec.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a learning guide
 *
 * @author victor
 */
public class LearningGuide {

    public String id = "";
    public String local_id = "";
    public String institution = "";
    public String title = "";
    public List<String> learning_outcome = new ArrayList();

    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    }
