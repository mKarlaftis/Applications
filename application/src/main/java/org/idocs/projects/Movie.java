package org.idocs.projects;

import java.util.regex.Pattern;

public class Movie{


	String title;
	String genre;
	int length;
	int timeSpentbyPerson;

	public Movie(String title,String genre,int length, int timeSpent) {
		this.title =  title;
		this.genre = genre;
		this.timeSpentbyPerson = timeSpent;
		this.length = length;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getTimeSpentbyPerson() {
		return timeSpentbyPerson;
	}
	public void setTimeSpentbyPerson(int timeSpentbyPerson) {
		this.timeSpentbyPerson = timeSpentbyPerson;
	}


	//method to parse a Movie object from a line
	public static Movie parseMovie(String line) {

		String[] tokens = Pattern.compile(" ").split(line);
		String title = line.replace(tokens[0]+" ", "").replaceFirst("(\\d+)min.*","");
		String[] partsofToken = Pattern.compile(" ").split(line.replace(tokens[0]+" ", "").replace(title,""));

		Movie movie = new Movie(title,
				partsofToken[2],
				Integer.parseInt(partsofToken[0].replace("min", "")),
				Integer.parseInt(partsofToken[1].replace("min", "")));
		return movie;
	}
	
	
	public int getPercentage() {
		return (this.getTimeSpentbyPerson()*100)/this.getLength();
	}
	

}
