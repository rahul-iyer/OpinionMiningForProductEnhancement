/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opinionmining;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.neural.rnn.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author rahuliyer
 */
public class Opinionmining {

    /**
     * @param args the command line arguments
     */
    


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

    
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        Opinionmining.init();
        int t=999999;
        Scanner sc=new Scanner(System.in);
        String review;
        //t=sc.nextInt();
        //while(t!=0)
        //{
         System.out.println("Enter the review");
         review=sc.nextLine();
         System.out.println("The opinion is: " + Opinionmining.findSentiment(review));
//         System.out.println("Enter 1 to continue the analysis or enter 0 to exit:");

        //}
    }
}
