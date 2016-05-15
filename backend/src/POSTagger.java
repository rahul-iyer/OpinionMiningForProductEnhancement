
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.lang.String;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahuliyer
 */
public class POSTagger {

    /**
     * @param args the command line arguments
     */
    static String text="";
    static int token_length;
    static String arr1[],arr2[],add_info="";
    public static void main(String[] args) {
        // TODO code application logic here
        
       Properties props = new Properties();
       props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
       StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
       System.out.println("Enter the text:");
       Scanner sc= new Scanner (System.in);
       text=sc.nextLine();
       
    while(text!="exit")
    {
       Annotation document=new Annotation(text); 
       pipeline.annotate(document); 
       
       List<CoreMap> sentences = document.get(SentencesAnnotation.class);
       
       for(CoreMap sentence: sentences) {
          
          
          
         // System.out.println("Size"+token_length);
          //Tree x=sentence.get(TreeAnnotation.class);
          //System.out.print(x);
           //Map<String,String> mp= Map<String,String>();
  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
   
    String word = token.get(TextAnnotation.class);
    
    String pos = token.get(PartOfSpeechAnnotation.class);
    //mp.put(token.toString(), pos);
    System.out.println(pos);
//    String ner = token.get(NamedEntityTagAnnotation.class);
   /* if(pos.equals("JJ")||pos.equals("JJR")||pos.equals("JJS"))
        add_info="adjective";
    else if(pos.equals("RBR")||pos.equals("RB")||pos.equals("RBS"))
        add_info="adverb";
    else
        add_info="";
    System.out.println(word+" "+pos+" "+add_info);
    
    /*if(pos=="JJ"||pos=="JJR"||pos=="JJS")
    {
      arr1[i++] =word; 
      System.out.println("Arr 1"+(i-1)+" "+arr1[i-1]);
    }
    else if(pos=="RBR"||pos=="RB"||pos=="RBS")
    {
        arr2[j++]=word;
    }
    */
    
   
  
    
   /* System.out.println("The adjectives are ");
    for(int k=0;k<(POSTagger.token_length);k++)
    {
        System.out.println(arr1[k]);
    }
    System.out.println("The adverbs are ");Hi 
    for(int k=0;k<(POSTagger.token_length);k++)
    {
        System.out.println(arr2[k]);
    }*/
    }
       System.out.println("Enter the text:");
       text=sc.nextLine();  
    }
}
}
}