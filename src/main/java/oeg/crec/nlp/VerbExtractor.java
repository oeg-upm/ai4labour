package oeg.crec.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import oeg.crec.Main;

/**
 * Core NLP. Models live here:
 * https://nlp.stanford.edu/software/stanford-corenlp-4.5.4.zip Models live
 * here:
 * https://search.maven.org/remotecontent?filepath=edu/stanford/nlp/stanford-corenlp/4.4.0/stanford-corenlp-4.4.0-models-spanish.jar
 *
 * @author victor
 */
public class VerbExtractor {

    private StanfordCoreNLP pipeline = null;
    
    public static void main(String[] args) {
        Map<String,Integer> mapa = new HashMap();
        String sen = "I love you dear, but I am killing you. I killed others before.";
        String ses = "Te quiero cariño, pero te mataré. Ya maté a otras.";
        VerbExtractor ven = new VerbExtractor();
        ven.init("en");
        System.out.println("Extractor en inglés frase en inglés");
        print(ven.getVerbs(sen));
        System.out.println("Extractor en inglés frase en español");
        print(ven.getVerbs(ses));

        VerbExtractor ves = new VerbExtractor();
        ves.init("es");
        System.out.println("Extractor en español frase en inglés");
        print(ves.getVerbs(sen));
        System.out.println("Extractor en español frase en español");
        print(ves.getVerbs(ses));
    }
    
   public static void print(Map<String,Integer> map)
   {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key+": "+value);
        }       
   }

    public Map<String,Integer> getVerbs(String text) {
        Map<String, Integer> map = new HashMap();
        CoreDocument document = pipeline.processToCoreDocument(text);
        for (CoreLabel tok : document.tokens()) {
            String pos = tok.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.startsWith("VB") || pos.startsWith("VERB")) {
                String lemma = tok.lemma();
                Integer i = map.get(lemma);
                i = (i == null ? 0 : i);
                map.put(lemma, i + 1);
            }
        }
        return map;

    }
 public void init(String lan)
    {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        props.setProperty("tokenize.language", lan);
        if (lan.equals("es"))
        {
            String modPath = Main.DATAFOLDER + "/nlp/stanford-corenlp-4.5.4/stanford-corenlp-4.5.4-models-spanish/edu/stanford/nlp/models/";
            props.put("pos.model", modPath + "pos-tagger/spanish-ud.tagger");
        }
        else
        {
            String modPath = Main.DATAFOLDER + "/nlp/stanford-corenlp-4.5.4/stanford-corenlp-4.5.4-models/edu/stanford/nlp/models/";
            props.put("pos.model", modPath + "pos-tagger/english-left3words-distsim.tagger");
        }
 //       props.setProperty("tokenize.options", "untokenizable=NoneKeep");
        pipeline = new StanfordCoreNLP(props);      
    }    
}


/* props.put("ner.model", modPath + "ner/english.all.3class.distsim.crf.ser.gz");
    props.put("parse.model", modPath + "lexparser/englishPCFG.ser.gz");
    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
    props.put("sutime.binders","0");
    props.put("sutime.rules", modPath + "sutime/defs.sutime.txt, " + modPath + "sutime/english.sutime.txt");
    props.put("dcoref.demonym", modPath + "dcoref/demonyms.txt");
    props.put("dcoref.states", modPath + "dcoref/state-abbreviations.txt");
    props.put("dcoref.animate", modPath + "dcoref/animate.unigrams.txt");
    props.put("dcoref.inanimate", modPath + "dcoref/inanimate.unigrams.txt");
    props.put("dcoref.big.gender.number", modPath + "dcoref/gender.data.gz");
    props.put("dcoref.countries", modPath + "dcoref/countries");
    props.put("dcoref.states.provinces", modPath + "dcoref/statesandprovinces");
    props.put("dcoref.singleton.model", modPath + "dcoref/singleton.predictor.ser");    */
