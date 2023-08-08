package oeg.crec;

import oeg.crec.store.Courses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    
    public static boolean LINUX = false;
    public static String DATAFOLDER = "/var/www/html/data/course/test";
    
    static {
        LINUX = !System.getProperty("os.name").toLowerCase().contains("win");
        if (LINUX)
            DATAFOLDER = "/var/www/html/data";
        else
            DATAFOLDER = "D:\\svn\\ai4labour\\data";
    }
    
    public static void main(String[] args) {
        
        System.out.println("Linux: " + LINUX);
        Courses.init();
        SpringApplication.run(Main.class, args);
    }
    
    
}
