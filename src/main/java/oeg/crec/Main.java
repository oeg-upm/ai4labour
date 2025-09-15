package oeg.crec;

import oeg.crec.store.Courses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the entry point for the Web application. 
 * It statically assumes determined folders. 
 * Please change the folder to the one of interest for you.
 * @author Victor Rodríguez Doncel 
 */

@SpringBootApplication
public class Main {
    
    public static boolean LINUX = false;
    public static String DATAFOLDER = "/var/www/html/data/";
    
    static {
        LINUX = !System.getProperty("os.name").toLowerCase().contains("win");
        if (LINUX)
            DATAFOLDER = "/var/www/html/data";
        else
            DATAFOLDER = "D:\\svn\\ai4labour\\data";
    }
    
    public static void main(String[] args) {
        init();
        SpringApplication.run(Main.class, args);
    }
    
    public static void init()
    {
        System.out.println("Linux: " + LINUX);
        Courses.init();
    }
    
    
}
