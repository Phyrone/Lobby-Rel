package de.phyrone.lobbyrel.lib;

public class Values<F extends Object, S extends Object> {
	
	private F first;
	
	private S second;
	
	public Values() {
	
	}
	
	
	public Values<F, S> setFirst(F first) {
		this.first = first;
		return this;
	}
	public Values<F, S> setSecond(S second) {
		this.second = second;
		return this;
	}
	public F getFirst() {
		return first;
	}
	public S getSecond() {
		return second;
	}
	public String toString() {
		return "Values(F=" + getFirst() + ",S=" + getSecond() + ")";
	}
	
	
}
