package oeg.crec.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.parsers.ParserUPM;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author victor
 */
public class Courses {

    public static List<Course> courses = new ArrayList();

    public static void main(String arg[]) {
        init();
        for (Course course : courses) {
            System.out.println(course.title);
        }
        String sfile = Main.DATAFOLDER+"/courses/courses.json";
        escribir(courses, sfile);
    }
    
    /**
     * Exports the courses loaded in memory into a single JSON file.
     */
    public static void escribir(List<Course> coursesx, String sfile)
    {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(coursesx);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }          
    }
    public static List<Course> leer(String sfile)
    {
        try{
            File jsonFile = new File(sfile);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Course> course2 = objectMapper.readValue(jsonFile, new TypeReference<List<Course>>() {});
            return course2;
        }catch(Exception es)
        {
            es.printStackTrace();
        }        
        return new ArrayList();
    }
    

    /** 
     * Loads the courses into memory.
    */
    public static void init() {
        if (!courses.isEmpty()) {
            return;
        }
        //init UPM
        courses.addAll(leer(Main.DATAFOLDER+"/courses/kadir/courses.json"));
        //init KADIR
        courses.addAll(leer(Main.DATAFOLDER+"/courses/upm/courses.json"));
    }

    /**
     * Gets a course by id.
     * 
     */
    public static Course get(String id) {
        init();
        for(Course course : courses)
        {
            if (course.local_id.equals(id))
                return course;
        }
        return null;
    }
    
    /**
     * Returns courses. max=10
     */
    public static List<Course> search(String value, int max) {
        init();
        List<Course> list = new ArrayList();
        int count=0;
        for(Course course : courses)
        {
            if (course.hasLO(value))
            {
                count++;
                list.add(course);
            }
            else if (course.hasAnywhere(value))
            {
                count++;
                list.add(course);
            }
            if (count==max)
                break;
        }
        return list;
    }

}
