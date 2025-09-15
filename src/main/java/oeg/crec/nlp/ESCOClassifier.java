package oeg.crec.nlp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.model.Skill;
import oeg.crec.parsers.ParserESCO;
import oeg.crec.store.Courses;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author victor
 */
public class ESCOClassifier {
    
    public static void patchCoursesWithEsco()    
    {
        if (ParserESCO.skills.isEmpty()) {
            ParserESCO.skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        }
        Map<String, List<String>> course_skills = loadCoursesSkills("data/skills/esco/courses-skills.csv");
        for (Course curso : Courses.courses) {
            List<Skill> skills = new ArrayList();
            if (!curso.institution.equals("Universidad Politécnica de Madrid")) {
                continue;
            }
            List<String> ls = course_skills.get(curso.id);
            for(String s : ls)
            {
                Skill skill = ParserESCO.getSkillById(s);
                if (skill!=null)
                {
                    List<Skill> lst = curso.getEsco_skills();
                    lst.add(skill);
                    curso.setEsco_skills(lst);
                }
            }
            System.out.println(curso.id + " " + curso.getEsco_skills().size());
        }
        
        ///Voy a intentar guardarlo
        
        try{
            String sfile = "data/courses/courses3.json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(Courses.courses);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }          
        
        
    }
    
    public static Map<String, List<String>> loadCoursesSkills(String path)  {
        Map<String, List<String>> map = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip empty lines
                String[] parts = line.split("\t");

                if (parts.length < 1) continue; // no id

                String courseId = parts[0].trim();
                List<String> skills = new ArrayList<>();

                // check columns 2, 4, 6, 8 (indexes 1, 3, 5, 7)
                int[] skillColumns = {1, 3, 5, 7};
                for (int col : skillColumns) {
                    if (col < parts.length && !parts[col].trim().isEmpty()) {
                        skills.add(parts[col].trim());
                    }
                }

                map.put(courseId, skills);
            }
        }catch(Exception e)
        {
            ;
        }
        return map;
    }
    public static void refine() {
        

        if (ParserESCO.skills.isEmpty()) {
            ParserESCO.skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        }
        Map<String, List<String>> datos = cargar("data/skills/esco/sinrefinar.csv");
        for (Course curso : Courses.courses) {
            List<Skill> skills = new ArrayList();
            if (!curso.institution.equals("Universidad Politécnica de Madrid")) {
                continue;
            }

            String text = curso.calculateText();

            List<String> tmp = datos.get(curso.id);
            String sskills = String.join(",", tmp);
            String system = "Eres un clasificador. Debes elegir exactamente 5 skills de entre la lista que te doy para describir un curso. Solo quiero las categorías, separadas por comas.";
            String pregunta = "Esta son las categorías (skills): '" + sskills + "'. Y esta es la descripción del curso:'" + text + "'.";
            String resp = DeepSeek.chat(system, pregunta);

            String[] askills = resp.split(",");
            for (String s : askills) {
                Skill sk = ParserESCO.getSkillByName(s.trim());
                if (sk != null) {
                    skills.add(sk);
                    List<Skill> t = curso.getEsco_skills();
                    t.add(sk);
                    curso.setEsco_skills(t);
                    
                }
            }

            System.out.print(curso.id + "\t");
            for (Skill skill : skills) {
                System.out.print(skill.getId() + "\t" + skill.getName() + "\t");
            }

            System.out.print("\n");
        }
        
        try{
            String sfile = "data/courses/courses4.json";
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(Courses.courses);
            FileUtils.writeStringToFile(new File(sfile), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }          
        
    }

    public static List<Skill> clasifica(Course curso) {
        if (ParserESCO.skills.isEmpty()) {
            ParserESCO.skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        }
        List<Skill> found = new ArrayList();

        String text = curso.calculateText();

        String identificados = "";
        for (int i = 0; i < 1300; i += 300) {
            String desc = "";
            for (int j = 0; j < 300; j++) {
                int indice = i + j;
                if (indice < ParserESCO.skills.size()) {
                    Skill skill = ParserESCO.skills.get(indice);
                    desc += skill.name + ", ";
                }
            }
            //       System.out.println(desc);
            String res = DeepSeek.findESCO(text, desc);
            List<String> temp = Arrays.stream(res.split(",")).map(String::trim).collect(Collectors.toList());
            //    System.out.println(i+"/1300 " + temp.size());
            identificados = identificados + ", " + res;
        }
        List<String> skills = Arrays.stream(identificados.split(",")).map(String::trim).collect(Collectors.toList());
        //   System.out.println("Skills identified: " + skills.size());
        System.out.print(curso.id + "\t");
        for (String skill : skills) {
            System.out.print(skill + ", ");
        }
        System.out.println();

//        String res = DeepSeek.chat2(curso, sskils);
        return found;
    }

    public static Map<String, List<String>> cargar(String rutaArchivo) {
        Map<String, List<String>> mapa = new HashMap<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split("\t");
                if (columnas.length >= 2) {
                    String id = columnas[0].trim();
                    String[] skills = columnas[1].split(",");
                    List<String> lista = new ArrayList<>();
                    for (String s : skills) {
                        lista.add(s.trim());
                    }
                    mapa.put(id, lista);
                }
            }
        } catch (Exception e) {

        }

        return mapa;
    }

}
