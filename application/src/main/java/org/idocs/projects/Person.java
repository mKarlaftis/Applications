package org.idocs.projects;

import java.util.List;

public class Person {

	String name;
	List<Movie> movies;
	
	public Person(String name, List<Movie> movies) {
		this.name=name;
		this.movies = movies;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	

	//method to parse a Movie object from a line
	public void printPercentages() {

		System.out.println("\nPerson: "+this.getName());
		
		this.getMovies().stream().forEach(m-> {
		int percentage = (m.getTimeSpentbyPerson()*100)/m.getLength();
		System.out.println(m.getTitle()+": "+percentage+"% seen");
		});
		System.out.println("Average percentage:"+
					(int)this.getMovies().stream().mapToInt(m-> (m.getTimeSpentbyPerson()*100)/m.getLength()).summaryStatistics().getAverage()+"%");
	}
}
