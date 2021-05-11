package it.polito.tdp.borders.model;

public class Border {

	private Country stato1;
	private Country stato2;
	
	
	
	public Border(Country stato1, Country stato2) {
		super();
		this.stato1 = stato1;
		this.stato2 = stato2;
		
	}


	@Override
	public String toString() {
		return stato1.getCcode() + ", " + stato2.getCcode() +"\n";
	}

	public Country getStato1() {
		return stato1;
	}


	public void setStato1(Country stato1) {
		this.stato1 = stato1;
	}


	public Country getStato2() {
		return stato2;
	}


	public void setStato2(Country stato2) {
		this.stato2 = stato2;
	}

	
	
	
}
