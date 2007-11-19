import java.net.URL;

import gate.creole.annic.apache.lucene.analysis.standard.StandardAnalyzer;
import gate.creole.annic.apache.lucene.analysis.Token;
import gate.creole.annic.apache.lucene.analysis.TokenStream;
import gate.creole.annic.apache.lucene.analysis.standard.StandardTokenizer;
import gate.creole.annic.apache.lucene.analysis.LowerCaseFilter;

import java.io.*;

class Sight {
    
    String city;
    String name;
    String realName;
    URL url;
    
    String[] descriptions;
    
    String[] keywords;
    int nrOfKeywords;
    
    
    String[] usedWords;
    int nrOfUsedWords;
    
    Sight(){
    }
    
    Sight(String city, String name, String realName, URL url, String[] descriptions){
	  this.city = city;
	  this.name = name;
	  this.realName = realName;
	  this.url = url;
	  this.descriptions = descriptions;
	  keywords = new String[100];
	  nrOfKeywords = 0;
	  usedWords = new String[10000];
	  nrOfUsedWords = 0;
    }
    
    
    String getAllReviews() {
	  StringBuffer result = new StringBuffer();
	  for(int i=0;i<descriptions.length;i++){
		result.append(descriptions[i]);
	  }
	  return result.toString();
    }
    
    TokenStream getTokenStream() {
	  StringReader sr = new StringReader(realName + getAllReviews());
	  
	  StandardAnalyzer sa = new StandardAnalyzer();
	  
	  
	  return sa.tokenStream("standard", sr);
    }
    
    
    int nrOfOccurrences(String word) {
	  int result = 0;
	  TokenStream ts = getTokenStream();
	  try {
		Token t = ts.next();
		while(t != null) {
		    if(t.termText().equals(word)) {
			  result++;
		    }
		    t = ts.next();
		}
	  } catch (Exception e) {
		System.out.println("error that should not occur in nrOfOccurrences"+e);
	  }
	  return result;
    }
    
    void addKeyword(String s) {
	  keywords[nrOfKeywords] = s;
	  nrOfKeywords++;
    }
    
    boolean containsKeyword(String s) {
	  for(int i=0;i<nrOfKeywords;i++){
		if(s.equals(keywords[i])) {
		    return true;
		} 
	  }
	  return false;
    }
    
    void addUsedWord(String s){
	  usedWords[nrOfUsedWords] = s;
	  nrOfUsedWords++;
    }
    
    boolean containsUsedWord(String s) {
	  for(int i=0;i<nrOfUsedWords;i++){
		if(s.equals(usedWords[i])) {
		    return true;
		}
	  }
	  return false;
    }
    
    void setFinalKeywords() {
	  StringReader sr = new StringReader(realName);
	  TokenStream ts = new StandardTokenizer(sr);
	  
	  LowerCaseFilter lcf = new LowerCaseFilter(ts);
	  
	  //System.out.println("adding final keywords...");
	  try{
		Token t = lcf.next();
		while(t != null) {
		    if(!containsKeyword(t.termText())) {
			  addKeyword(t.termText());
			  //System.out.println("---------------- Added "+t.termText()+"!! ----------------");
		    } else {
			  //System.out.println(t.termText()+" was already there...");
		    }
		    t = lcf.next();
		}
	  } catch (Exception e) {
		System.out.println("this should never happen! setFinalKeywords"+e);
	  }
	  
    }
    
}
