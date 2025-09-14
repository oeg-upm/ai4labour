package oeg.crec.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oeg.crec.model.Course;
import oeg.crec.model.Skill;
import oeg.crec.parsers.ParserESCO;

/**
 *
 * @author victor
 */
public class ESCOClassifier {
    
    public static List<Skill> clasifica(Course curso)
    {
        if (ParserESCO.skills.isEmpty())
            ParserESCO.skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        List<Skill> found = new ArrayList();

        String text = curso.title+"\n";
        text+=String.join(" ", curso.learning_outcomes);
        text += curso.objective;

        
        String identificados = "";
        for(int i = 0; i< 1300;i+=300)
        {
            String desc = "";
            for(int j=0;j<300;j++)
            {
                int indice = i+j;
                if (indice<ParserESCO.skills.size())
                {
                    Skill skill = ParserESCO.skills.get(indice);
                    desc += skill.name+", ";
                }
            }
     //       System.out.println(desc);
            String res = DeepSeek.findESCO(text, desc);
            identificados +=res;
      //      System.out.println(i+"/1300");
        }
        List<String> skills = Arrays.stream(identificados.split(",")).map(String::trim).collect(Collectors.toList());
     //   System.out.println("Skills identified: " + skills.size());
        System.out.print(curso.id+"\t");
        for(String skill : skills)
        {
            System.out.print(skill+", ");
        }
        System.out.println();
        
//        String res = DeepSeek.chat2(curso, sskils);
        return found;
    }    
    
}
