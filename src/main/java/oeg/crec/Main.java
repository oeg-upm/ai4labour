package oeg.crec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    
    static boolean LINUX = false;
    static String DATAFOLDER = "/var/www/html/data/course/test";
    
    
    public static void main(String[] args) {
        LINUX = !System.getProperty("os.name").toLowerCase().contains("win");
        System.out.println("Linux: " + LINUX);
        SpringApplication.run(Main.class, args);
    }
    
    
}
