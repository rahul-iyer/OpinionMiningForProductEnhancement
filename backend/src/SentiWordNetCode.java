//import static POSTagger.text;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class SentiWordNetCode {

  private Map<String, Double> dictionary;

  public SentiWordNetCode(String pathToSWN) throws IOException {
  
    dictionary = new HashMap<String, Double>();

  
    HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

    BufferedReader csv = null;
    try {
      csv = new BufferedReader(new FileReader(pathToSWN));
      int lineNumber = 0;

      String line;
      while ((line = csv.readLine()) != null) {
	lineNumber++;


	if (!line.trim().startsWith("#")) {

	  String[] data = line.split("\t");
	  String wordTypeMarker = data[0];

	  if (data.length != 6) {
	    throw new IllegalArgumentException(
					       "Incorrect tabulation format in file, line: "
					       + lineNumber);
	  }


	  Double synsetScore = Double.parseDouble(data[2])
	    - Double.parseDouble(data[3]);


	  String[] synTermsSplit = data[4].split(" ");


	  for (String synTermSplit : synTermsSplit) {

	    String[] synTermAndRank = synTermSplit.split("#");
	    String synTerm = synTermAndRank[0] + "#"
	      + wordTypeMarker;

	    int synTermRank = Integer.parseInt(synTermAndRank[1]);
	    if (!tempDictionary.containsKey(synTerm)) {
	      tempDictionary.put(synTerm,
				 new HashMap<Integer, Double>());
	    }


	    tempDictionary.get(synTerm).put(synTermRank,
					    synsetScore);
	  }
	}
      }


      for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
	     .entrySet()) {
	String word = entry.getKey();
	Map<Integer, Double> synSetScoreMap = entry.getValue();

	double score = 0.0;
	double sum = 0.0;
	for (Map.Entry<Integer, Double> setScore : synSetScoreMap
	       .entrySet()) {
	  score += setScore.getValue() / (double) setScore.getKey();
	  sum += 1.0 / (double) setScore.getKey();
	}
	score /= sum;

	dictionary.put(word, score);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (csv != null) {
	csv.close();
      }
    }
  }

  public double extract(String word, String pos) {
    double vals;
      try{vals= dictionary.get(word + "#" + pos);}
    catch(NullPointerException e){
       vals=0.0;}
      return vals;
  }
  
  public static void main(String [] args) throws IOException {
    Double senti_score,sum = 0.0;
    int tkc=0;
    String pathToSWN,sentiment="";
      pathToSWN = "C:\\Users\\rahuliyer\\Desktop\\SentiWordNet_3.0.0_20130122.txt";
    SentiWordNetCode sentiwordnet = new SentiWordNetCode(pathToSWN);
    Properties props = new Properties();
       props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
       StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
       System.out.println("Enter the text:");
       Scanner sc= new Scanner (System.in);
       String text=sc.nextLine();
       
       Annotation document=new Annotation(text); 
       pipeline.annotate(document); 
       
       List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
       
       for(CoreMap sentence: sentences) {
          
          
         // System.out.println("Size"+token_length);
  for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
   
    String word = token.get(CoreAnnotations.TextAnnotation.class);
    
    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
    String wordnet_info = null;
//    String ner = token.get(NamedEntityTagAnnotation.class);
    if(pos.equals("JJ")||pos.equals("JJR")||pos.equals("JJS"))
        wordnet_info="a";
    else if(pos.equals("RBR")||pos.equals("RB")||pos.equals("RBS"))
        wordnet_info="r";
    else if(pos.equals("VB")||pos.equals("VBD")||pos.equals("VBG")||pos.equals("VBN")||pos.equals("VBP")||pos.equals("VBZ"))
        wordnet_info="v";
    else if(pos.equals("NN")||pos.equals("NNS")||pos.equals("NNP")||pos.equals("NNPS"))
         wordnet_info="n";
    else
        wordnet_info=null;
    // System.out.println(wordnet_info);
    if(wordnet_info!=null){
         senti_score= sentiwordnet.extract(word, wordnet_info);
         sum=senti_score+sum;
         tkc++;
         System.out.println(pos+" "+sum);
    }
      
    
    
   
        }
    }
       sum=sum/tkc;
       System.out.println(tkc);
    if(sum>=0.75)
            sentiment="very positive";
        else if(sum > 0.25 && sum<0.5)
            sentiment="positive";
        else if(sum>=0.5)
            sentiment="positive";
        else if(sum < 0 && sum>=-0.25)
            sentiment="negative";
        else if(sum < -0.25 && sum>=-0.5)
            sentiment="negative";
        else if(sum<=-0.75)
            sentiment="very negative";
    else    
    sentiment="neutral";
    System.out.println("Sentiment associated with this text is "+sentiment); 
    }  
  }

