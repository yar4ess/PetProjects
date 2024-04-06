package CommandManager;
import CollectionManager.CollectionManager;
import InputManager.*;
import Models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.stream.IntStream;

import static CollectionManager.IDManager.GetNewId;

public class RandomMovies extends Command{
    // TODO: Команда которая принимает int N на вход и генерит N разных вариантов объектов коллекции
    private final CollectionManager collectionManager;
    private final ArrayList<String> films = new ArrayList<>();
    private final ArrayList<String> taglines = new ArrayList<>();
    private final ArrayList<String> screenWriters = new ArrayList<>();
    private final ArrayList<String> location = new ArrayList<>();
    private final MovieGenre[] movieGenre = MovieGenre.values();
    private final String path;
    public RandomMovies(Console console, CollectionManager collectionManager, CommandManager commandManager, String path) {
        super("randomMovies N", "добавлеят n случайных объектов");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.path = path;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public CommandMessage execution(String args, Object obj) {
        {
            films.add("Star Wars");
            films.add("Green Elephant");
            films.add("Wolf From Wall Street");
            films.add("Shrek");
            films.add("Sopranos");
            films.add("Breaking Bad");
            films.add("Brat");
            films.add("Sherlock");

            taglines.add("kill");
            taglines.add("die");
            taglines.add("love");
            taglines.add("win");
            taglines.add("lose");
            taglines.add("hope");
            taglines.add("survive");
            taglines.add("solve");

            screenWriters.add("Balabanov");
            screenWriters.add("Bondarchuk");
            screenWriters.add("Kubrick");
            screenWriters.add("Nolan");
            screenWriters.add("Scorsese");
            screenWriters.add("Lynch");
            screenWriters.add("Spielberg");
            screenWriters.add("Tarantino");

            location.add("Britain");
            location.add("Italy");
            location.add("France");
            location.add("Usa");
            location.add("Russia");
            location.add("Germany");
            location.add("China");
            location.add("Korea");
        }
        if (args == null || args.isEmpty()) {
            return new CommandMessage(400, "Неправильное количество аргументов!)");
        }
        else{
            int N = Integer.parseInt(args.split(" ")[0]);
            Random random = new Random();
            IntStream.range(0, N).forEach(i -> {
                        Movie movie = new Movie();
                        movie.setId(GetNewId());
                        movie.setName(films.get(random.nextInt(films.size())));
                        Coordinates coordinates = new Coordinates();
                        coordinates.setX(random.nextInt(10000));
                        coordinates.setY(random.nextInt(627));
                        movie.setCoordinates(coordinates);
                        movie.setCreationDate(new Date());
                        movie.setOscarsCount(random.nextInt(1, 6));
                        movie.setBudget(random.nextInt(100000, 999999999));
                        movie.setTagline(taglines.get(random.nextInt(taglines.size())));
                        movie.setGenre(movieGenre[random.nextInt(movieGenre.length)]);

                        Person screenWriter = new Person();
                        screenWriter.setName(screenWriters.get(random.nextInt(screenWriters.size())));
                        screenWriter.setBirthday(LocalDate.of(random.nextInt(1940, 2000), random.nextInt(1, 12), random.nextInt(1, 28)));

                        Location location1 = new Location();
                        location1.setName(location.get(random.nextInt(location.size())));
                        location1.setX(random.nextInt(10000));
                        location1.setY((float)random.nextInt(10000));
                        location1.setZ(random.nextInt(10000));
                        screenWriter.setLocation(location1);

                        movie.setScreenwriter(screenWriter);
                        collectionManager.add(movie);

                    });
            collectionManager.saveCollection(path);
            return new CommandMessage("В коллекцию успешно добавлено " + N + " элементов");
        }
    }
}



