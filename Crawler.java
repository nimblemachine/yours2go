import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Crawler {
    
    Crawler() {
	  protocol = "http";
	  host = "www.virtualtourist.com";
	  file = "/travel/Europe/TravelGuide-Europe.html";
	  //file = "/travel/Europe/United_Kingdom/England/Greater_London/London-309228/TravelGuide-London.html";
    }
    
    String host;
    String protocol;
    String file;
    
    ArrayList<Link> cityLinks;
    
    void start() {
	  
	  try {
		URL url = new URL(protocol, host, file);
		
		URLConnection connection = url.openConnection();
		
		InputStream input = connection.getInputStream();
		
		Scanner scanner = new Scanner(input).useDelimiter("\n");
		
		
		//while(scanner.hasNextLine()) {
		    //    String regel = scanner.next();
		cityLinks = findCityLinks(findLinks(scanner));
		for(int i=0;i<cityLinks.size();i++){
		    //System.out.println(((Link)cityLinks.get(i)).getUrl());
		    processCity((Link)cityLinks.get(i));
		}
		    
		//}
		
		
		
		
	  } catch (Exception e) {
		System.out.println(e);
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
		
		if( ((Link)links.get(i)).getFileName() != null && ((Link)links.get(i)).getFileName().startsWith("Things_To_Do-")) {
		    
		    return (Link)links.get(i);
		}
	  }
	  return null; //not found
    }
    
    void processCity(Link l) {
	  try {
		
		URL cityUrl = new URL(l.getProtocol(), l.getHost(), l.getUrl());
		//System.out.println(l.getProtocol()+" "+l.getHost()+" "+l.getUrl());
		URLConnection cityConnection = cityUrl.openConnection();
		
		InputStream cityInput = cityConnection.getInputStream();
		
		Scanner cityScanner = new Scanner(cityInput).useDelimiter("\n");
		
		
		
		ArrayList<Link> links = findLinks(cityScanner);
		
		Link thingsToDoLink = findThingsToDoLink(links);
		
		System.out.println(thingsToDoLink.getProtocol()+"://"+thingsToDoLink.getHost()+thingsToDoLink.getUrl());
		
		
	  } catch (Exception e) {
		System.out.println("foutje: "+e);
	  }
    }
    
    
    public static void main(String[] args) {
	  new Crawler().start();
    }
}
