/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahuliyer
 */
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahuliyer
 */
public class Treeparse {

    /**
     * @param args the command line arguments
     */
    static String text="";
    static int token_length;
    static String arr1[],arr2[],add_info="";
    
    
    public static List<Tree> GetNounPhrases(Tree parse)
{

    List<Tree> phraseList=new ArrayList<Tree>();
    for (Tree subtree: parse)
    {
      if(subtree.label().value().equals("NP"))
      {
          String str=subtree.toString();
          
          if(str.contains("JJ")||str.contains("JJR")||str.contains("JJS")||str.contains("RB")||str.contains("RBR")||str.contains("RBS")){
            phraseList.add(subtree);
            
          }
      }
    }

      return phraseList;

}
    
    
    
    
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
       Properties props = new Properties();
       props.setProperty("annotators", "tokenize, ssplit, pos, lemma,parse");
       StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
       System.out.println("Enter the text:");
       Scanner sc= new Scanner (System.in);
       text=sc.nextLine();
       
    //while(text!="exit")
    //{
       Annotation document=new Annotation(text); 
       pipeline.annotate(document); 
       
       List<CoreMap> sentences = document.get(SentencesAnnotation.class);
       
       for(CoreMap sentence: sentences) {
          
          token_length=sentence.get(TokensAnnotation.class).size();
          arr1=new String[POSTagger.token_length];
          arr2=new String[POSTagger.token_length];
          int i=0,j=0;
          
         // System.out.println("Size"+token_length);
  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
   
    String word = token.get(TextAnnotation.class);
    
    String pos = token.get(PartOfSpeechAnnotation.class);
//    String ner = token.get(NamedEntityTagAnnotation.class);
     
    
   
  }
     Tree tree = sentence.get(TreeAnnotation.class);
    // System.out.println(tree);
     List<Tree> x=GetNounPhrases(tree);
     System.out.println(x);
     
     // Print words and Pos Tags
        /*for (Tree leaf : leaves) { 
            Tree parent = leaf.parent(tree);
            System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
        }*/
       }
       //System.out.println("Enter the text:");
       //text=sc.nextLine();  
    
}
}