package oeg.crec.upm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oeg.crec.model.Course;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Methods to parse a UPM Guía de Aprendizaje
 * @author victor
 */
public class ParserUPM {

    String text = "";
    
    public static void main(String arg[]) {
        String input = "d:\\guia.pdf";
        ParserUPM parser = new ParserUPM();
        Course lg = parser.parse(input);
        System.out.println(lg);
    }
    
    public Course parse(String sfile)
    {
        Course lg = new Course();
        lg.institution = "Universidad Politécnica de Madrid";
        
        
        try {
            File file = new File(sfile);
            PDDocument doc = PDDocument.load(file);
            text = new PDFTextStripper().getText(doc);
            lg.lan = (text.contains("GUÍA DE APRENDIZAJE")) ? "es" : "en";
            lg.link = "https://ai4labour.linkeddata.es/data/courses/upm/"+file.getName();
            if (lg.lan.equals("es"))
            {
                lg.title = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[1].trim();
                lg.local_id = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[0].trim();
                lg.degree = getFragment("PLAN DE ESTUDIOS", "CURSO ACADÉMICO Y SEMESTRE").split("-")[1].trim();
                lg.learning_outcomes = getSkills("3.2. Resultados del aprendizaje","4. Descripción de la asignatura y temario" );
            }
            else
            {
                lg.title = getFragment("SUBJECT", "DEGREE PROGRAMME").split("-")[1].trim();
                lg.local_id = getFragment("SUBJECT", "DEGREE PROGRAMME").split("-")[0].trim();
                lg.degree = getFragment("DEGREE PROGRAMME", "ACADEMIC YEAR & SEMESTER").split("-")[1].trim();
                lg.learning_outcomes = getSkills("3.2. Learning outcomes","4. Brief description of the subject and syllabus" );
            }
            lg.autoSetId();
            
        }catch(Exception e)
        {
            return lg;
        }
        
        
        
        return lg;
    }
/*        
*/    
    
    
    private String getFragment(String limite1, String limite2)  {
        String frag="";
        int i0=text.indexOf(limite1);
        int i1=text.indexOf(limite2);
        if (i0!=-1 && i1!=-1)
           frag  = text.substring(i0+limite1.length(),i1);
        return frag;
    }

    private List<String> getSkills(String limite1, String limite2) {
        try {
            String ra = "";
            
            int i0=text.indexOf(limite1);
            int i10=text.indexOf(limite2);
            int i1=text.indexOf(limite2,i10+1);
            if (i0!=-1 && i1!=-1)
                ra = text.substring(i0+limite1.length(),i1);
            String lines[] = ra.split("\\r?\\n");
            return Arrays.asList(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

}
