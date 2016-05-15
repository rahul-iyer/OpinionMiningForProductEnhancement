//package stancore;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahuliyer
 */
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.neural.rnn.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.*;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dependency2 {

    static StanfordCoreNLP pipeline;

    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static int findSentiment(String review) {

        int mainSentiment = 0;
        if (review != null && review.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(review);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }
    public static HashMap<String, List<String>> features = new HashMap<String, List<String>>();
    public static String[] feature_head = {"Camera", "Battery", "Body", "Display", "Audio", "Performance"};

    public static void readCsv() {

        String csvFile = "features.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int i = 0;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                features.put(feature_head[i], new ArrayList<String>());
                String[] featuresCsv = line.split(cvsSplitBy);
                for (String feat : featuresCsv) {
                    if (feat.equals("")) {
                        continue;
                    }
                    features.get(feature_head[i]).add(feat);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Set set = features.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            //System.out.print(me.getKey() + ": ");
            ArrayList<String> values = (ArrayList<String>) me.getValue();
            //System.out.println(Arrays.toString(values.toArray(new String[values.size()])));
        }
    }

    public static boolean multifeatures(String review) {
        boolean found;
        int count = 0;
        for (String feat : feature_head) {
            found = false;
            ArrayList<String> values = (ArrayList<String>) features.get(feat);
            for (String f : values) {
                if (review.toLowerCase().contains(f.toLowerCase())) {
                    found = true;
                }
            }
            if (found) {
                count++;
            }
        }
        if (count > 1) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFeature(String word) {
        for (String feat : feature_head) {
            ArrayList<String> values = (ArrayList<String>) features.get(feat);
            for (String f : values) {
                if (word.equalsIgnoreCase(f)) {
                    return feat;
                }
            }
        }
        return null;
    }
    

    public static void main(String[] args) {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
        Scanner sc = new Scanner(System.in);

        readCsv();
        String text = "";
        text = sc.nextLine();
        if (multifeatures(text)) {
            System.out.println("Multiple features present");
            MaxentTagger tagger = new MaxentTagger(taggerPath);
            DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

            DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
            for (List<HasWord> sentence : tokenizer) {
                List<TaggedWord> tagged = tagger.tagSentence(sentence);
                GrammaticalStructure gs = parser.predict(tagged);

                Collection<TypedDependency> s = gs.typedDependenciesCollapsedTree();
                Map<Character, Pair<Character, Character>> map = new HashMap<Character, Pair<Character, Character>>();
                Object[] z = s.toArray();
                String rels[] = new String[z.length];
                String word1[] = new String[z.length];
                String word2[] = new String[z.length];
                int j = 0;
                String f,f1,f2;
                for (Object i : z) {
                    //System.out.println(i);
                    String temp = i.toString();
                    System.out.println(temp);
                    String pattern0 = "(.*)(?=\\()";
                    String pattern1 = "(?<=\\()(.*?)(?=-)";
                    String pattern2 = "(?<=,)(.*)(?=-)";
                    Pattern r0 = Pattern.compile(pattern0);
                    Pattern r1 = Pattern.compile(pattern1);
                    Pattern r2 = Pattern.compile(pattern2);
                    Matcher m0 = r0.matcher(temp);
                    Matcher m1 = r1.matcher(temp);
                    Matcher m2 = r2.matcher(temp);
                    if (m0.find()) 
                        rels[j] = m0.group(0);
                    if (m1.find()) 
                        word1[j] = m1.group(0);
                    if (m2.find()) 
                        word2[j] = m2.group(0);
                    if (rels[j].equals("amod")){
                        f1 = getFeature(word1[j]);
                        f2 = getFeature(word2[j]);
                        f = f1!=null ? (f1) : (f2!=null ? f2 : null);
                        if(f!=null){
                        System.out.println("Feature: "+f);
                        
                        }
                            
                            
                    } 


                    j++;
                }
                //System.out.println(Arrays.toString(rels));
            }
        } else {
            //sentence score is feature score
        }

    }
}
