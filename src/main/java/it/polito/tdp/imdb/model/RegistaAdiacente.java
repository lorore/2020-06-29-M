package it.polito.tdp.imdb.model;

public class RegistaAdiacente implements Comparable<RegistaAdiacente> {

	private Director d;
	private Double peso;
	public RegistaAdiacente(Director d, Double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "RegistaAdiacente [d=" + d + ", peso=" + peso + "]";
	}
	@Override
	public int compareTo(RegistaAdiacente o) {
		return -Double.compare(this.peso, o.peso);
	}
	
	
}
