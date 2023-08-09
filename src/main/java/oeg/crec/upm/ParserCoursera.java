package oeg.crec.upm;

import oeg.crec.model.Course;

/**
 * Data from Maria NavaS: https://github.com/mnavasloro/course_crawler
 * @author victor
 */
public class ParserCoursera {
    String text = "";
    
    public static void main(String arg[]) {
        String input = "d:\\guia.pdf";
        ParserUPM parser = new ParserUPM();
        Course lg = parser.parse(input);
        System.out.println(lg);
    }
    
}
