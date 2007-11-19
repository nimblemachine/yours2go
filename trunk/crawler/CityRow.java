class CityRow {

	private City[] cities;
	private int nrOfElements;
	
	CityRow() {
		cities = new City[20];
		nrOfElements = 15;
	}
	
	public int length(){
		return nrOfElements;
	}
	
	public void insert(City city) {
		cities[nrOfElements] = city;
		nrOfElements++;
	}
	
	public City get(int i) {
		if(i >= 0 && i < nrOfElements) {
			return cities[i];
		}
		return null; //not found...
	}



}
