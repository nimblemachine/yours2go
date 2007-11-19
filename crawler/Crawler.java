import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import gate.creole.annic.apache.lucene.analysis.standard.StandardAnalyzer;
import gate.creole.annic.apache.lucene.analysis.Token;
import gate.creole.annic.apache.lucene.analysis.TokenStream;


class Crawler {
    
    String host;
    String protocol;
    String file;
    
    ArrayList<Link> cityLinks;
    CityRow cities;
    
    Crawler() {
	  cities = new CityRow();
	  protocol = "http";
	  host = "www.virtualtourist.com";
	  file = "/travel/Europe/TravelGuide-Europe.html";
	  //file = "/travel/Europe/United_Kingdom/England/Greater_London/London-309228/TravelGuide-London.html";
    }
    
    
    
    void start() {
	  
	  try {
		URL europeUrl = new URL(protocol, host, file);
		
		
		Scanner scanner = getScannerFromUrl(europeUrl);
		
		
		
		cityLinks = findCityLinks(findLinks(scanner));
		for(int i=15;i<cityLinks.size();i++){
		    //System.out.println("komt ie hier??");
		    processCity((Link)cityLinks.get(i));
		    //System.out.println("en hier dan?");
		    
		    
		    //System.out.println("City processed!\n");
		    //System.out.println(cities.length());
		    City city = cities.get(i);
		    //System.out.println("komt murk hier?");
		    //for(int j=0;j<city.length();j++){
			  //System.out.println("bla?!?");
			  if(city==null){
				System.out.println("city is null! hahaa");
			  }
			  Sight s = city.getSight(i);
			  
			  //System.out.println("number of occ of tower in 1st sight: ");
			  //System.out.println(s.nrOfOccurrences("tower"));
			  //System.out.println("number of occ of tower in all but 1st sight: ");
			  //System.out.println(city.countOccExcept("tower", s));
			  //System.out.println("ladielaaa");
			  city.setKeywordsForSights();
			  //System.out.println("your mother")S;
			  
			  printCity(city);
			  
			  
			  
			  
			  /*
			  TokenStream ts = s.getTokenStream();
			  
			  try{
				Token t = ts.next();
				while(t != null) {
				    System.out.println(t.termText());
				    t = ts.next();
				}
			  } catch (Exception e) {
				System.out.println("error error: "+e);
			  }
			  */
		    //}
		    
		    
		    
		}
		
		
		
		
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  } 
	  
    }
    
    void printCity(City city) {
	  System.out.println("Begin City");
	  System.out.println(city.getName());
	  printSights(city);
	  System.out.println("End City");
	  System.out.println("-----------------");
    }
    
    void printSights(City city) {
	  System.out.println("Begin all sights");
	  for(int i=0;i<city.length();i++){
		Sight sight = city.getSight(i);
		printSight(sight);
		
	  }
	  System.out.println("End all Sights");
	  
    }
    
    void printSight(Sight sight) {
	  System.out.println("Begin Sight");
	  System.out.println(sight.realName);
	  System.out.println(sight.name);
	  printKeywords(sight);
	  System.out.println("End Sight");
    }
    
    void printKeywords(Sight sight) {
	  System.out.println("Begin Keywords:");
	  for(int i=0; i<sight.nrOfKeywords; i++){
		System.out.println(sight.keywords[i]);
	  }
	  System.out.println("End Keywords");
    }
    
    /*
    find all links on a certain scanner. the scanner should contain a html file. 
    */
    ArrayList<Link> findLinks(Scanner scanner) {
	  ArrayList<Link> result = new ArrayList<Link>();
	  
	  
	  while(scanner.hasNextLine()) {
		String s = scanner.next();
		int i=0;
		
		while(s.indexOf("<a href=", i) != -1) {
		    
		    int beginOpen  = s.indexOf("<a ", i);
		    
		    int beginSluit = s.indexOf("</a>", i);
		    if(beginSluit < beginOpen) { //something wrong in html code, it happens...
			  beginSluit = s.indexOf("</a>", i+5);
			  
			  i+=5;
		    }
		    if(beginSluit != -1){
			  
			  
			  //System.out.println(protocol+" "+host+" "+s.substring(beginOpen, beginSluit+4));
			  Link l = new Link(protocol, host, s.substring(beginOpen, beginSluit+4));
			  result.add(l);
			  
			  
			  
			  i = beginSluit;
		    }
		    
		}
		
	  }
	  return result;
    }
    
    
    
