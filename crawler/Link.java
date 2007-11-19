class Link {
    /* This class describes a html hyperlink.
    */
    
    private String completeLink;
    
    private String[] linkParts;
    private int nrOfParts;
    
    Link() { //never to be used
	  completeLink = null;
	  linkParts = new String[100];
	  nrOfParts = 0;
    }
    
    
    /*
    Makes a new Link object, with the protocol on index 0 and the host on
    index 1 of the parts array.  
    However, when String s is an absolute link, the whole thing is placed on
    the first index position of the linkParts array. The devideIntoParts method
    will do this.
    */
    Link(String protocol, String host, String s) {
	  completeLink = s;
	  linkParts = new String[100];
	  linkParts[0] = protocol;
	  linkParts[1] = host;
	  nrOfParts = 2;
	  divideIntoParts();
	  
    }
    
    /* this will devide the complete link in an array.
       the first position will be the hostname (with protocol), the following
	 positions will contain the directory names, and the last
	 position will contain the file name (e.g. the html file)
	 
	 When the link is an absolute link, is is not devided into parts, but the
	 whole url is placed in index 0 of the linkParts array.
    */
    void divideIntoParts(){
	  String url = getUrl();
	  if(url.charAt(0) == '/') { //relative link
		int i = 1;
		//split up all directories
		while(url.indexOf("/", i) != -1) {
		    int j = url.indexOf("/", i);
		    linkParts[nrOfParts] = url.substring(i,j);
		    nrOfParts++;
		    i = j+1;
		}
		//finally, get the file name (moestly .html)
		linkParts[nrOfParts] = url.substring(i, url.length());
		nrOfParts++;
		
		/*
		for(int k=0;k<nrOfParts;k++) {
		    System.out.print(nrOfParts+" ");
		    System.out.println(linkParts[k]);
		}
		*/
	  } else { //absolute link of javascript link or wrongly formed link. Doesn't matter for our purpose
		//System.out.print("abslink: ");
		linkParts[0] = url;
		nrOfParts = 1;
	  }
    }
    
    
    /*
    returns the text of a link (as shown on a html page)
    */
    String getText() {
	  int i = completeLink.indexOf(">")+1;
	  int j = completeLink.indexOf("</a>");
	  return completeLink.substring(i,j);
    }
    
    
    /*
    returns the url of the link (<a href="this part">)
    */
    String getUrl() {
	  int i = completeLink.indexOf("=")+2;
	  int j = completeLink.indexOf("\"", i);
	  if( j == -1 ) {
		j = completeLink.indexOf(">", i)-1;
	  }
	  return completeLink.substring(i,j);
    }
    
    
    /*
    returns the filename of the link. Mostly a .html file
    */
    String getFileName() {
	  return linkParts[nrOfParts-1];
    }
    
    /*
    returns the name of the part on the specified index
    */
    String getPart(int index) {
	  if(index >= nrOfParts || index < 0) {
		throw new Error("wrong index");
	  }
	  return linkParts[index];
    }
    
    /*
    returns the number of parts the link consists of
    */
    int numberOfParts() {
	  return nrOfParts;
    }
    
    /*
    returns the protocol of the link
    */
    String getProtocol() {
	  return linkParts[0];
    }
    
    /*
    returns the host of the link
    */
    String getHost() {
	  return linkParts[1];
    }
    
    String getCityName() {
	  int i = completeLink.indexOf("Do-");
	  if (i != -1 ) {
		i += 3;
		int j = completeLink.indexOf("-", i+1);
		return completeLink.substring(i,j);
	  } else {
		return "name of city not found...";
	  }
    }
    
}
