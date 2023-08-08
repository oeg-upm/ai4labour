package oeg.crec.upm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oeg.crec.model.LearningGuide;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Methods to parse a UPM Guía de Aprendizaje
 * @author victor
 */
public class ParserGuiaAprendizaje {

    String text = "";
    
    public static void main(String arg[]) {
        String input = "d:\\guia.pdf";
        ParserGuiaAprendizaje parser = new ParserGuiaAprendizaje();
        LearningGuide lg = parser.parse(input);
        System.out.println(lg);

    }
    
    public LearningGuide parse(String sfile)
    {
        LearningGuide lg = new LearningGuide();
        lg.institution = "Universidad Politécnica de Madrid";
        
        try {
            File file = new File(sfile);
            PDDocument doc = PDDocument.load(file);
            text = new PDFTextStripper().getText(doc);
            lg.title = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[1].trim();
            lg.local_id = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[0].trim();
            lg.learning_outcome = getSkills();
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

    private List<String> getSkills() {
        try {
            String ra = "";
            String limite1 = "3.2. Resultados del aprendizaje";
            String limite2 = "4. Descripción de la asignatura y temario";
            
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
