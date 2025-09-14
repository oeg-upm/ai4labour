package oeg.crec.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oeg.crec.model.Skill;

/**
 *
 * @author victor
 */
public class ParserESCO {

    public static List<Skill> skills = new ArrayList();
    
    public static void main(String[] args) {
        skills = ParserESCO.parseSkills("data/skills/esco/ESCO Dataset classification/digitalSkillsCollection_en.csv");
        for (Skill skill : skills) {
            System.out.println(skill.getName());
        }
    }

    public static List<Skill> parseSkills(String filePath) {
        List<Skill> skills = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                Skill skill = new Skill();
                lineCount++;
                // skip the first two rows
                if (lineCount <= 2) {
                    continue;
                }
                // split by comma - simple CSV assumption
                String[] parts = line.split(",", -1); // -1 keeps empty cells
                if (parts.length >= 3) {
                    // take columns 2 and 3 (indexes 1 and 2)
                    skill.setId(parts[1].trim());
                    skill.setName(parts[2].trim());
                    skills.add(skill);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skills;
    }
}
