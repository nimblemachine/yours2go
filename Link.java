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
    
    Link(String protocol, String host, String s) {
	  completeLink = s;
	  linkParts = new String[100];
	  linkParts[0] = protocol+"://"+host;
	  nrOfParts = 1;
	  divideIntoParts();
	  
    }
    
    /* this will devide the complete link in an array.
       the first position will be the hostname (with protocol), the following
	 positions will contain the directory names, and the last
	 position will contain the file name (e.g. the html file)
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
    
    
    String getText() {
	  int i = completeLink.indexOf(">")+1;
	  int j = completeLink.indexOf("</a>");
	  return completeLink.substring(i,j);
    }
    
    String getUrl() {
	  int i = completeLink.indexOf("=")+2;
	  int j = completeLink.indexOf("\"", i);
	  if( j == -1 ) {
		j = completeLink.indexOf(">", i)-1;
	  }
	  return completeLink.substring(i,j);
    }
    
    String getFileName() {
	  return linkParts[nrOfParts];
    }
    
    String getDirectory(int index) {
	  if(index >= nrOfParts || index < 0) {
		throw new Error("wrong index");
	  }
	  return linkParts[index];
    }
    
    int numberOfParts() {
	  return nrOfParts;
    }
    
    
    
}
