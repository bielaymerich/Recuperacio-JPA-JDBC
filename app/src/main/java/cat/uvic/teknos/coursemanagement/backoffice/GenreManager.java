package cat.uvic.teknos.coursemanagement.backoffice;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

import static cat.uvic.teknos.coursemanagement.backoffice.IOUtils.*;

public class GenreManager {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final Properties properties = new Properties();
    private final ModelFactory modelFactory;

    public GenreManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory, ModelFactory modelFactory) throws IOException {
        this.in = in;
        this.out = out;

        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        out.println("Genres:");
        out.println("Type:");
        out.println("1 to insert a new genre");
        out.println("2 to modify a genre data");
        out.println("3 to show a genre details");
        out.println("4 to show all the genres");
        out.println("5 to delete a genre data");
        out.println("exit to exit");

        var command = "";
        do {
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> get();
                case "4" -> getAll();
                case "5" -> delete();
                default -> { if(!command.equalsIgnoreCase("exit"))out.println("Invalid command");
                }}

        } while (!command.equalsIgnoreCase("exit"));

        out.println("Bye!");
    }
    private void delete(){
        var genreRepository = repositoryFactory.getGenreRepository();
        var genre = modelFactory.createGenre();
        out.println("Please enter the id of the genre you would like to delete");
        int id = Integer.parseInt(readLine(in));
        genre.setId(id);
        genreRepository.delete(genre);
    }
    private void get() {
        out.println("Please enter the id of the genre you would like to search");
        int id = Integer.parseInt(readLine(in));
        var genreRepository = repositoryFactory.getGenreRepository();
        var genre = genreRepository.get(id);
        String[] headers = {"ID","DESCCRIPTION"};
        String[][] genreT = {{String.valueOf(genre.getId()),genre.getDescription()}};
        if(genre != null) {
            System.out.println(AsciiTable.getTable(headers,genreT));
        }


    }

    private void insert() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        var genre = modelFactory.createGenre();
        var genreRepository = repositoryFactory.getGenreRepository();

        System.out.println("Please enter the description of the genre");
        String description = readLine(in);
        genre.setDescription(description);

        genreRepository.save(genre);
        out.println("Genre inserted successfully");
    }

    private void update() {
        var genre = modelFactory.createGenre();
        var genreRepository = repositoryFactory.getGenreRepository();
        Set<Genre> genres = genreRepository.getAll();
        for (var genre1 : genres) {
            System.out.println("ID:" + genre1.getId() + "     " + "Description: " + genre1.getDescription());
        }
        System.out.println("Please enter the id of the genre you want to update");
        int id = Integer.parseInt(readLine(in));
        genre.setId(id);
        System.out.println("Please enter the description of the genre");
        String description = readLine(in);
        genre.setDescription(description);

        genreRepository.save(genre);
        out.println("Genre inserted successfully");
    }

    private void getAll() {
        out.println("These are all the genres: ");
        var genreRepository = repositoryFactory.getGenreRepository();
        Set<Genre> genres = genreRepository.getAll();
        String[] headers = {"ID","DESCRIPTION"};
        String[] [] genresList = new String[genres.toArray().length][headers.length];
        var index = 0;
        for (var genre : genres) {
            String[] genreT = {String.valueOf(genre.getId()),genre.getDescription()};
            genresList[index] = genreT;
            index ++;
        }
        System.out.println(AsciiTable.getTable(headers,genresList));
    }
}