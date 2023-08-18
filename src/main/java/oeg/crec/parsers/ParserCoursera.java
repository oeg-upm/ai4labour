package oeg.crec.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import oeg.crec.bloom.Bloom;
import oeg.crec.model.Skill;
import oeg.crec.store.Courses;
/**
 * Data from Maria NavaS: https://github.com/mnavasloro/course_crawler
 * @author victor
 */
public class ParserCoursera {
    String text = "";
    
    public static void main(String arg[]) {
        generatenew();
        List<Course> res = Courses.leer(Main.DATAFOLDER+"/courses/coursera/courses.json");
        for(Course c : res)
        {
        System.out.println(c.title + " "+c.link);
        }
    }
    public static void generatenew()
    {
        String ruta = Main.DATAFOLDER+"/courses/coursera/coursessample.json";
        List<Course> res = new ArrayList();
        try{
            Bloom bloomen = new Bloom("en"); //course.lan
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(ruta));
            if (rootNode.isArray()) {
                for (JsonNode o : rootNode) {            
                    Course c = new Course();
                    String lan = o.get("language").asText();
                    if (!lan.equals("English"))
                        continue;
                    c.id="coursera"+generateRandomHexString(8);
                    c.lan = "en";
                    c.title = o.get("name").asText().replace("| Coursera", "");
                    c.link = o.get("url").asText();
                    c.objective = o.get("description").asText();
                    c.skills = objectMapper.convertValue(o.get("skills"), new TypeReference<List<String>>() {});
                    c.learning_outcomes = objectMapper.convertValue(o.get("outcomes"), new TypeReference<List<String>>() {});
                    List<String> topics = objectMapper.convertValue(o.get("topics"), new TypeReference<List<String>>() {});
                    c.contents = String.join(", ", topics);
                    c.bloom = bloomen.getBloom(c);
                    c.institution = "Coursera";
                    c.degree = o.get("url").asText();
                            

                    res.add(c);
                }
            }
            String sfile = Main.DATAFOLDER + "/courses/coursera/courses.json";
            Courses.escribir(res, sfile);            
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    //"url","name","topics","skills","type","description"
    public static void generate()
    {
        final LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages().build();
        List<Course> res = new ArrayList();
        String ruta = Main.DATAFOLDER+"/courses/coursera/courses.csv";
        int conta=0;
        try (CSVReader csvReader = new CSVReader(new FileReader(ruta))) {
            List<String[]> csvData = csvReader.readAll();
            Bloom bloomen = new Bloom("en"); //course.lan
            for (String[] row : csvData) {        
                System.out.println(conta);
                conta++;
                Course c = new Course();
                c.link = row[0];
                c.title=row[1];
                c.contents=row[2];
                c.objective=row[5];
                c.skills.add(row[3]);
                c.bloom = bloomen.getBloom(c);

                Language detectedLanguage = detector.detectLanguageOf(c.title);
                String lan = detectedLanguage.getIsoCode639_1().toString();
                if (!lan.equals("en"))
                    continue;

                res.add(c);
                
                
                
            }
            String sfile = Main.DATAFOLDER + "/courses/coursera/courses.json";
            Courses.escribir(res, sfile);            
                }catch(Exception e)
            {
                e.printStackTrace();
            }
    }
     public static String generateRandomHexString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length / 2];
        random.nextBytes(bytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }

        return hexString.toString();
    }  
}
