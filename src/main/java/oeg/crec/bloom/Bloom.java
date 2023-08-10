package oeg.crec.bloom;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import oeg.crec.Main;
import oeg.crec.model.Course;
import oeg.crec.nlp.VerbExtractor;
import oeg.crec.store.Courses;

/**
 * Lista de verbos según https://tips.uark.edu/blooms-taxonomy-verb-chart/.
 * https://www.valamis.com/hub/blooms-taxonomy
 * @author victor
 */
public class Bloom {

    public static final List<String> remember = Arrays.asList("remember", "cite","define","describe","draw","enumerate","identify","index","indicate","label","list","match","meet","name","outline","point","quote","read","recall","recite","recognize","record","repeat","reproduce","review","select","state","study","tabulate","trace","write","interpret","observe","paraphrase","picture graphically","predict","review","rewrite","subtract","summarize","translate","visualize","personalize","plot","practice","predict","prepare","price","process","produce","project","provide","relate","round off","sequence","show","simulate","sketch","solve","subscribe","tabulate","transcribe","translate","use");
    public static final List<String> understand = Arrays.asList("understand","add","approximate","articulate","associate","characterize","clarify","classify","compare","compute","contrast","convert","defend","describe","detail","differentiate","discuss","distinguish","elaborate","estimate","example","explain","express","extend","extrapolate","factor","generalize","give","infer","interact","interpolate","express","factor","figure","graph","handle","illustrate","interconvert","investigate","manipulate","modify","operate","proofread","query","relate","select","separate","subdivide","train","transform");
    public static final List<String> apply = Arrays.asList("apply","acquire","adapt","allocate","alphabetize","apply","ascertain","assign","attain","avoid","back up","calculate","capture","change","classify","complete","compute","construct","customize","demonstrate","depreciate","derive","determine","diminish","discover","draw","employ","examine","exercise","explore","expose","inventory","investigate","layout","manage","maximize","minimize","optimize","order","outline","point out","prioritize");
    public static final List<String> analyze = Arrays.asList("analyze"," analyze","audit","blueprint","breadboard","break down","characterize","classify","compare","confirm","contrast","correlate","detect","diagnose","diagram","differentiate","discriminate","dissect","distinguish","document","ensure","examine","explain","explore","figure out","file","group","identify","illustrate","infer","interrupt","validate","verify","","","","","","","","","","overhaul","plan","portray","prepare","prescribe","produce","program","rearrange","reconstruct","relate","reorganize","revise","rewrite","specify","summarize","","","","","","","");
    public static final List<String> evaluate = Arrays.asList("evaluate","appraise","assess","compare","conclude","contrast","counsel","criticize","critique","defend","determine","discriminate","estimate","evaluate","explain","grade","hire","interpret","judge","justify","measure","predict","prescribe","rank","rate","recommend","release","select","summarize","support","test","improve","incorporate","integrate","interface","join","lecture","model","modify","network","organize","outline");
    public static final List<String> create = Arrays.asList("create","abstract","animate","arrange","assemble","budget","categorize","code","combine","compile","compose","construct","cope","correspond","create","cultivate","debug","depict","design","develop","devise","dictate","enhance","explain","facilitate","format","formulate","generalize","generate","handle","import");

    
    public static void main(String arg[])
    {
        Main.init();
        Course c = Courses.get("103000826");
        List<Float> res = getBloom(c);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (Float value : res) {
            System.out.println(decimalFormat.format(value));
        }        
        
    }
    
    public static List<Float> getBloom(Course c)
    {
        List<Float> res = new ArrayList();
        StringBuilder concatenatedString = new StringBuilder();
        for (String str : c.learning_outcomes) {
            concatenatedString.append(str);
        }        
        String text = concatenatedString.toString();        
        int irem = countHits(text, "remember", remember);
        int iund = countHits(text, "understand", understand);
        int iapp = countHits(text, "apply", apply);
        int iana = countHits(text, "analyze", analyze);
        int ieva = countHits(text, "evaluate", evaluate);
        int icre = countHits(text, "create", create);
        int tot = irem+iund+iapp+iana+ieva+icre;
    //    System.out.println(text);
    //    System.out.println(irem +" "+ iund+" "+iapp+" "+iana+" "+ieva+" "+icre);
        res.add(Float.valueOf(irem) /Float.valueOf(tot));
        res.add(Float.valueOf(iund) /Float.valueOf(tot));
        res.add(Float.valueOf(iapp) /Float.valueOf(tot));
        res.add(Float.valueOf(iana) /Float.valueOf(tot));
        res.add(Float.valueOf(ieva) /Float.valueOf(tot));
        res.add(Float.valueOf(icre) /Float.valueOf(tot));
        return res;

    }
    
    private static int countHits(String text, String category, List<String> lista)
    {
        int conta=0;
        Map<String, Integer> map = VerbExtractor.getVerbs(text);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            key = key.toLowerCase();
            if (lista.contains(key))
                conta+=value;
//            System.out.println(key+" "+ category+" "+conta);
        }
        return conta;
    }
    
}
