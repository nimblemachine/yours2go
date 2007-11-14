import java.net.*;
import java.io.*;
import java.util.Scanner;

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
    
    void start() {
	  
	  try {
		URL url = new URL(protocol, host, file);
		
		URLConnection connection = url.openConnection();
		
		InputStream input = connection.getInputStream();
		
		Scanner scanner = new Scanner(input).useDelimiter("\n");
		
		
		while(scanner.hasNextLine()) {
		    String regel = scanner.next();
		    printLinks(regel);
		    
		}
		
		
		
		
	  } catch (MalformedURLException e) {
		System.out.println(e);
	  } catch (IOException e) {
		System.out.println(e);
	  }
	  
    }
    
    void printLinks(String s) {
	  
	  
	  int i=0;
	  
	  while(s.indexOf("<a href=", i) != -1) {
		
		int beginOpen  = s.indexOf("<a ", i);
		
		int beginSluit = s.indexOf("</a>", i);
		if(beginSluit < beginOpen) { //something wrong in html code, it happens...
		    beginSluit = s.indexOf("</a>", i+5);
		    
		    i+=5;
		}
		if(beginSluit != -1){
		    
		    

			  Link l = new Link(protocol, host, s.substring(beginOpen, beginSluit+4));
			  
			  if (l.numberOfParts() >= 7 && l.getDirectory(1).equals("travel")) {
				System.out.println(l.getUrl());
			  }

			  i = beginSluit;
		}
		
	  }
	  
	  
    }
    
    public static void main(String[] args) {
	  new Crawler().start();
    }
}
