package cat.uvic.teknos.coursemanagement.backoffice;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

import static cat.uvic.teknos.coursemanagement.backoffice.IOUtils.*;

public class CourseManager {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final Properties properties = new Properties();
    private final ModelFactory modelFactory;

    public CourseManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory, ModelFactory modelFactory) throws IOException {
        this.in = in;
        this.out = out;

        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        out.println("Courses:");
        out.println("Type:");
        out.println("1 to insert a new course");
        out.println("2 to modify a course data");
        out.println("3 to show a course details");
        out.println("4 to show all the courses");
        out.println("5 to delete a course data");
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
        var courseRepository = repositoryFactory.getCourseRepository();
        var course = modelFactory.createCourse();
        out.println("Please enter the id of the course you would like to delete");
        int id = Integer.parseInt(readLine(in));
        course.setId(id);
        courseRepository.delete(course);
    }
    private void get() {
        out.println("Please enter the id of the course you would like to search");
        int id = Integer.parseInt(readLine(in));
        var courseRepository = repositoryFactory.getCourseRepository();
        var course = courseRepository.get(id);
        String[] headers = {"ID","NAME"};
        String[][] courseT = {{String.valueOf(course.getId()),course.getName()}};
        if(course != null) {
            System.out.println(AsciiTable.getTable(headers,courseT));
        }
    }

    private void insert() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        var course = modelFactory.createCourse();
        var courseRepository = repositoryFactory.getCourseRepository();

        System.out.println("Please enter the name of the course");
        String name = readLine(in);
        course.setName(name);

        courseRepository.save(course);
        out.println("Course inserted successfully");
    }

    private void update() {
        var course = modelFactory.createCourse();
        var courseRepository = repositoryFactory.getCourseRepository();
        Set<Course> courses = courseRepository.getAll();
        for (var c : courses) {
            System.out.println("ID:" + course.getId() + "     " + "Name: " + course.getName());
        }
        System.out.println("Please enter the id of the course you want to update");
        int id = Integer.parseInt(readLine(in));
        course.setId(id);
        System.out.println("Please enter the name of the course");
        String description = readLine(in);
        course.setName(description);

        courseRepository.save(course);
        out.println("Course inserted successfully");
    }

    private void getAll() {
        out.println("These are all the courses: ");
        var courseRepository = repositoryFactory.getCourseRepository();
        Set<Course> courses = courseRepository.getAll();
        String[] headers = {"ID","NAME"};
        String[] [] coursesList = new String[courses.toArray().length][headers.length];
        var index = 0;
        for (var course : courses) {
            String[] courseT = {String.valueOf(course.getId()),course.getName()};
            coursesList[index] = courseT;
            index ++;
        }
        System.out.println(AsciiTable.getTable(headers,coursesList));
    }
}