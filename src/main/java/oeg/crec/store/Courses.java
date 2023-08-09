package oeg.crec.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.upm.ParserUPM;
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
        export();
    }
    public static void export()
    {
        try{
            String sfile = Main.DATAFOLDER+"/courses/courses.json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(courses);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }          
        
    }

    public static void init() {
        if (!courses.isEmpty()) {
            return;
        }
        //init UPM
        String pdffolder = Main.DATAFOLDER + "/courses/upm";
        File dir = new File(pdffolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Course course = new ParserUPM().parse(child.getAbsolutePath());
                courses.add(course);
            }
        } else {
        }
        //init KADIR
        try{
            String sfile = Main.DATAFOLDER+"/courses/kadir/courses.json";
            File jsonFile = new File(sfile);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Course> course2 = objectMapper.readValue(jsonFile, new TypeReference<List<Course>>() {});
            courses.addAll(course2);
        }catch(Exception es)
        {
            es.printStackTrace();
        }
        
        
        
    }

    public static Course get(String id) {
        init();
        for(Course course : courses)
        {
            if (course.local_id.equals(id))
                return course;
        }
        return null;
    }
    
    public static List<Course> search(String value) {
        init();
        List<Course> list = new ArrayList();
        for(Course course : courses)
        {
            if (course.hasLO(value))
                list.add(course);
            else if (course.hasAnywhere(value))
                list.add(course);
        }
        return list;
    }

}
