package oeg.crec.upm;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.model.Course;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;
/**
 * Parser of Kadir Has courses. Example of course:
 * https://bologna.khas.edu.tr/ders/30002402/program/50258558
 *
 * @author victor
 */
public class ParserKadir {

    Document docCourse = null;
    Document docProgram = null;

    public static void main(String arg[]) {
        
        scrapAll();
        /*
        String samplecourse = "https://bologna.khas.edu.tr/ders/30002402/program/50258558";
        String sampleprogram = "https://bologna.khas.edu.tr/program/50258558/ders-plani-sap";
        ParserKadir parser = new ParserKadir();
        
        List<String> programs = getPrograms();
        Course lg = parser.parse(samplecourse);
        System.out.println(lg);
        List<Course> courses = parser.parseProgram(sampleprogram);
        
        
        try{
            String sfile = Main.DATAFOLDER+"/courses/kadir/courses.json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(courses);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
            System.out.println("Veamos1: " + courses.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }        
        
        try{
            String sfile = Main.DATAFOLDER+"/courses/kadir/courses.json";
            File jsonFile = new File(sfile);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Course> course2 = objectMapper.readValue(jsonFile, new TypeReference<List<Course>>() {});
            System.out.println("Veamos2: " + course2.get(0));
        }catch(Exception es)
        {
            
        }
*/
        
    }
    public static List<Course> all = new ArrayList();
    
    public static void scrapAll()
    {
        ParserKadir parser = new ParserKadir();
        List<String> programs = getPrograms();
        int i=0;
        for(String program : programs)
        {
            i++;
            System.out.println("PROGRAM " + i + "/"+programs.size()+" " + program);
            List<Course> courses = parser.parseProgram(program);
            all.addAll(courses);
        }
        try{
            String sfile = Main.DATAFOLDER+"/courses/kadir/courses.json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(all);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }                
    }
    
    public static List<String> getPrograms()
    {
        List<String> uris = new ArrayList();
        List<String> constantValues = Arrays.asList("50258558","50258535","50258562","50258564","50258505","50258523","50258524","50258545","50357516","50258389","50258556","50258548","50258549","50258384","50258388","50260041","50258527","50366239","50258569","50258570","50258237","50258375","50374356","50258538","50258539","50258509","50258540","50258531","50371628","50258567","50258551");
        for(String value : constantValues)
        {
            String uri = "https://bologna.khas.edu.tr/program/"+value+"/ders-plani-sap";
            uris.add(uri);
        }
        return uris;

    }
    
    //Sample: https://bologna.khas.edu.tr/program/50258558/ders-plani-sap
    public List<Course> parseProgram(String uri)
    {
        List<Course> courses = new ArrayList();
        List<String> courseurls= new ArrayList();

        String programid="";
        String[] parts = uri.split("/");
        int thirdPartIndex = parts.length - 2; // Index of the third part from the end
        if (thirdPartIndex >= 0) {
            programid=parts[thirdPartIndex];
        }
        
        
        try {
            String englishurl = "https://bologna.khas.edu.tr/cl/en";
            Connection.Response englishresponse = Jsoup.connect(englishurl).execute();
            Document englishdoc = englishresponse.parse();
            docProgram = Jsoup.connect(uri).cookies(englishresponse.cookies()).get();
            Elements links = docProgram.select("a[href]");
            for (Element link : links) {
                String href = link.attr("href");
                if (href.contains("/program/"))
                {
                    courseurls.add(href);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
        int i=0;
        
        for(String courseurl : courseurls)
        {
            String u = "https://bologna.khas.edu.tr/"+courseurl;
            Course course = parse(u);
            if (course==null)
            {
                System.out.println("Error en: " + u);
                continue;
            }
            course.degree=programid;
            
            
            boolean existia=false;
            for(Course a : all)
            {
                if (a.id.equals(course.id))
                    existia=true;
            }
            if (!existia)
                courses.add(course);
     //       System.out.println(course);
            i++;
            if (i==9999)
                break;
            
        }
        try{
            String sfile = Main.DATAFOLDER+"/courses/kadir/" + programid+".json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(courses);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }          
        
        
        return courses;
    }
    
    
    /**
     * Parses a Kadir Has course descriptoin
     * Este método supera la dificultad de las cookies, que las retiene para
     * futuras consultas. necesario por lo del inglés. Meritoroi!
     */
    public Course parse(String uri) {

        try {
            String englishurl = "https://bologna.khas.edu.tr/cl/en";
            Connection.Response englishresponse = Jsoup.connect(englishurl).execute();
            Document englishdoc = englishresponse.parse();
            docCourse = Jsoup.connect(uri).cookies(englishresponse.cookies()).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Course course = new Course();
        course.institution = "Kadir Has Üniversitesi";
        course.title = getTextInTable(0,1,0);
        if (course.title.isEmpty())
            return null;
        course.local_id = getLocalId(uri);
        
        int icontent = guessRow(1, "Course Contents:");
        int ilos = guessRow(1,"Learning Outcomes");    
        int iobje = guessRow(1,"Objectives");
        
        if (icontent>0)
            course.contents = getTextInTable(1,icontent,1);
        if(iobje>0)
            course.objective = getTextInTable(1,iobje,1);
        if(ilos>0)
        {
            String[] los = getTextInTable(1,ilos,1).split("\n");
            for(String lo : los)
                course.learning_outcomes.add(lo);
        }
        
        uri =uri.replace("ders", "pdf");
        course.link = uri;
        course.autoSetId();
        return course;
    }

    private int guessRow(int itable,String header)
    {
        Elements tables = docCourse.select("table");
        if (itable>=tables.size())
            return -1;
        Element table = tables.get(itable); // Index 1 corresponds to the second table
        int rowIndex = -1; // Initialize to -1 (not found)
        Elements rows = table.select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            if (columns.size() > 0 && columns.first().text().contains(header)) {
                rowIndex = i; // Update rowIndex if the word is found
                break; // No need to continue searching
            }        
        }
        return rowIndex;
    }
    
    private String getLocalId(String url) {
        String[] parts = url.split("/");
        int thirdPartIndex = parts.length - 3; // Index of the third part from the end
        if (thirdPartIndex >= 0) {
            return parts[thirdPartIndex];
        }
        return "";

    }
    
    //1,7,1
    private String getTextInTable(int itable, int irow, int icolumn)
    {
        try {
            Elements tables = docCourse.select("table");
            if (tables.size() >= itable+1) {
                Element secondTable = tables.get(itable); // Index 1 corresponds to the second table
                Elements rows = secondTable.select("tr");
                if (rows.size() >= irow+1) {
                    Element eighthRow = rows.get(irow); // Index 7 corresponds to the eighth row
                    Elements columns = eighthRow.select("td");
                    if (columns.size() >= icolumn+1) {
                        Element secondColumn = columns.get(icolumn); // Index 1 corresponds to the second column
                        String text = secondColumn.text();
                        return text;
                    } else {
                    }
                } else {
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     */


}
