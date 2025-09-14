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
    public static final List<String> analyze = Arrays.asList("analyze"," analyze","audit","blueprint","breadboard","break down","characterize","classify","compare","confirm","contrast","correlate","detect","diagnose","diagram","differentiate","discriminate","dissect","distinguish","document","ensure","examine","explain","explore","figure out","file","group","identify","illustrate","infer","interrupt","validate","verify","","","","","","","","","","overhaul","plan","portray","prepare","prescribe","produce","program","rearrange","reconstruct","relate","reorganize","revise","rewrite","specify","summarize");
    public static final List<String> evaluate = Arrays.asList("evaluate","appraise","assess","compare","conclude","contrast","counsel","criticize","critique","defend","determine","discriminate","estimate","evaluate","explain","grade","hire","interpret","judge","justify","measure","predict","prescribe","rank","rate","recommend","release","select","summarize","support","test","improve","incorporate","integrate","interface","join","lecture","model","modify","network","organize","outline");
    public static final List<String> create = Arrays.asList("create","abstract","animate","arrange","assemble","budget","categorize","code","combine","compile","compose","construct","cope","correspond","create","cultivate","debug","depict","design","develop","devise","dictate","enhance","explain","facilitate","format","formulate","generalize","generate","handle","import");

    public static final List<String> rememberes = Arrays.asList("recordar", "citar","definar","describir","dibujar","enumerar","identificar","memorizar","indicar","etiquetar","listar","asignar","caracterizar","nombrar","recitar","resumir","citar","leer","recordra","memorizar","reconocer","acordar","repetir","reproducir","revisar","seleccionar","afirmar","estudiar","tabular","trazar","escribir","interpretar","observar","parafrasear","encontrar","predecir","revisar","reescribir","restar","resumir","traducir","visualizar","personalizar","dibujar","graficar","predecir","preparar","justificar","procesar","producir","proyectar","proporcionra","relatar","narrar","secuenciar","mostrar","simular","bosquejar","resolver","suscribir","tabular","transcribir","traducir","usar");
    public static final List<String> understandes = Arrays.asList("entender","añadir","aproximar","articular","asociar","caracterizar","aclarar","clasificar","comparar","computar","contrastar","convertir","defender","describir","detallar","diferenciar","discutir","identificar","elaborar","estimar","ejemplificar","explicar","expresar","extender","extrapolar","reescribir","generalizar","dar","inferir","interactuar","interpolar","expresar","factorizar","figurar","graficar","manipular","ilustrar","convertir","investigar","manipular","modificar","operar","revisar","consultar","relacionar","seleccionar","separar","subdividir","entrenar","transformar");
    public static final List<String> applyes = Arrays.asList("aplicar","adquirir","adaptar","ubicar","alfabetizar","determinar","asignar","obtener","evitar","respaldar","calcular","capturar","desarrollar","cambiar","clasificar","completar","computar","programar","demostrar","implementar","derivar","determinar","reducir","simplificar","descubrir","dibujar","emplear","examinar","explorar","exponer","inventar","investigar","trazar","gestionar","maximizar","optimizar","minimizar","ordenar","compilar","calibrar","priorizar");
    public static final List<String> analyzees = Arrays.asList("analizar","auditar","ponderar","considerar","particularizar","caracterizar","clasificar","comparar","confirmar","contrastar","correlar","detectar","diagnosticar","dibujar","diferenciar","discriminar","diseccionar","distinguir","documentar","asegurar","examinar","explicar","explorar","archivar","implementar","agrupar","identificar","ilustrar","inferir","interrumpir","validar","verificar");
    public static final List<String> evaluatees = Arrays.asList("evaluar","valorar","assess","comparar","concluir","contrastar","aconsejar","criticar","critique","defender","determinar","discriminar","estimular","evaluar","explicar","puntuar","contratar","interpretar","juzgar","justificar","medir","predecir","anticipar","prescribir","ordenar","recomendar","lanzar","seleccionar","resumir","apoyar","testear","mejorar","incorporar","integrar","interface","combinar","presentar","modelar","modificar","conectar","organize","outline");
    public static final List<String> createes = Arrays.asList("crear","abstraer","animar","diseñar","ensamblar","presupuestar","categorizar","codificar","combinar","compilar","componer","construir","resolver","corresponder","crear","cultivar","depurar","mostrar","diseñar","desarollar","concebir","dictar","mejorar","explicar");

    VerbExtractor ve = null;
    String lan = "en";
    
    public static void main(String arg[])
    {
        Main.init();
        Course c = Courses.get("103000826");

        Bloom bloom = new Bloom("en");
        List<Float> res = bloom.getBloom(c);
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (Float value : res) {
            System.out.println(decimalFormat.format(value));
        }        
    }
    public Bloom(String language)
    {
        lan = language;
        ve = new VerbExtractor();
        ve.init(language);
    }
    
    /**
     * Makes the standard Bloom analysis.
     * @return remember understand apply analyse evaluate create
    */
    public List<Float> getBloom(Course c)
    {
        List<Float> res = new ArrayList();
        StringBuilder concatenatedString = new StringBuilder();
        for (String str : c.learning_outcomes) {
            concatenatedString.append(str);
        }        
        String text = concatenatedString.toString();       
        int irem=0, iund=0, iapp=0, iana=0, ieva=0, icre=0;

        if (lan.equals("en"))
        {
            irem = countHits(text, "remember", remember);
            iund = countHits(text, "understand", understand);
            iapp = countHits(text, "apply", apply);
            iana = countHits(text, "analyze", analyze);
            ieva = countHits(text, "evaluate", evaluate);
            icre = countHits(text, "create", create);
        }
        if (lan.equals("es"))
        {
            irem = countHits(text, "remember", rememberes);
            iund = countHits(text, "understand", understandes);
            iapp = countHits(text, "apply", applyes);
            iana = countHits(text, "analyze", analyzees);
            ieva = countHits(text, "evaluate", evaluatees);
            icre = countHits(text, "create", createes);
            
        }
        
        int tot = irem+iund+iapp+iana+ieva+icre;
    //    System.out.println(text);
    //    System.out.println(irem +" "+ iund+" "+iapp+" "+iana+" "+ieva+" "+icre);
        res.add(tot==0 ? 0F : Float.valueOf(irem) /Float.valueOf(tot));
        res.add(tot==0 ? 0F :Float.valueOf(iund) /Float.valueOf(tot));
        res.add(tot==0 ? 0F :Float.valueOf(iapp) /Float.valueOf(tot));
        res.add(tot==0 ? 0F :Float.valueOf(iana) /Float.valueOf(tot));
        res.add(tot==0 ? 0F :Float.valueOf(ieva) /Float.valueOf(tot));
        res.add(tot==0 ? 0F :Float.valueOf(icre) /Float.valueOf(tot));
        return res;

    }
    
    private int countHits(String text, String category, List<String> lista)
    {
        int conta=0;
        if (text.isEmpty())
            return 0;
        Map<String, Integer> map = ve.getVerbs(text);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            key = key.toLowerCase();
            if (lista.contains(key))
                conta+=value;
        }
        return conta;
    }
    
}
