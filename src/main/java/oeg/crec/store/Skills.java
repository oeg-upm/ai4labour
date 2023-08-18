package oeg.crec.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.model.Skill;
import static oeg.crec.store.Courses.courses;
import static oeg.crec.store.Courses.leer;

/**
 *
 * @author victor
 */
public class Skills {

    public static List<Skill> skills = new ArrayList();

    public static void main(String arg[]) {
        init();
    }
    
    /**
     * Loads in memory a list of skills, this time form OpenSkills
     * D:\svn\ai4labour\data\skills\openskills
     */
    public static void init() {
        skills.addAll(leer(Main.DATAFOLDER+"/skills/openskills/data.json"));
        for(Skill s : skills)
        {
            System.out.println(s.name);
        }
        
    }
    
    public static List<Skill> leer(String sfile)
    {
        List<Skill> res = new ArrayList();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(sfile));
            JsonNode dataArray = rootNode.get("data");
            if (dataArray.isArray()) {
                for (JsonNode o : dataArray) 
                {      
                    Skill skill = new Skill();
                    skill.id = o.get("id").asText();
                    skill.name = o.get("name").asText();
                    skill.infoURL = o.get("infoUrl").asText();
                    skill.description = o.get("description").asText();
                    res.add(skill);
                }
            }
        }
                catch(Exception es)
        {
            es.printStackTrace();
        }        
        return res;
    }
    
}
