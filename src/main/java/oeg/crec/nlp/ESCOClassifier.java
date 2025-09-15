package oeg.crec.nlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oeg.crec.model.Course;
import oeg.crec.model.Skill;
import oeg.crec.parsers.ParserESCO;
import oeg.crec.store.Courses;

/**
 *
 * @author victor
 */
public class ESCOClassifier {

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

            String text = curso.getText();

            List<String> tmp = datos.get(curso.id);
            String sskills = String.join(",", tmp);
            String system = "Eres un clasificador. Debes elegir exactamente 3 skills de entre la lista que te doy para describir un curso. Solo quiero las categorías, separadas por comas.";
            String pregunta = "Esta son las categorías (skills): '" + sskills + "'. Y esta es la descripción del curso:'" + text + "'.";
            String resp = DeepSeek.chat(system, pregunta);

            String[] askills = resp.split(",");
            for (String s : askills) {
                Skill sk = ParserESCO.getSkill(s.trim());
                if (sk != null) {
                    skills.add(sk);
                }
            }

            System.out.print(curso.id + "\t");
            for (Skill skill : skills) {
                System.out.print(skill.getId() + "\t" + skill.getName() + "\t");
            }

            System.out.print("\n");
        }
    }

    public static List<Skill> clasifica(Course curso) {
        if (ParserESCO.skills.isEmpty()) {
            ParserESCO.skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        }
        List<Skill> found = new ArrayList();

        String text = curso.getText();

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
