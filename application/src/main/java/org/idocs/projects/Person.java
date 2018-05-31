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
	public  String getPercentages() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nPerson: "+this.getName());
		
		this.getMovies().stream().forEach(m-> {
		int percentage = (m.getTimeSpentbyPerson()*100)/m.getLength();
		builder.append("\n"+m.getTitle()+": "+percentage+"% seen");
		});
		builder.append("\nAverage percentage:"+
					(int)this.getMovies().stream().mapToInt(m-> (m.getTimeSpentbyPerson()*100)/m.getLength()).summaryStatistics().getAverage()+"%\n");
		return builder.toString();
	}
}
