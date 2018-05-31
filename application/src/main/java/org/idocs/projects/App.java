package org.idocs.projects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

	public static void writeToFile(String str,BufferedWriter writer) {
		try
		{
			writer.write(str);

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Stream<Movie> concatenateStream(List<Person> persons)
	{
		Stream<Movie> allLines = Stream.empty();
		for(Person person:persons) 
		{

			allLines = Stream.concat(person.getMovies().stream(),allLines);

		}
		
		return allLines;
	}
	
	
	public static String  reccomendedMovies(List<Person> persons ) {

        Supplier<Stream<Movie>> streamSupplier = ()-> concatenateStream(persons) ;
		List<Movie> movies = streamSupplier.get().collect(Collectors.toList());

        
		int averageMinSeen = (int) Math.round(movies.stream().mapToInt(m -> m.getTimeSpentbyPerson()).summaryStatistics().getAverage());
		Map<Integer,List<Movie>> map= streamSupplier.get().collect(Collectors.groupingBy(m-> Math.abs(m.length-averageMinSeen)));
		Integer key = map.keySet().stream().mapToInt(x ->x).summaryStatistics().getMin();
		int total= key+averageMinSeen;
		
		Map<String,List<Movie>> map2= streamSupplier.get().collect(Collectors.groupingBy(m-> m.genre));
		Stream<Entry<String, List<Movie>>> sortedList = map2.entrySet().stream().sorted(new Comparator<Entry<String,List<Movie>>>() {
			public int compare(Entry<String,List<Movie>> o1,Entry<String,List<Movie>> o2) 
			{
				if(o1.getValue().size() < o2.getValue().size()) {
					return 1;
				}
				else if(o1.getValue().size() == o2.getValue().size()) {
					return -0;
				}else
				{
					return -1;
				}
			}
		});

		List<Entry<String, List<Movie>>> listOfGenres = sortedList.collect(Collectors.toList());
		return "\nReccomended movie duration: "+total+
				"\nRecomended genres: "+ listOfGenres.get(0).getKey()+","+listOfGenres.get(1).getKey();
	}

	public static void main(String args[])
	{


		if(args.length!=4)
		{
			System.out.println("\n[ERROR]: WRONG INVOCATION, PLEASE PROVIDE RIGHT NUMBER OF PARAMETERS");
			System.exit(0);
		}

		try
		{

			//create output file if not exists
			File outputFile = new File(args[3]);
			outputFile.createNewFile();
			Path outputPath = Paths.get(outputFile.getPath());


			String regex = "^(\\d+)/(\\d+)/(\\d+) (.*) (\\d+)min (\\d+)min (\\S+)$";
			Pattern pattern = Pattern.compile(regex);

			Person person1 = new Person(Paths.get(args[0]).getFileName().toString(),
					Files.lines(Paths.get(args[0])).filter(pattern.asPredicate()).
					map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));

			Person person2 = new Person(Paths.get(args[1]).getFileName().toString(),
					Files.lines(Paths.get(args[1])).filter(pattern.asPredicate()).
					map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));

			Person person3 = new Person(Paths.get(args[2]).getFileName().toString(),
					Files.lines(Paths.get(args[2])).filter(pattern.asPredicate()).
					map(l -> Movie.parseMovie(l)).collect((Collectors.toList())));

			List<Person> persons = new ArrayList<Person>();
			persons.add(person1);
			persons.add(person2);
			persons.add(person3);


			Stream<Movie> allLines = Stream.concat(person1.getMovies().stream(), Stream.concat(person2.getMovies().stream(), person3.getMovies().stream()));

			try (BufferedWriter writer = Files.newBufferedWriter(outputPath)){

				// 1st Exercise
				writeToFile("\nEXERCISE 1st\n", writer);
				List<Movie> movies = allLines.collect(Collectors.toList());
				movies.stream().sorted(Comparator.comparing(Movie::getTimeSpentbyPerson))
				.forEach(m -> writeToFile("\nTitle:" +m.title+" time : "+m.timeSpentbyPerson+"min",writer));

				// 2st Exercise
				writeToFile("\n\nEXERCISE 2st\n", writer);
				persons.stream().forEach(p-> writeToFile(p.getPercentages(),writer));
				
				// 3st Exercise
				writeToFile("\n\nEXERCISE 3st\n", writer);
				persons.stream().forEach(p -> writeToFile("\n"+p.name+"'s Favorite genre: "+p.movies.stream()
						.filter(m -> (m.getPercentage() > 60))
						.collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()))
				        // fetch the max entry
				        .entrySet().stream().max(Map.Entry.comparingByValue())
				        // map to tag
				        .map(Map.Entry::getKey).orElse("None"),writer));
				
				// 4st Exercise
				writeToFile("\n\nEXERCISE 4st\n", writer);
				writeToFile(reccomendedMovies(persons), writer);
			}


			}catch (IOException e) {

				System.out.println("\n[ERROR]: PLEASE CHECK THAT ALL INPUT FILES EXIST !");
				System.exit(0);
			}



		}
	
	}

