package org.idocs.projects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class App {

	//method to parse a Movie object from a line
	public static Movie parseMovie(String line) {

		String[] tokens = Pattern.compile(" ").split(line);
		Movie movie = new Movie(tokens[1],
				tokens[3],
				Integer.parseInt(tokens[2].replace("min", "")),
				Integer.parseInt(tokens[3].replace("min", "")));
		return movie;
	}



	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
		
		
	public static void main(String args[])
	{
		
		try{
			Stream<String> streamPerson1 = Files.lines(Paths.get("/home/michalis/eclipse-workspace/Nasos"));
			Stream<String> streamPerson2 = Files.lines(Paths.get("/home/michalis/eclipse-workspace/Michalis"));
			Stream<String> streamPerson3 = Files.lines(Paths.get("/home/michalis/eclipse-workspace/Tasos"));
			String regex = "^(\\d+)/(\\d+)/(\\d+) (.*) (\\d+)min (\\d+)min (\\S+)$";
			Pattern pattern = Pattern.compile(regex);

			Person person1 = new Person(Paths.get("/home/michalis/eclipse-workspace/Nasos").getFileName().toString(),
					                    Files.lines(Paths.get("/home/michalis/eclipse-workspace/Nasos")).filter(pattern.asPredicate()).
					                    		map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));
			
			Person person2 = new Person(Paths.get("/home/michalis/eclipse-workspace/Michalis").getFileName().toString(),
                    Files.lines(Paths.get("/home/michalis/eclipse-workspace/Michalis")).filter(pattern.asPredicate()).
                    		map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));
			
			Person person3 = new Person(Paths.get("/home/michalis/eclipse-workspace/Tasos").getFileName().toString(),
                    Files.lines(Paths.get("/home/michalis/eclipse-workspace/Tasos")).filter(pattern.asPredicate()).
                    		map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));
			
			List<Person> persons = new ArrayList<Person>();
			persons.add(person1);
			persons.add(person2);
			persons.add(person3);
			
			
			Stream<Movie> allLines = Stream.concat(person1.getMovies().stream(), Stream.concat(person2.getMovies().stream(), person3.getMovies().stream()));
			
			// 1st Exercise
			List<Movie> movies = allLines.collect(Collectors.toList());
			movies.stream().sorted(Comparator.comparing(Movie::getTimeSpentbyPerson))
						   .forEach(m -> System.out.println("Title:" +m.title+" time : "+m.timeSpentbyPerson+"min"));
			
			// 2st Exercise
			persons.stream().forEach(p-> p.printPercentages());
						
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}



	}
}