    /*
    finds links to cities of europe from a list containing all links on a page
    */
    ArrayList<Link> findCityLinks(ArrayList<Link> links) {
	  ArrayList<Link> result = new ArrayList<Link>();
	  
	  for(int i=0; i<links.size();i++){
		Link l = (Link)links.get(i);
		if (l.numberOfParts() >= 7 && l.getPart(2).equals("travel")) { //city link found!!
		    result.add(l);
		}
	  }
	  return result;
    }
    
    
    /*
    finds links to a Things To Do page in all Links in the list
    */
    Link findThingsToDoLink(ArrayList<Link> links) {
	  for(int i=0; i<links.size();i++){
		//System.out.println(((Link)links.get(i)).getFileName());
		
		if( ((Link)links.get(i)).getFileName().startsWith("Things_To_Do-")) {
		    
		    return (Link)links.get(i);
		}
	  }
	  return null; //not found
    }
    
    
    /*
    processes a city with link l
    */
    void processCity(Link l) {
	  try {
		
		URL cityUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		//System.out.println(l.getProtocol()+" "+l.getHost()+" "+l.getUrl());
		Scanner cityScanner = getScannerFromUrl(cityUrl);
		
		
		
		ArrayList<Link> links = findLinks(cityScanner);
		
		Link thingsToDoLink = findThingsToDoLink(links);
		
		//System.out.println(thingsToDoLink.getProtocol()+"://"+thingsToDoLink.getHost()+thingsToDoLink.getUrl());
		
		processSights(thingsToDoLink);
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  }
    }
    
    
    /*
    Makes a scanner containing the html from a certain URL object
    */
    Scanner getScannerFromUrl(URL url) {
	  try{
		URLConnection connection = url.openConnection();
		InputStream input = connection.getInputStream();
		return new Scanner(input).useDelimiter("\n");
	  } catch (IOException e) {
		System.out.println("error while opening connection: "+e);
		System.exit(1);
	  }
	  return null; //will never happen
	  
    }
    
    
    /*
    gets all links to sights on the page with link l
    */
    void processSights(Link l) {
	  City city = new City(l.getCityName());
	  
	  
	  
	  try{
		URL toDoUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		//System.out.println(l.getProtocol()+" "+l.getHost()+" "+l.getUrl());
		
		Scanner toDoScanner = getScannerFromUrl(toDoUrl);
		
		
		ArrayList<Link> links = findLinks(toDoScanner);
		
		ArrayList<Link> sightLinks = findSightLinks(links);
		
		
		for(int i=0; i<sightLinks.size(); i++){
		    //System.out.println(((Link)sightLinks.get(i)).getText());
		    Sight sight = readSight(((Link)sightLinks.get(i)));
		    city.addSight(sight);
		    //System.out.println(sight.nrOfOccurrences("tower"));
		}
		
		cities.insert(city);
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  }
	  
    }
    
    
    /*
    Filters links for sights from an arraylist with all links for a page
    */    
    ArrayList<Link> findSightLinks(ArrayList<Link> links) {
	  ArrayList<Link> result = new ArrayList<Link>();
	  for(int i=0; i<links.size();i++){
		//System.out.println(((Link)links.get(i)).getFileName());
		
		if( ((Link)links.get(i)).getFileName().startsWith("Things_To_Do-") && ((Link)links.get(i)).getFileName().endsWith("-BR-1.html") && !((Link)links.get(i)).getFileName().endsWith("MISC-BR-1.html") && !((Link)links.get(i)).getText().equals("All Tips")){
		    if(!linkInList((Link)links.get(i), result)){
			  result.add((Link)links.get(i));
		    }
		}
	  }
	  return result; 
    }
    
    boolean linkInList(Link l, ArrayList<Link> list) {
	  for(int i=0;i<list.size();i++){
		if( ((Link)list.get(i)).getText().equals(l.getText())){
		    return true;
		}
	  }
	  return false;
    }
    
    
    Sight readSight(Link l) {
	  try {
		URL sightUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		Scanner sightScanner = getScannerFromUrl(sightUrl);
		
		String filename = l.getFileName();
		String city = getCityFromFileName(filename);
		
		String sightName = getSightNameFromFileName(filename);
		
		String realSightName = l.getText();
		
		String[] descriptions = findDescriptions(sightScanner);
		
		
		//System.out.println(sightName);
		
		return new Sight(city, sightName, realSightName, sightUrl, descriptions);
		
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  }
	  
	  return null; //should never happen...
	  
	  
    }
    
    
    String getCityFromFileName(String filename) {
	  int i = filename.indexOf("-")+1;
	  int j = filename.indexOf("-", i+1);
	  return filename.substring(i,j);
    }
    
    String getSightNameFromFileName(String filename) {
	  int i = filename.indexOf("-");
	  int j = filename.indexOf("-", i+1)+1;
	  int k = filename.indexOf("-", j+1);
	  return filename.substring(j,k);
    }
    
    String[] findDescriptions(Scanner scanner) {
	  String[] result = new String[10];
	  int nrOfDescriptions=0;
	  
	  while(scanner.hasNextLine()) {
		String s = scanner.next();
		
		int i=0;
		while(s.indexOf("<span class=\"content\">",i) != -1) {
		    
		    int beginOpen  = s.indexOf("<span class=\"content\">",i);
		    
		    int beginSluit = s.indexOf("</span>", beginOpen);
		    
		    String description = s.substring(beginOpen+22, beginSluit);
		    
		    result[nrOfDescriptions] = description;
		    nrOfDescriptions++;
		    i = beginSluit;
		}
		
	  }
	  return result;
    }
    
    
    
    
    public static void main(String[] args) {
	  new Crawler().start();
    }
}
