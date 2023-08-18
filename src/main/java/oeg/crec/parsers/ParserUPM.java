package oeg.crec.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oeg.crec.Main;
import oeg.crec.Misc;
import oeg.crec.bloom.Bloom;
import oeg.crec.model.Course;
import oeg.crec.store.Courses;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Methods to parse a UPM Guía de Aprendizaje
 *
 * @author victor
 */
public class ParserUPM {

    String text = "";

    public static void main(String arg[]) {
//        download();
        generateJSON();
    }

    /**
     * Parses all the PDFs and generates a single JSON.
     */
    public static void generateJSON() {
        List<Course> courses = new ArrayList();
        String pdffolder = Main.DATAFOLDER + "/courses/upm";
        Bloom bloomen = new Bloom("en"); //course.lan
        Bloom bloomes = new Bloom("es"); //course.lan

        File dir = new File(pdffolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!child.getAbsolutePath().endsWith(".pdf")) {
                    continue;
                }
                Course course = new ParserUPM().parse(child.getAbsolutePath());
                if (course.lan.equals("es")) {
                    course.bloom = bloomes.getBloom(course);
                } else {
                    course.bloom = bloomen.getBloom(course);
                }
                courses.add(course);
            }
        } else {
        }
        String sfile = Main.DATAFOLDER + "/courses/upm/courses.json";
        Courses.escribir(courses, sfile);
    }

    public static void download() {
        //https://www.fi.upm.es/?pagina=2749 science in health
        //https://www.fi.upm.es/?pagina=2873 matematicas e informatica
        //https://www.fi.upm.es/?pagina=2875 grado en ciencia de datos e ia
        //https://www.fi.upm.es/?pagina=2877 informatica y ade
        String url = "https://www.fi.upm.es/?pagina=2877";
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");
            for (Element link : links) {
                String href = link.attr("href");
                if (!href.contains("/publico")) {
                    continue;
                }
                System.out.println(href);
                Misc.download(href);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Parses one individual PDF file with a learning guide in the UPM format.
     */
    public Course parse(String sfile) {
        Course lg = new Course();
        lg.institution = "Universidad Politécnica de Madrid";
        try {
            File file = new File(sfile);
            PDDocument doc = PDDocument.load(file);
            text = new PDFTextStripper().getText(doc);
            lg.lan = (text.contains("GUÍA DE APRENDIZAJE")) ? "es" : "en";
            lg.link = "https://ai4labour.linkeddata.es/data/courses/upm/" + file.getName();
            if (lg.lan.equals("es")) {
                lg.title = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[1].trim();
                lg.local_id = getFragment("ASIGNATURA", "PLAN DE ESTUDIOS").split("-")[0].trim();
                lg.degree = getFragment("PLAN DE ESTUDIOS", "CURSO ACADÉMICO Y SEMESTRE").split("-")[1].trim();
                lg.learning_outcomes = getSkills("Resultados del aprendizaje", "Descripción de la asignatura y temario");
                lg.objective = getFragment("1. Descripción de la asignatura", "2. Temario de la asignatura");
//                lg.contents = getFragment("2. Temario de la asignatura", "5. Cronograma");
            } else {
                lg.title = getFragment("SUBJECT", "DEGREE PROGRAMME").split("-")[1].trim();
                lg.local_id = getFragment("SUBJECT", "DEGREE PROGRAMME").split("-")[0].trim();
                lg.degree = getFragment("DEGREE PROGRAMME", "ACADEMIC YEAR & SEMESTER").split("-")[1].trim();
                lg.learning_outcomes = getSkills("Learning outcomes", "Brief description of the subject and syllabus");
            }
            lg.autoSetId();

        } catch (Exception e) {
            return lg;
        }
        return lg;
    }

    /**
     * ******************************************************************************************
     */
    private String getFragment(String limite1, String limite2) {
        String frag = "";
        int i0 = text.indexOf(limite1);
        int i1 = text.indexOf(limite2);
        if (i0 != -1 && i1 != -1) {
            frag = text.substring(i0 + limite1.length(), i1);
        }
        return frag;
    }

    /* private String getFragment2(String limite1, String limite2)  {
        String frag="";
        int i0=text.indexOf(limite1);
        int i10=text.indexOf(limite2);
        int i1=text.indexOf(limite2,i10+1);
            
        if (i0!=-1 && i1!=-1)
           frag  = text.substring(i0+limite1.length(),i1);
        return frag;
    }*/

    private List<String> getSkills(String limite1, String limite2) {
        try {
            String ra = "";

            int i0 = text.indexOf(limite1);
            int i10 = text.indexOf(limite2);
            int i1 = text.indexOf(limite2, i10 + 1);
            if (i0 != -1 && i1 != -1) {
                ra = text.substring(i0 + limite1.length(), i1);
            }
            String lines[] = ra.split("\\r?\\n");
            return Arrays.asList(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

}
