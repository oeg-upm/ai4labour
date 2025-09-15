package oeg.crec;

import java.util.ArrayList;
import java.util.List;
import static oeg.crec.Main.init;
import oeg.crec.bloom.Bloom;
import oeg.crec.model.Course;
import oeg.crec.model.Skill;
import oeg.crec.nlp.DeepSeek;
import oeg.crec.nlp.ESCOClassifier;
import oeg.crec.store.Courses;

/**
 *
 * @author victor
 */
public class Statistics {
    public static List<Skill> escoskills = new ArrayList();

    public static void main(String[] args) {
        init();
        ESCOClassifier.refine();
    }
    
    
    public static void run3()
    {
        System.out.println(java.time.LocalDateTime.now());
        for (Course c : Courses.courses)
        {
            if (!c.institution.equals("Universidad Politécnica de Madrid"))
                continue;
            
            List<Skill> skills = ESCOClassifier.clasifica(c);
            for(Skill skill : skills)
            {
                System.out.println(skill.name);
            }
            
       //     break;
        }
        System.out.println(java.time.LocalDateTime.now());
    }
    public static void run2()
    {
        String tarea="You are a classifier, give me just one word for the category. Categories are the Bloom taxonomy categories: remember understand apply analyse evaluate create.";
        for (Course c : Courses.courses)
        {
            if (!c.institution.equals("Universidad Politécnica de Madrid"))
                continue;
            String cat="empty";            
                String lo = String.join(" ", c.learning_outcomes);
            if (!c.learning_outcomes.isEmpty())
            {
                try{
                cat = DeepSeek.chat(tarea, lo);
                }catch(Exception e)
                {
                    cat="error";
                }
            }
            System.out.println(c.id+"\t"+cat+"\t"+lo); 
        }
        
    }
    
    public static void run1()
    {
        Bloom bloomes = new Bloom("es");
        Bloom bloomen = new Bloom("en");
        
        
        for (Course c : Courses.courses)
        {
            if (!c.institution.equals("Universidad Politécnica de Madrid"))
                continue;
            List<Float> bloom = new ArrayList();
            if (c.lan.equals("en"))
                bloom = bloomen.getBloom(c);
            if (c.lan.equals("es"))
                bloom = bloomes.getBloom(c);
            String nivel = "undergraduate";
            if (c.degree.contains("Master"))
                nivel = "master";
                
            System.out.print(c.id +"\t"+c.title+"\t"+c.lan+"\t"+nivel+"\t");
            System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n", bloom.get(0), bloom.get(1), bloom.get(2), bloom.get(3), bloom.get(4), bloom.get(5));
            
        }        
    }

}
