import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Crawler {
    
    String host;
    String protocol;
    String file;
    
    ArrayList<Link> cityLinks;
    
    Crawler() {
	  protocol = "http";
	  host = "www.virtualtourist.com";
	  file = "/travel/Europe/TravelGuide-Europe.html";
	  //file = "/travel/Europe/United_Kingdom/England/Greater_London/London-309228/TravelGuide-London.html";
    }
    
    
    
    void start() {
	  
	  try {
		URL europeUrl = new URL(protocol, host, file);
		
		
		Scanner scanner = getScannerFromUrl(europeUrl);
		
		
		//while(scanner.hasNextLine()) {
		    //    String regel = scanner.next();
		cityLinks = findCityLinks(findLinks(scanner));
		for(int i=0;i<cityLinks.size();i++){
		    //System.out.println(((Link)cityLinks.get(i)).getUrl());
		    processCity((Link)cityLinks.get(i));
		}
		    
		//}
		
		
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  } 
	  
    }
    
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
    
    
    
    
    ArrayList<Link> findCityLinks(ArrayList<Link> links) {
	  ArrayList<Link> result = new ArrayList<Link>();
	  
	  for(int i=0; i<links.size();i++){
		Link l = (Link)links.get(i);
		if (l.numberOfParts() >= 7 && l.getDirectory(2).equals("travel")) { //city link found!!
		    result.add(l);
		}
	  }
	  return result;
    }
    
    Link findThingsToDoLink(ArrayList<Link> links) {
	  for(int i=0; i<links.size();i++){
		//System.out.println(((Link)links.get(i)).getFileName());
		
		if( ((Link)links.get(i)).getFileName().startsWith("Things_To_Do-")) {
		    
		    return (Link)links.get(i);
		}
	  }
	  return null; //not found
    }
    
    void processCity(Link l) {
	  try {
		
		URL cityUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		//System.out.println(l.getProtocol()+" "+l.getHost()+" "+l.getUrl());
		Scanner cityScanner = getScannerFromUrl(cityUrl);
		
		
		
		ArrayList<Link> links = findLinks(cityScanner);
		
		Link thingsToDoLink = findThingsToDoLink(links);
		
		//System.out.println(thingsToDoLink.getProtocol()+"://"+thingsToDoLink.getHost()+thingsToDoLink.getUrl());
		
		processThingsToDo(thingsToDoLink);
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  }
    }
    
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
    
    void processThingsToDo(Link l) {
	  
	  try{
		URL toDoUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		//System.out.println(l.getProtocol()+" "+l.getHost()+" "+l.getUrl());

		Scanner toDoScanner = getScannerFromUrl(toDoUrl);
		
		
		ArrayList<Link> links = findLinks(toDoScanner);
		
		ArrayList<Link> sightLinks = findSightLinks(links);
		
		
		for(int i=0; i<sightLinks.size(); i++){
		    System.out.println(((Link)sightLinks.get(i)).getFileName());
		}
		
		
		
	  } catch (MalformedURLException e) {
		System.out.println("error while forming URL object: "+e);
	  }
	  
    }
    
    ArrayList<Link> findSightLinks(ArrayList<Link> links) {
	  ArrayList<Link> result = new ArrayList<Link>();
	  for(int i=0; i<links.size();i++){
		//System.out.println(((Link)links.get(i)).getFileName());
		
		if( ((Link)links.get(i)).getFileName().startsWith("Things_To_Do-") && ((Link)links.get(i)).getFileName().endsWith("-BR-1.html") && !((Link)links.get(i)).getFileName().endsWith("MISC-BR-1.html") && !((Link)links.get(i)).getText().equals("All Tips")){
		    
		    result.add((Link)links.get(i));
		}
	  }
	  return result; 
    }
    
    public static void main(String[] args) {
	  new Crawler().start();
    }
}
