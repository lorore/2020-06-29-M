package it.polito.tdp.imdb.model;

import java.time.Year;

public class TestModel {

	public static void main(String[] args) {
		Model m=new Model();
		m.creaGrafo(Year.of(2005));
	}

}
