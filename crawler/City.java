import gate.creole.annic.apache.lucene.analysis.Token;
import gate.creole.annic.apache.lucene.analysis.TokenStream;

class City {

    
    	private String name;
	private Sight[] sights;
	private int nrOfElements;
	
	
	City(String name) {
	      this.name = name;
		sights = new Sight[100];
		nrOfElements = 0;
	}
	
	
	public void addSight(Sight sight) {
		sights[nrOfElements] = sight;
		nrOfElements++;
	}
	
	public int length() {
		return nrOfElements;
	}
	
	public Sight getSight(int i) {
	    if(i >= 0 && i < nrOfElements) {
			return sights[i];
		}
		
		return null; //element not found
	}
	
	public String getName() {
	    return name;
	}
	
	public void setKeywordsForSights() {
	    for(int i=0;i<nrOfElements;i++){
		  Sight s = sights[i];
		  
		  //System.out.println("Starting to process sight: "+s.realName);
		  
		  TokenStream ts = s.getTokenStream();
		  
		  try{
			Token t = ts.next();
			while(t != null ) {
			    if(!s.containsUsedWord(t.termText())){
				  //System.out.println("new term: "+t.termText());
					int occInThis = s.nrOfOccurrences(t.termText());
					int occInOthers = countOccExcept(t.termText(), s);
					//System.out.println(occInThis+" "+occInOthers);
					if(occInThis > 5 && occInOthers < (4*occInThis)) {
					    s.addKeyword(t.termText());
					    
					    //System.out.println("---------------- Added "+t.termText()+"!! ----------------");
					}
					s.addUsedWord(t.termText());
			    } else {
				  //System.out.println(t.termText()+" was already there...");
			    }
			    
			    t = ts.next();
			}
		  } catch (Exception e) {
			System.out.println("should never happen, setKeywordsForSights"+e);
		  }
		  
		  s.setFinalKeywords();
	    }
	    
	}
	
	public int countOccExcept(String s, Sight sight) {
	    int result = 0;
	    for(int i=0;i<nrOfElements;i++){
		  if(!sights[i].name.equals(sight.name)) {
			result += sights[i].nrOfOccurrences(s);
		  }
	    }
	    return result;
	}
	
	public void addFinalKeywordsToSights() {
	    for(int i=0;i<nrOfElements;i++){
		  sights[i].setFinalKeywords();
	    }
	
	    
	}
	

}
