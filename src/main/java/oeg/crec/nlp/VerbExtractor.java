/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public static void main(String[] args) {
        getVerbs("I love you dear, but I am killing you. I killed others before.");
    }

    public static Map<String,Integer> getVerbs(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        props.setProperty("tokenize.language", "en");
        String folder = Main.DATAFOLDER + "/nlp/stanford-corenlp-4.5.4/stanford-corenlp-4.5.4-models.jar";
        String modPath = Main.DATAFOLDER + "/nlp/stanford-corenlp-4.5.4/stanford-corenlp-4.5.4-models/edu/stanford/nlp/models/";
        props.put("pos.model", modPath + "pos-tagger/english-left3words-distsim.tagger");
        Map<String, Integer> map = new HashMap();

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
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = pipeline.processToCoreDocument(text);
        for (CoreLabel tok : document.tokens()) {
            // System.out.println(String.format("%s\t%s", tok.word(), tok.tag()));
            String word = tok.get(CoreAnnotations.TextAnnotation.class);
            String pos = tok.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.startsWith("VB")) {
                String lemma = tok.lemma();
      //          System.out.println(lemma);
                Integer i = map.get(lemma);
                i = (i == null ? 0 : i);
                map.put(lemma, i + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
        //    System.out.println("Key: " + key + " | Value: " + value);
        }
        return map;

    }
}
