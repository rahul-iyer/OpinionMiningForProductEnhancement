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
import java.lang.Object;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Dependency  {

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
    public static int NNC(String[] rels,String x){
        int count=0;
        for(int i=0;i<rels.length;i++){
            if(rels[i]==x)
                count++;
        }
        return count;
    }
    
    public static List<Integer> grRecog(String[] rels,String x)
    {
        List l=new ArrayList();
        for(int i=0;i<rels.length;i++){
           // System.out.println(rels[i]+","+x);
            if(rels[i].equals(x)){
                //System.out.println(rels[i]);
                l.add(i);
            }
        }
        return l;
    }
    
    public static String posrecog(String[] token,String[] pos,String srch){
        for(int i=0;i<token.length;i++){
            //System.out.println(token[i]+","+srch);
            if(token[i].equalsIgnoreCase(srch)){
               // System.out.println(srch);
                return pos[i];
            }
        }
        return "NULL";
    }
    public static int[] toIntArray(List<Integer> list){
 
        int[] ret = new int[list.size()];
  
        for(int i = 0;i < ret.length;i++)
    
            ret[i] = list.get(i);
  
        return ret;

    }
    public static int compgrRecog(String[] reln,String[] word1,String[] word2,String srch,String word){
        for(int i=0;i<reln.length;i++){
           // System.out.println(srch+","+word);
            if(reln[i].equals(srch) && word1[i].equals(word))
                return i;
        }
     return -1;   
    }
    
    
    
    
    

  public static void main(String[] args) {
    String modelPath = DependencyParser.DEFAULT_MODEL;
    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
    Scanner sc=new Scanner(System.in);
    

    String text = "";
    text=sc.nextLine();
   // while(text!="exit"){

    MaxentTagger tagger = new MaxentTagger(taggerPath);
    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
    for (List<HasWord> sentence : tokenizer) {
        
      List<TaggedWord> tagged = tagger.tagSentence(sentence);
        Object[] x = tagged.toArray();
      GrammaticalStructure gs = parser.predict(tagged);
      //System.out.println();
      
        Collection<TypedDependency> s=gs.typedDependenciesCollapsedTree();
        Object[] z = s.toArray();
        
        
        
        
        System.out.println(tagged.toString());
        String token[]=new String[z.length];
        String pos[]=new String[z.length];
        int k=0;
        for(Object i:x){
            String str=i.toString();
            /*String temp0="(.*?)(?=\\/)";
            String temp1="\\/(.*)";
            
            System.out.println(str);
            Pattern t0 = Pattern.compile("(.*?)(?=\\/)");
            Pattern t1 = Pattern.compile("\\/(.*)");
            Matcher m0 = t0.matcher(str);
            Matcher m1 = t1.matcher(str);*/
            int index=str.lastIndexOf('/');
            token[k]=str.substring(0, index);
            pos[k]=str.substring(index+1);
            //System.out.println(pos[k]);
            k++;
    }
        String rels[]=new String[z.length];
        String word1[]=new String[z.length];
        String word2[]=new String[z.length];
        int j=0;
        for(Object i:z){
            System.out.println(i);
            String temp=i.toString();
            String pattern0="(.*)(?=\\()";
            String pattern1="(?<=\\()(.*?)(?=-)";
            String pattern2="(?<=, )(.*)(?=-)";
            Pattern r0 = Pattern.compile(pattern0);
            Pattern r1 = Pattern.compile(pattern1);
            Pattern r2 = Pattern.compile(pattern2);
            Matcher m0 = r0.matcher(temp);
            Matcher m1 = r1.matcher(temp);
            Matcher m2 = r2.matcher(temp);
            if(m0.find()){
                rels[j]=m0.group(0);
                //System.out.println(rels[j]);
            }
            if(m1.find()){
                word1[j]=m1.group(0);
            }
            if(m2.find()){
                word2[j]=m2.group(0);
            }
            j++;
        }
      //System.out.println(s);
        //Rules for feature extraction.
        //rule1:::::::::::::::::
        //System.out.println("1");
        int[] q=toIntArray(grRecog(rels,"nsubj"));
        //System.out.println("2");
        if(q.length!=0){
            //System.out.println("3");
            if(posrecog(token,pos,word2[q[0]]).equals("NN"))
            {
                //System.out.println("4");
                int[] w=toIntArray(grRecog(rels,"compound"));
                //System.out.println("5");
                if(w.length!=0){
                    System.out.println("6");
                    System.out.println(word1[q[0]]+","+word2[q[0]]+","+word2[w[0]]);
                }
                else
                {
                    int conj_and_index=compgrRecog(rels,word1,word2,"conj:and",word2[q[0]]);
                    if(conj_and_index!=-1){
                        System.out.println(word1[conj_and_index]+","+word2[conj_and_index]+","+word2[q[0]]);
                    }
                    else
                    System.out.println(word1[q[0]]+","+word2[q[0]]);
                }
            }
            //RULE 2:::::::::::::
            else if(posrecog(token,pos,word1[q[0]]).equals("JJ")){
                //System.out.println("aaaaa_JJ");
                int a= compgrRecog(rels,word1,word2,"xcomp",word1[q[0]]);   
                if(a!=-1){
                    int b=compgrRecog(rels,word1,word2,"dobj",word2[a]);
                            if(b!=-1){
                                int c=compgrRecog(rels,word1,word2,"compound",word2[b]);
                                if(c!=-1){
                                    System.out.println(word1[q[0]]+","+word1[c]+","+word2[c]);
                                }
                            }
                }
                //RULE 3::::::::::
                else{
                    int b[]=toIntArray(grRecog(rels,"ccomp"));
                            if(b.length!=0){
                                    System.out.println(word1[q[1]]+","+word2[q[1]]+","+word1[b[0]]);
                            }
                            
                }
                }
            //RULE 4::::::::::
            else if(posrecog(token,pos,word1[q[0]]).equals("VBZ")){
                //System.out.println("aaaaa");
                int vbp_dobj_index=compgrRecog(rels,word1,word2,"dobj",word2[q[0]]);
                if(vbp_dobj_index!=-1){
                    System.out.println(word1[vbp_dobj_index]+","+word2[vbp_dobj_index]);
                }
                else {
                    int vbp_xcomp_index=compgrRecog(rels,word1,word2,"xcomp",word1[q[0]]);
                        if(vbp_xcomp_index!=-1){
                            
                            System.out.println(word1[vbp_xcomp_index]+","+word2[vbp_xcomp_index]);
                        }
                        else
                        {
                            int vbp_acomp_index=compgrRecog(rels,word1,word2,"acomp",word1[q[0]]);
                            if(vbp_acomp_index!=-1){
                            
                            System.out.println(word1[q[0]]+","+word1[vbp_acomp_index]+","+word2[vbp_acomp_index]);
                        }
                            else
                                System.out.println(word1[q[0]]);
                        
                        }
                            
                        }
                
                        
                        
                
            }
            int[] f=toIntArray(grRecog(rels,"amod"));
            if(f.length!=0)
            {
                for(int i:f){
                    System.out.println(word1[i]+","+word2[i]);
                }
                int cj[]=toIntArray(grRecog(rels,"conj:and"));
                if(cj.length!=0){
                    for(int i:cj){
                    System.out.println(word1[i]+","+word2[i]);
                }
                }
            }
            int[] neg=toIntArray(grRecog(rels,"neg"));
            if(neg.length!=0)
            {
                for(int i:neg){
                    System.out.println(word1[i]+","+word2[i]);
                }
                
                
            }
            
        }
        else
        {
            int[] f=toIntArray(grRecog(rels,"amod"));
            if(f.length!=0)
            {
                for(int i:f){
                    System.out.print(word1[i]+","+word2[i]);
                    String qwe=word1[i]+","+word2[i];
                }
                int cj[]=toIntArray(grRecog(rels,"conj:and"));
                if(cj.length!=0){
                    for(int i:cj){
                    System.out.println(word2[i]);
                    
                }
                }
            }
            int[] neg=toIntArray(grRecog(rels,"neg"));
            if(neg.length!=0)
            {
                for(int i:neg){
                    System.out.println(word1[i]+","+word2[i]);
                }
                
                
            }
            
            
            
        }
        
        //RULE 2:::::::::::::
        
        
    }
    
    
    
  //  text=sc.nextLine();
  //}
  }
}
