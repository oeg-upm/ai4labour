package oeg.crec.store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.upm.ParserUPM;

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
    }

    public static void init() {
        if (!courses.isEmpty()) {
            return;
        }
        String pdffolder = Main.DATAFOLDER + "/courses/upm";
        File dir = new File(pdffolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Course course = new ParserUPM().parse(child.getAbsolutePath());
                courses.add(course);
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
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
