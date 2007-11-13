import java.net.*;
import java.io.*;
import java.util.Scanner;

class Crawler {

    Crawler() {
    }

    void start() {
	String protocol = "http";
	String host = "www.virtualtourist.com";
	String file = "/travel/Europe/TravelGuide-Europe.html";


	System.out.println("helloooo");
	try {
	    URL url = new URL(protocol, host, file);
	    
	    URLConnection connection = url.openConnection();
	    
	    InputStream input = connection.getInputStream();

	    Scanner scanner = new Scanner(input).useDelimiter("\n");
	    //while(scanner.hasNext("^.*") || scanner.hasNext(".*") || scanner.hasNext("\n")) {
		
	    while(scanner.hasNextLine()) {
		String regel = scanner.next();
		Scanner regelScanner = new Scanner(regel);
		//System.out.println("begin"+regel+"end");
		//		if(regel.endsWith("</a>")) {
		//		    System.out.println(regel+"endlink");
		//		}
		printLinks(regel);

	    }
	    //System.out.println(scanner.nextLine());
	    //System.out.println(scanner.nextLine());
       


	} catch (MalformedURLException e) {
	    System.out.println(e);
	} catch (IOException e) {
	    System.out.println(e);
	}
	
    }

    void printLinks(String s) {

	/*
	for(int i=0;i<s.length();i++){
	    
	    
	    if(s.substring(i).length() > 10 && s.substring(i, i+8).equals("<a href=")) {
		System.out.println("yess!");
		System.out.println(s.substring(i)+"q");
		
		for(int j=i;j<s.substring(i).length();j++){
		    if(s.substring(j).length() > 3 && s.substring(i,j+4).endsWith("</a>")) {
			System.out.println("einde link:");
			System.out.println(s.substring(i,j+4));
		    } 
		    if(s.endsWith("</a>")) {
			System.out.println("ee");
		    }
		}

	    }
       	}
	*/
	int i=0;
	//System.out.println("\n\n"+s+"\n\nq");
	while(s.indexOf("<a href=", i) != -1) {
	    int beginOpen  = s.indexOf("<a ", i);

	    int beginSluit = s.indexOf("</a>", i);
	    if(beginSluit < beginOpen) { //something wrong in html code, it happens...
		beginSluit = s.indexOf("</a>", i+5);
	    }
	    i = beginSluit;
	    //System.out.print(""+beginOpen+" "+beginSluit);
	    System.out.println(s.substring(beginOpen, beginSluit+4));
	}

   

    }

    public static void main(String[] args) {
	new Crawler().start();
    }
}
