package cat.uvic.teknos.coursemanagement.backoffice;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.models.Student;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cat.uvic.teknos.coursemanagement.backoffice.IOUtils.*;

public class StudentManager {
    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final Properties properties = new Properties();
    private final ModelFactory modelFactory;

    public StudentManager(BufferedReader in, PrintStream out, RepositoryFactory repositoryFactory, ModelFactory modelFactory) throws IOException {
        this.in = in;
        this.out = out;

        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        properties.load(App.class.getResourceAsStream("/app.properties"));
    }

    public void start() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        out.println("Students:");
        out.println("Type:");
        out.println("1 to insert a new student");
        out.println("2 to modify a student data");
        out.println("3 to show a student details");
        out.println("4 to show all the students");
        out.println("5 to delete a student data");
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
        var studentRepository = repositoryFactory.getStudentRepository();
        var student = modelFactory.createStudent();
        out.println("Please enter the id of the student you would like to delete");
        int id = Integer.parseInt(readLine(in));
        student.setId(id);
        studentRepository.delete(student);
    }
    private void get() {
        out.println("Please enter the id of the student you would like to search");
        int id = Integer.parseInt(readLine(in));
        var studentRepository = repositoryFactory.getStudentRepository();
        var student = studentRepository.get(id);
        String[] headers = {"ID","FIRST NAME", "LAST NAME", "ADDRESS", "GENRE", "BORN ON"};
        String[][] studentT = {{String.valueOf(student.getId()),student.getFirstName(),student.getLastName(), String.valueOf(student.getAddress().getId()), String.valueOf(student.getGenre().getId()),String.valueOf(student.getBornOn())}};
        if(student != null) {
            System.out.println(AsciiTable.getTable(headers,studentT));
        }


    }

    private void insert() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        var student = modelFactory.createStudent();
        var studentRepository = repositoryFactory.getStudentRepository();
        var addressRepository = repositoryFactory.getAddressRepository();
        var address = modelFactory.createAddress();
        var genreRepository = repositoryFactory.getGenreRepository();
        System.out.println("Please enter the first name of the student");
        String firstName = readLine(in);
        student.setFirstName(firstName);
        System.out.println("Please enter the last name of the student");
        String lastName = readLine(in);
        student.setLastName(lastName);
        System.out.println("Please enter the street of the student");
        String street = readLine(in);
        address.setStreet(street);
        System.out.println("Please enter the ZIP of the student");
        String zip = readLine(in);
        address.setZip(zip);
        addressRepository.save(address);
        student.setAddress(address);
        System.out.println("Please enter the gender ID of the student");
        var genders = genreRepository.getAll();
        for(var g : genders) {
            System.out.println(g.getId() + "   " + g.getDescription());
        }
        Integer id = Integer.parseInt(readLine(in));
        student.setGenre(genreRepository.get(id));
        System.out.println("Please enter the birthday of the student (YYYY-MM-DD)");
        String birthday = readLine(in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(birthday, formatter);
        student.setBornOn(date);

        studentRepository.save(student);
        out.println("Student inserted successfully");
    }

    private void update() {
        var student = modelFactory.createStudent();
        var studentRepository = repositoryFactory.getStudentRepository();
        var addressRepository = repositoryFactory.getAddressRepository();
        var address = modelFactory.createAddress();
        var genreRepository = repositoryFactory.getGenreRepository();
        var students = studentRepository.getAll();
        for(var s : students){
            System.out.println("ID: " + s.getId() + "      " + " Name:" + s.getFirstName() + " " + s.getLastName());
        }
        System.out.println("Please enter the id of the student you want to update: ");
        Integer id = Integer.parseInt(readLine(in));
        student.setId(id);
        System.out.println("Please enter the first name of the student");
        String firstName = readLine(in);
        student.setFirstName(firstName);
        System.out.println("Please enter the last name of the student");
        String lastName = readLine(in);
        student.setLastName(lastName);
        System.out.println("Please enter the street of the student");
        String street = readLine(in);
        address.setStreet(street);
        System.out.println("Please enter the ZIP of the student");
        String zip = readLine(in);
        address.setZip(zip);
        addressRepository.save(address);
        student.setAddress(address);
        System.out.println("Please enter the gender ID of the student");
        var genders = genreRepository.getAll();
        for(var g : genders) {
            System.out.println(g.getId() + "   " + g.getDescription());
        }
        Integer idg = Integer.parseInt(readLine(in));
        student.setGenre(genreRepository.get(idg));
        System.out.println("Please enter the birthday of the student (YYYY-MM-DD)");
        String birthday = readLine(in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(birthday, formatter);
        student.setBornOn(date);

        studentRepository.save(student);
        out.println("Student inserted successfully");
    }

    private void getAll() {
        out.println("These are all the students: ");
        var studentRepository = repositoryFactory.getStudentRepository();
        Set<Student> students = studentRepository.getAll();
        String[] headers = {"ID","FIRST NAME", "LAST NAME", "ADDRESS", "GENRE", "BORN ON"};
        String[] [] studentsList = new String[students.toArray().length][headers.length];
        var index = 0;
        for (Student student : students) {
            String[] studentT = {String.valueOf(student.getId()),student.getFirstName(),student.getLastName(), String.valueOf(student.getAddress().getId()), String.valueOf(student.getGenre().getId()),String.valueOf(student.getBornOn())};
            studentsList[index] = (studentT);
            index ++;
        }
        System.out.println(AsciiTable.getTable(headers,studentsList));
    }
}